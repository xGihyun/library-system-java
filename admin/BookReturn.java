package admin;

import assets.Colors;
import entities.User;
import views.Sidebar;
import entities.Session;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BookReturn extends JFrame {

  private Connection conn;
  private User user;
  private JPanel bookPanel;
  private JButton returnButton;
  private JButton payButton;
  private List<JCheckBox> bookCheckBoxes;
  private JLabel penaltyLabel;
  private JTextField paymentField;

  public BookReturn(Connection conn) {
    this.conn = conn;
    this.user = Session.getInstance().getUser();
    bookCheckBoxes = new ArrayList<>();

    SwingUtilities.invokeLater(this::display);
  }

  private void display() {
    setTitle("Book Return");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    add(new Sidebar(conn, this), BorderLayout.WEST);

    // Main panel for the book list
    bookPanel = new JPanel(new GridBagLayout());
    bookPanel.setBackground(Colors.BASE);
    add(new JScrollPane(bookPanel), BorderLayout.CENTER);

    loadBorrowedBooks();

    // Buttons Panel
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setBackground(Colors.BASE);
    add(buttonsPanel, BorderLayout.SOUTH);

    // Return Button
    returnButton = new JButton("Return Selected Books");
    returnButton.setBackground(Colors.GREEN);
    returnButton.setForeground(Colors.TEXT);
    returnButton.addActionListener(e -> returnSelectedBooks());
    buttonsPanel.add(returnButton);

    // Payment Components
    penaltyLabel = new JLabel();
    penaltyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    penaltyLabel.setForeground(Colors.TEXT);
    buttonsPanel.add(penaltyLabel);

    paymentField = new JTextField(10);
    paymentField.setFont(new Font("Arial", Font.PLAIN, 16));
    buttonsPanel.add(paymentField);

    // Pay Button
    payButton = new JButton("Pay Penalty");
    payButton.setBackground(Colors.GREEN);
    payButton.setForeground(Colors.TEXT);
    payButton.addActionListener(e -> payPenalty());
    buttonsPanel.add(payButton);

    setVisible(true);
  }

  private void loadBorrowedBooks() {
    String query = "SELECT bb.id, b.title, bb.due_date, bb.returned_at, "
        + " CASE WHEN bb.returned_at IS NULL THEN DATEDIFF(CURDATE(), bb.due_date) ELSE 0 END AS days_late, "
        + " COALESCE(p.amount, 0) AS amount"
        + " FROM book_borrows bb "
        + " JOIN books b ON bb.book_id = b.id "
        + " LEFT JOIN penalties p ON p.user_role = ? "
        + " WHERE bb.user_id = ? AND bb.returned_at IS NULL";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, user.getRole()); // Assuming the User class has a getRole() method
      stmt.setString(2, user.getId());
      ResultSet rs = stmt.executeQuery();

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.gridx = 0;
      gbc.gridy = 0;

      while (rs.next()) {
        int borrowId = rs.getInt("id");
        String title = rs.getString("title");
        LocalDate dueDate = rs.getDate("due_date").toLocalDate();
        int daysLate = rs.getInt("days_late");
        double penaltyAmount = rs.getDouble("amount");

        JCheckBox checkBox = new JCheckBox(title + " (Due: " + dueDate + ")");
        checkBox.setFont(new Font("Arial", Font.PLAIN, 16));
        checkBox.setBackground(Colors.MANTLE);
        checkBox.setForeground(Colors.TEXT);
        checkBox.putClientProperty("borrowId", borrowId);
        checkBox.putClientProperty("daysLate", daysLate);
        checkBox.putClientProperty("penaltyAmount", penaltyAmount);
        bookCheckBoxes.add(checkBox);
        bookPanel.add(checkBox, gbc);

        gbc.gridy++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void returnSelectedBooks() {
    List<Integer> selectedBooks = new ArrayList<>();
    int totalDaysLate = 0;
    double totalPenaltyAmount = 0;

    for (JCheckBox checkBox : bookCheckBoxes) {
      if (checkBox.isSelected()) {
        int borrowId = (int) checkBox.getClientProperty("borrowId");
        int daysLate = (int) checkBox.getClientProperty("daysLate");
        double penaltyAmount = (double) checkBox.getClientProperty("penaltyAmount");

        selectedBooks.add(borrowId);
        totalDaysLate += daysLate;
        totalPenaltyAmount += daysLate * penaltyAmount;
      }
    }

    if (totalDaysLate > 0) {
      penaltyLabel.setText("Total Penalty: $" + totalPenaltyAmount + " (Days Late: " + totalDaysLate + ")");
      returnButton.setEnabled(false);
    } else {
      updateReturnDates(selectedBooks);
    }
  }

  private void payPenalty() {
    double totalPenaltyAmount = Double.parseDouble(penaltyLabel.getText().replaceAll("[^\\d.]", ""));
    double payment = Double.parseDouble(paymentField.getText());

    if (payment >= totalPenaltyAmount) {
      List<Integer> selectedBooks = new ArrayList<>();
      for (JCheckBox checkBox : bookCheckBoxes) {
        if (checkBox.isSelected()) {
          selectedBooks.add((int) checkBox.getClientProperty("borrowId"));
        }
      }

      recordPenaltyPayment(selectedBooks, totalPenaltyAmount);
      updateReturnDates(selectedBooks);
      penaltyLabel.setText("");
      paymentField.setText("");
      returnButton.setEnabled(true);
    } else {
      JOptionPane.showMessageDialog(this, "Payment not enough to cover the penalty.");
    }
  }

  private void recordPenaltyPayment(List<Integer> selectedBooks, double totalPenaltyAmount) {
    String insertQuery = "INSERT INTO book_penalties (payment_amount, book_borrow_id, penalty_id) VALUES (?, ?, (SELECT id FROM penalties WHERE user_role = ?))";
    try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
      for (int borrowId : selectedBooks) {
        stmt.setDouble(1, totalPenaltyAmount);
        stmt.setInt(2, borrowId);
        stmt.setString(3, user.getRole());
        stmt.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateReturnDates(List<Integer> selectedBooks) {
    String updateQuery = "UPDATE book_borrows SET returned_at = CURDATE() WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
      for (int borrowId : selectedBooks) {
        stmt.setInt(1, borrowId);
        stmt.executeUpdate();
      }
      JOptionPane.showMessageDialog(this, "Books returned successfully!");
      dispose();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
