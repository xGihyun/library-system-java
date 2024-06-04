package admin;

import assets.Colors;
import entities.*;
import views.Sidebar;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookList extends JFrame {
  private Connection conn;
  private List<Book> selectedBooks = new ArrayList<>();
  private boolean canBorrow;
  private JButton borrowButton;
  private User user;

  public BookList(Connection conn) {
    this.conn = conn;
    this.user = Session.getInstance().getUser();

    SwingUtilities.invokeLater(this::createAndShowBookList);
  }

  private void createAndShowBookList() {
    setTitle("Book List");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    add(new Sidebar(conn, this), BorderLayout.WEST);

    // Title Label
    // JLabel titleLabel = new JLabel("Book List");
    // titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    // titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    // add(titleLabel, BorderLayout.NORTH);

    // Main panel for the book grid
    JPanel bookPanel = new JPanel(new GridBagLayout());
    bookPanel.setBackground(Colors.MANTLE);
    add(new JScrollPane(bookPanel), BorderLayout.CENTER);

    List<Book> books = fetchBooksFromDatabase();

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;

    for (Book book : books) {
      JPanel bookCard = createBookCard(book);
      bookPanel.add(bookCard, gbc);

      if (gbc.gridx == 2) {
        gbc.gridx = 0;
        gbc.gridy++;
      } else {
        gbc.gridx++;
      }
    }

    // Add Book Button
    // JButton addBookButton = new JButton("Add Book");
    // addBookButton.setFont(new Font("Arial", Font.PLAIN, 18));
    // addBookButton.setBackground(Colors.GREEN);
    // addBookButton.setForeground(Colors.BASE);
    // addBookButton.setFocusPainted(false);
    // addBookButton.setBorderPainted(false);
    // addBookButton.addActionListener(e -> openAddBookForm());
    // add(addBookButton, BorderLayout.SOUTH);

    // Borrow Button
    borrowButton = new JButton("Borrow Selected Books");
    borrowButton.setFont(new Font("Arial", Font.BOLD, 16));
    borrowButton.setBackground(Colors.GREEN);
    borrowButton.setForeground(Colors.BASE);
    borrowButton.setVisible(false);
    borrowButton.addActionListener(e -> borrowSelectedBooks());
    add(borrowButton, BorderLayout.SOUTH);

    setVisible(true);
  }

  private List<Book> fetchBooksFromDatabase() {
    List<Book> books = new ArrayList<>();
    String query = "SELECT b.id, b.isbn, b.title, b.category, b.author_id, b.image_url, a.first_name AS author_first_name, a.middle_name AS author_middle_name, a.last_name AS author_last_name, a.suffix_name AS author_suffix_name, bb.user_id AS borrower_id, "
        +
        " (SELECT returned_at IS NULL FROM book_borrows WHERE book_id = b.id AND returned_at IS NULL LIMIT 1) AS borrowed "
        +
        " FROM books b"
        +
        " LEFT JOIN authors a ON b.author_id = a.id"
        +
        " LEFT JOIN book_borrows bb ON bb.book_id = b.id";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String isbn = rs.getString("isbn");
        String title = rs.getString("title");
        String category = rs.getString("category");
        String authorId = rs.getString("author_id");
        boolean borrowed = rs.getBoolean("borrowed");
        String imageUrl = rs.getString("image_url");
        String borrowerId = rs.getString("borrower_id");

        // TODO: Put the full author name on the book
        String authorFirstName = rs.getString("author_first_name");
        String authorMiddleName = rs.getString("author_middle_name");
        String authorLastName = rs.getString("author_last_name");
        String authorSuffixName = rs.getString("author_suffix_name");

        books.add(new Book(id, isbn, title, category, authorId, borrowed, imageUrl, authorFirstName, borrowerId));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  private JPanel createBookCard(Book book) {
    JPanel card = new JPanel(new BorderLayout());
    card.setPreferredSize(new Dimension(300, 400));
    card.setBorder(BorderFactory.createLineBorder(Colors.OVERLAY1, 2));
    card.setBackground(Colors.MANTLE);

    // Book Image
    JLabel imageLabel = new JLabel();
    ImageIcon imageIcon;

    if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
      imageIcon = new ImageIcon(getClass().getResource("../assets/images/" + book.getImageUrl()));
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
    detailsPanel.add(createDetailLabel("Title: " + book.getTitle()));
    detailsPanel.add(createDetailLabel("Category: " + book.getCategory()));
    detailsPanel.add(createDetailLabel("Author: " + book.getAuthorFullName()));

    if (book.isBorrowed()) {
      String label = "Status: Borrowed";

      if (book.getBorrowerId().equalsIgnoreCase(user.getId())) {
        label += " (You)";
      }

      detailsPanel.add(createDetailLabel(label, Colors.RED));
    } else {
      detailsPanel.add(createDetailLabel("Status: Returned", Colors.GREEN));
    }

    card.add(detailsPanel, BorderLayout.SOUTH);

    Font font = new Font("Arial", Font.PLAIN, 18);

    JCheckBox selectCheckBox = new JCheckBox("Select");
    selectCheckBox.setFont(font);
    selectCheckBox.setBackground(Colors.BASE);
    selectCheckBox.setForeground(Colors.TEXT);
    selectCheckBox.setFocusPainted(false);
    selectCheckBox.setBorderPainted(false);

    selectCheckBox.setEnabled(!book.isBorrowed());

    selectCheckBox.addActionListener(e -> {
      if (selectCheckBox.isSelected()) {
        selectedBooks.add(book);
      } else {
        selectedBooks.remove(book);
      }

      borrowButton.setVisible(!selectedBooks.isEmpty());
    });
    detailsPanel.add(selectCheckBox);

    return card;
  }

  private JLabel createDetailLabel(String text) {
    return createDetailLabel(text, Colors.TEXT);
  }

  private JLabel createDetailLabel(String text, Color color) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Arial", Font.PLAIN, 16));
    label.setForeground(color);

    return label;
  }

  private List<Author> fetchAuthorsFromDatabase() {
    List<Author> authors = new ArrayList<>();
    String query = "SELECT * FROM authors";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String id = rs.getString("id");
        String firstName = rs.getString("first_name");
        String middleName = rs.getString("middle_name");
        String lastName = rs.getString("last_name");
        String suffixName = rs.getString("suffix_name");

        authors.add(new Author(id, firstName, middleName, lastName, suffixName));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return authors;
  }

  private boolean addBookToDatabase(String isbn, String title, String category, String authorId, String imageUrl) {
    String query = "INSERT INTO books (isbn, title, category, author_id, image_url) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, isbn);
      stmt.setString(2, title);
      stmt.setString(3, category);
      stmt.setString(4, authorId);
      stmt.setString(5, imageUrl);

      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void borrowSelectedBooks() {
    if (user == null) {
      JOptionPane.showMessageDialog(this, "User not authenticated.");
      return;
    }

    if (user.getRole().equalsIgnoreCase("student") && selectedBooks.size() > 2) {
      JOptionPane.showMessageDialog(this, "Sorry! You have exceeded the number of books (2) to be borrowed.");
      return;
    }

    if (user.getRole().equalsIgnoreCase("teacher") && selectedBooks.size() > 5) {
      JOptionPane.showMessageDialog(this, "Sorry! You have exceeded the number of books (5) to be borrowed.");
      return;
    }

    try {
      // NOTE: Only students get 3 days due date
      String query = "INSERT INTO book_borrows (due_date, book_id, user_id) VALUES (DATE_ADD(CURDATE(), INTERVAL 3 DAY), ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(query);

      for (Book book : selectedBooks) {
        stmt.setInt(1, book.getId());
        stmt.setString(2, user.getId());
        stmt.addBatch();
      }

      stmt.executeBatch();
      JOptionPane.showMessageDialog(this, "Books borrowed successfully!");

      dispose();
      new BookList(conn);
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error borrowing books.");
    }
  }

  // NOTE: Not sure if we still need this, would be cool though
  private void openAddBookForm() {
    JDialog addBookDialog = new JDialog(this, "Add Book", true);
    addBookDialog.setSize(500, 400);
    addBookDialog.setLayout(new GridBagLayout());
    addBookDialog.setLocationRelativeTo(null);

    Container container = addBookDialog.getContentPane();

    container.setBackground(Colors.BASE);
    container.setForeground(Colors.TEXT);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;

    JLabel isbnLabel = new JLabel("ISBN:");
    isbnLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    isbnLabel.setForeground(Colors.TEXT);
    addBookDialog.add(isbnLabel, gbc);

    gbc.gridx = 1;
    JTextField isbnField = new JTextField(20);
    isbnField.setFont(new Font("Arial", Font.PLAIN, 18));
    isbnField.setPreferredSize(new Dimension(200, 30));
    addBookDialog.add(isbnField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    JLabel titleLabel = new JLabel("Title:");
    titleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    titleLabel.setForeground(Colors.TEXT);
    addBookDialog.add(titleLabel, gbc);

    gbc.gridx = 1;
    JTextField titleField = new JTextField(20);
    titleField.setFont(new Font("Arial", Font.PLAIN, 18));
    titleField.setPreferredSize(new Dimension(200, 30));
    addBookDialog.add(titleField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    categoryLabel.setForeground(Colors.TEXT);
    addBookDialog.add(categoryLabel, gbc);

    gbc.gridx = 1;
    JComboBox<String> categoryComboBox = new JComboBox<>(new String[] { "fictional", "non-fictional", "academic" });
    categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
    categoryComboBox.setPreferredSize(new Dimension(200, 30));
    addBookDialog.add(categoryComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    JLabel authorLabel = new JLabel("Author:");
    authorLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    authorLabel.setForeground(Colors.TEXT);
    addBookDialog.add(authorLabel, gbc);

    gbc.gridx = 1;

    JComboBox<Author> authorComboBox = new JComboBox<>();
    List<Author> authors = fetchAuthorsFromDatabase();

    for (Author author : authors) {
      authorComboBox.addItem(author);
    }

    authorComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
    authorComboBox.setPreferredSize(new Dimension(200, 30));
    addBookDialog.add(authorComboBox, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    JLabel imageLabel = new JLabel("Image URL:");
    imageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    addBookDialog.add(imageLabel, gbc);

    gbc.gridx = 1;
    JTextField imageField = new JTextField(20);
    imageField.setFont(new Font("Arial", Font.PLAIN, 18));
    imageField.setPreferredSize(new Dimension(200, 30));
    addBookDialog.add(imageField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;

    JButton addButton = new JButton("Add Book");

    addButton.setFont(new Font("Arial", Font.PLAIN, 18));
    addButton.setBackground(Colors.BLUE);
    addButton.setForeground(Colors.BASE);
    addButton.setFocusPainted(false);
    addButton.setBorderPainted(false);
    addButton.addActionListener(e -> {
      String isbn = isbnField.getText();
      String title = titleField.getText();
      String category = (String) categoryComboBox.getSelectedItem();
      Author selectedAuthor = (Author) authorComboBox.getSelectedItem();
      String authorId = selectedAuthor.getId();
      String imageUrl = imageField.getText();

      if (addBookToDatabase(isbn, title, category, authorId, imageUrl)) {
        addBookDialog.dispose();
        JOptionPane.showMessageDialog(this, "Book added successfully");
        dispose();
        new BookList(conn);
      } else {
        JOptionPane.showMessageDialog(this, "Failed to add book");
      }
    });
    addBookDialog.add(addButton, gbc);

    addBookDialog.setVisible(true);
  }

}
