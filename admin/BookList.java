package admin;

import assets.Colors;
import entities.*;
import views.Sidebar;
import views.TopPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookList extends JFrame {
  private Connection conn;
  private List<Book> selectedBooks = new ArrayList<>();
  // private boolean canBorrow;
  private JButton borrowButton;
  private User user;
  private int maxBorrowedBooksCount;

  public BookList(Connection conn) {
    this.conn = conn;
    this.user = Session.getInstance().getUser();

    if (user.getRole().equalsIgnoreCase("student")) {
      maxBorrowedBooksCount = 2;
    } else if (user.getRole().equalsIgnoreCase("teacher")) {
      maxBorrowedBooksCount = 5;
    }

    SwingUtilities.invokeLater(this::createAndShowBookList);
  }

  // private JPanel createUserInfoPanel() {
  //   JPanel userInfoPanel = new JPanel(new BorderLayout());
  //   userInfoPanel.setBackground(Colors.BASE);
  //   userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
  //
  //   // User name label
  //   JLabel userNameLabel = new JLabel(user.getFullName());
  //   userNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
  //   userNameLabel.setForeground(Colors.TEXT);
  //
  //   // User avatar
  //   JLabel userAvatarLabel = new JLabel();
  //   ImageIcon avatarIcon;
  //
  //   if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
  //     avatarIcon = new ImageIcon(getClass().getResource("../assets/images/avatars/" + user.getAvatarUrl()));
  //   } else {
  //     avatarIcon = new ImageIcon(getClass().getResource("../assets/images/bocchi.jpg"));
  //   }
  //
  //   Image img = avatarIcon.getImage();
  //   Image resizedImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
  //   avatarIcon = new ImageIcon(resizedImg);
  //
  //   userAvatarLabel.setIcon(avatarIcon);
  //
  //   // Adding components to the panel
  //   JPanel userDetailPanel = new JPanel();
  //   userDetailPanel.setBackground(Colors.BASE);
  //   userDetailPanel.add(userNameLabel);
  //   userDetailPanel.add(userAvatarLabel);
  //
  //   userInfoPanel.add(userDetailPanel, BorderLayout.EAST);
  //
  //   return userInfoPanel;
  // }

  private void createAndShowBookList() {
    setTitle("Book List");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    add(new Sidebar(conn, this), BorderLayout.WEST);

    TopPanel foo = new TopPanel();
    // JPanel userInfoPanel = createUserInfoPanel();
    add(foo.createTopPanel(), BorderLayout.NORTH);

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
    gbc.insets = new Insets(4, 4, 4, 4);
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

    int borrowedBooksCount = fetchBorrowedBooksCount();

    maxBorrowedBooksCount -= borrowedBooksCount;

    System.out.println(maxBorrowedBooksCount);

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
    String query = "SELECT b.id, b.isbn, b.title, b.category, b.author_id, b.image_url, b.copyright, "
        +
        " a.first_name AS author_first_name, a.middle_name AS author_middle_name, a.last_name AS author_last_name, a.suffix_name AS author_suffix_name, latest_bb.user_id AS borrower_id,   "
        +
        " p.name AS publisher_name, "
        +
        " CASE WHEN latest_bb.returned_at IS NULL AND latest_bb.user_id IS NOT NULL THEN 1 ELSE 0 END AS borrowed"
        +
        " FROM books b"
        +
        " LEFT JOIN authors a ON b.author_id = a.id"
        +
        " LEFT JOIN publishers p ON p.id = b.publisher_id"
        +
        " LEFT JOIN (SELECT book_id, user_id, returned_at FROM book_borrows bb1 WHERE returned_at IS NULL AND NOT EXISTS "
        +
        " (SELECT 1 FROM book_borrows bb2 WHERE bb1.book_id = bb2.book_id AND bb1.borrowed_at < bb2.borrowed_at)"
        + " ) AS latest_bb ON latest_bb.book_id = b.id";

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
        int copyright = rs.getInt("copyright");
        String publisherName = rs.getString("publisher_name");

        String authorFirstName = rs.getString("author_first_name");
        String authorMiddleName = rs.getString("author_middle_name");
        String authorLastName = rs.getString("author_last_name");
        String authorSuffixName = rs.getString("author_suffix_name");
        Author author = new Author(authorFirstName, authorMiddleName, authorLastName, authorSuffixName);

        books.add(new Book(id, isbn, title, category, authorId, borrowed, imageUrl, author.getFullName(), borrowerId,
            copyright, publisherName));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return books;
  }

  private JPanel createBookCard(Book book) {
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
    card.add(imageLabel, BorderLayout.NORTH);

    // Book Details
    JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
    detailsPanel.setBackground(Colors.BASE);
    detailsPanel.setForeground(Colors.TEXT);
    detailsPanel.add(createDetailLabel(book.getTitle(), Font.BOLD));
    detailsPanel.add(createDetailLabel(book.getAuthorFullName()));
    detailsPanel.add(createDetailLabel(book.getPublisherName()));
    detailsPanel.add(createDetailLabel(book.getIsbn()));
    detailsPanel.add(createDetailLabel("Copyright " + book.getCopyright()));
    detailsPanel.add(categoryLabel(book.getCategory()));
    detailsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

    if (book.isBorrowed()) {
      String label = "Borrowed";

      if (book.getBorrowerId().equalsIgnoreCase(user.getId())) {
        label += " (You)";
      }

      detailsPanel.add(createDetailLabel(label, Colors.RED));
    } else {
      if (!book.getCategory().equalsIgnoreCase("academic")) {
        detailsPanel.add(createDetailLabel("Returned", Colors.GREEN));
      }
    }

    card.add(detailsPanel, BorderLayout.CENTER);

    Font font = new Font("Arial", Font.PLAIN, 18);

    JCheckBox selectCheckBox = new JCheckBox("Select");
    Dimension selectCheckboxDimension = selectCheckBox.getBounds().getSize();
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

    if (!book.getCategory().equalsIgnoreCase("academic")) {
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
            borrowButton.setVisible(!selectedBooks.isEmpty());
          }
        }
      });
    } else {
      JPanel ghostElement = new JPanel();
      ghostElement.setPreferredSize(selectCheckboxDimension);
      ghostElement.setBackground(Colors.BASE);

      detailsPanel.add(ghostElement);
    }

    return card;
  }

  private JLabel categoryLabel(String category) {
    String cap = category.substring(0, 1).toUpperCase() + category.substring(1);

    if (category.equalsIgnoreCase("fictional")) {
      return createDetailLabel(cap, Colors.MAUVE, Font.ITALIC);
    }

    if (category.equalsIgnoreCase("non-fictional")) {
      return createDetailLabel(cap, Colors.SKY, Font.ITALIC);
    }

    if (category.equalsIgnoreCase("academic")) {
      return createDetailLabel(cap, Colors.PEACH, Font.ITALIC);
    }

    return createDetailLabel(cap, Colors.TEXT, Font.ITALIC);
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

  private int fetchBorrowedBooksCount() {
    String query = "SELECT COUNT(*) AS count FROM book_borrows"
        + " WHERE returned_at IS NULL AND user_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, user.getId());

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        int currentBorrowedBooksCount = rs.getInt("count");

        return currentBorrowedBooksCount;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0;
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

    if (selectedBooks.size() > maxBorrowedBooksCount) {
      JOptionPane.showMessageDialog(this, "Sorry! You have exceeded the number of books to be borrowed.");
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
