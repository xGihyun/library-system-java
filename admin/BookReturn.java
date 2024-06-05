package admin;

import assets.Colors;
import entities.User;
import views.Sidebar;
import entities.BorrowedBook;
import entities.Session;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BookReturn extends JFrame {

  private Connection conn;
  private User user;
  private JPanel bookPanel;
  private JButton returnButton;
  private JButton payButton;
  private List<BorrowedBook> selectedBooks = new ArrayList<>();
  private JLabel penaltyLabel;
  private JTextField paymentField;

  public BookReturn(Connection conn) {
    this.conn = conn;
    this.user = Session.getInstance().getUser();

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

    returnButton = new JButton("Return Selected Books");
    returnButton.setBackground(Colors.GREEN);
    returnButton.setForeground(Colors.BASE);
    returnButton.addActionListener(e -> returnSelectedBooks());
    returnButton.setVisible(false);
    buttonsPanel.add(returnButton);

    // Penalty stuff but for student only
    if (user.getRole().equalsIgnoreCase("student")) {
      penaltyLabel = new JLabel();
      penaltyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
      penaltyLabel.setForeground(Colors.TEXT);
      buttonsPanel.add(penaltyLabel);

      paymentField = new JTextField(10);
      paymentField.setFont(new Font("Arial", Font.PLAIN, 16));
      buttonsPanel.add(paymentField);

      payButton = new JButton("Pay Penalty");
      payButton.setBackground(Colors.LAVENDER);
      payButton.setForeground(Colors.BASE);
      payButton.addActionListener(e -> payPenalty());
      payButton.setEnabled(false);
      buttonsPanel.add(payButton);

    }

    setVisible(true);
  }

  private void loadBorrowedBooks() {
    List<BorrowedBook> borrowedBooks = fetchBorrowedBooksFromDatabase();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;

    for (BorrowedBook book : borrowedBooks) {
      JPanel bookCard = createBorrowedBookCard(book);
      bookPanel.add(bookCard, gbc);

      if (gbc.gridx == 2) {
        gbc.gridx = 0;
        gbc.gridy++;
      } else {
        gbc.gridx++;
      }
    }
  }

  private List<BorrowedBook> fetchBorrowedBooksFromDatabase() {
    List<BorrowedBook> books = new ArrayList<>();
    String query = "SELECT b.id, b.isbn, b.title, b.category, b.copyright, pub.name AS publisher_name, "
        +
        " a.first_name, a.middle_name, a.last_name, a.suffix_name, bb.borrowed_at, bb.due_date, bb.id AS book_borrow_id, "
        +
        " CASE WHEN CURDATE() > bb.due_date THEN DATEDIFF(CURDATE(), bb.due_date) * pen.amount ELSE 0 END AS penalty_amount, b.image_url "
        +
        " FROM book_borrows bb " +
        " JOIN books b ON bb.book_id = b.id " +
        " JOIN authors a ON b.author_id = a.id " +
        " JOIN publishers pub ON pub.id = b.publisher_id"
        +
        " LEFT JOIN penalties pen ON pen.user_role = ? " +
        " WHERE bb.returned_at IS NULL AND bb.user_id = ?";

    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
      preparedStatement.setString(1, user.getRole());
      preparedStatement.setString(2, user.getId());
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        int bookBorrowId = resultSet.getInt("book_borrow_id");
        int copyright = resultSet.getInt("copyright");
        String isbn = resultSet.getString("isbn");
        String title = resultSet.getString("title");
        String category = resultSet.getString("category");
        String authorFullName = resultSet.getString("first_name") + " " +
            (resultSet.getString("middle_name") != null ? resultSet.getString("middle_name") + " " : "") +
            resultSet.getString("last_name") + " " +
            (resultSet.getString("suffix_name") != null ? resultSet.getString("suffix_name") : "");
        Date borrowedAt = resultSet.getDate("borrowed_at");
        Date dueDate = resultSet.getDate("due_date");
        double penaltyAmount = resultSet.getDouble("penalty_amount");
        String imageUrl = resultSet.getString("image_url");
        String publisherName = resultSet.getString("publisher_name");

        BorrowedBook book = new BorrowedBook(id, isbn, title, category, authorFullName, true, imageUrl, authorFullName,
            user.getId(), borrowedAt, dueDate, penaltyAmount, bookBorrowId, copyright, publisherName);

        books.add(book);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  private JPanel createBorrowedBookCard(BorrowedBook book) {
    JPanel card = new JPanel(new BorderLayout());
    card.setPreferredSize(new Dimension(300, 600)); 
    card.setBorder(BorderFactory.createLineBorder(Colors.OVERLAY1, 2));
    card.setBackground(Colors.MANTLE);

    // Book Image
    JLabel imageLabel = new JLabel();
    ImageIcon imageIcon;

    if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
      imageIcon = new ImageIcon(getClass().getResource("../assets/images/books/" + book.getImageUrl()));
    } else {
      imageIcon = new ImageIcon(getClass().getResource("../assets/images/bocchi.jpg"));
    }

    Image img = imageIcon.getImage();
    Image resizedImg = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
    imageIcon = new ImageIcon(resizedImg);

    imageLabel.setIcon(imageIcon);
    card.add(imageLabel, BorderLayout.CENTER);

    // Book Details
    JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
    detailsPanel.setBackground(Colors.BASE);
    detailsPanel.setForeground(Colors.TEXT);
    detailsPanel.add(createDetailLabel(book.getTitle(), Font.BOLD));
    detailsPanel.add(createDetailLabel(book.getCategory()));
    detailsPanel.add(createDetailLabel(book.getAuthorFullName()));
    detailsPanel.add(createDetailLabel("Borrowed At: " + book.getBorrowedAt().toString()));

    if (user.getRole().equalsIgnoreCase("student")) {
      detailsPanel.add(createDetailLabel("Due Date: " + book.getDueDate().toString()));
      detailsPanel.add(createDetailLabel("Penalty Amount: Php. " + book.getPenaltyAmount()));
    }

    card.add(detailsPanel, BorderLayout.SOUTH);

    Font font = new Font("Arial", Font.PLAIN, 18);

    JCheckBox selectCheckBox = new JCheckBox("Select");
    selectCheckBox.setFont(font);
    selectCheckBox.setBackground(Colors.BASE);
    selectCheckBox.setForeground(Colors.TEXT);
    selectCheckBox.setFocusPainted(false);
    selectCheckBox.setBorderPainted(false);

    selectCheckBox.addActionListener(e -> {
      if (selectCheckBox.isSelected()) {
        selectedBooks.add(book);
      } else {
        selectedBooks.remove(book);
      }

      if (selectCheckBox.isSelected() && book.getPenaltyAmount() > 0) {
        payButton.setEnabled(true);
      } else {
        payButton.setEnabled(false);
      }

      returnButton.setVisible(!selectedBooks.isEmpty());
    });
    detailsPanel.add(selectCheckBox);

    card.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent e) {
        if (!book.isBorrowed()) {
          selectCheckBox.setSelected(!selectCheckBox.isSelected());
          if (selectCheckBox.isSelected()) {
            selectedBooks.add(book);
          } else {
            selectedBooks.remove(book);
          }
          returnButton.setVisible(!selectedBooks.isEmpty());
        }
      }
    });

    return card;
  }

  private JLabel createDetailLabel(String text) {
    return createDetailLabel(text, Colors.TEXT);
  }

  private JLabel createDetailLabel(String text, Color color) {
    return createDetailLabel(text, color, Font.PLAIN);
  }

  private JLabel createDetailLabel(String text, int style) {
    return createDetailLabel(text, Colors.TEXT, style);
  }

  private JLabel createDetailLabel(String text, Color color, int style) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Arial", style, 16));
    label.setForeground(color);
    return label;
  }

  private void returnSelectedBooks() {
    int totalDaysLate = 0;
    double totalPenaltyAmount = 0;

    for (BorrowedBook book : selectedBooks) {
      double penaltyAmount = book.getPenaltyAmount();
      Date currentDate = new Date(System.currentTimeMillis());
      long diffInMilliseconds = currentDate.getTime() - book.getDueDate().getTime();
      totalDaysLate = (int) TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

      System.out.println(penaltyAmount);
      System.out.println(totalDaysLate);

      if (totalDaysLate < 0) {
        totalDaysLate = 0;
      }

      totalPenaltyAmount += penaltyAmount;
    }

    if (totalDaysLate > 0) {
      penaltyLabel.setText("Total Penalty: Php. " + totalPenaltyAmount + " (Days Late: " + totalDaysLate + ")");
      returnButton.setEnabled(false);
    } else {
      updateReturnDates(selectedBooks);
    }
  }

  private void payPenalty() {
    int totalDaysLate = 0;
    double totalPenaltyAmount = 0;

    for (BorrowedBook book : selectedBooks) {
      double penaltyAmount = book.getPenaltyAmount();
      Date currentDate = new Date(System.currentTimeMillis());
      long diffInMilliseconds = currentDate.getTime() - book.getDueDate().getTime();
      totalDaysLate = (int) TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

      System.out.println(penaltyAmount);
      System.out.println(totalDaysLate);

      if (totalDaysLate < 0) {
        totalDaysLate = 0;
      }

      totalPenaltyAmount += penaltyAmount;
    }

    double payment = Double.parseDouble(paymentField.getText());
    double change = payment - totalPenaltyAmount;

    if (payment >= totalPenaltyAmount) {

      recordPenaltyPayment(totalPenaltyAmount);

      JOptionPane.showMessageDialog(this, "Payment successful. Your change is: Php. " + change);

      updateReturnDates(selectedBooks);

      penaltyLabel.setText("");
      paymentField.setText("");
      returnButton.setEnabled(true);
    } else {
      JOptionPane.showMessageDialog(this, "Payment not enough to cover the penalty.");
    }
  }

  private void recordPenaltyPayment(double totalPenaltyAmount) {
    String insertQuery = "INSERT INTO book_penalties (payment_amount, book_borrow_id, penalty_id) VALUES (?, ?, (SELECT id FROM penalties WHERE user_role = ?))";
    try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
      for (BorrowedBook book : selectedBooks) {
        stmt.setDouble(1, totalPenaltyAmount);
        stmt.setInt(2, book.getBookBorrowId());
        stmt.setString(3, user.getRole());
        stmt.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateReturnDates(List<BorrowedBook> selectedBooks) {
    String updateQuery = "UPDATE book_borrows SET returned_at = CURDATE() WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
      for (BorrowedBook book : selectedBooks) {
        stmt.setInt(1, book.getBookBorrowId());
        stmt.executeUpdate();
      }

      JOptionPane.showMessageDialog(this, "Books returned successfully!");
      dispose();
      new BookReturn(conn);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
