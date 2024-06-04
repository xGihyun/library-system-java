package entities;

public class Book {
  private int id;
  private String isbn;
  private String title;
  private String category;
  private String authorId;
  private String imageUrl;
  private String authorFullName;
  private String borrowerId;
  private boolean borrowed;

  public Book(int id, String isbn, String title, String category, String authorId, boolean borrowed, String imageUrl, String authorFullName, String borrowerId) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.category = category;
    this.authorId = authorId;
    this.borrowed = borrowed;
    this.imageUrl = imageUrl;
    this.authorFullName = authorFullName;
    this.borrowerId = borrowerId;
  }

  public int getId() {
    return id;
  }

  public String getIsbn() {
    return isbn;
  }

  public String getTitle() {
    return title;
  }

  public String getCategory() {
    return category;
  }

  public String getAuthorId() {
    return authorId;
  }

  public boolean isBorrowed() {
    return borrowed;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getAuthorFullName() {
    return authorFullName;
  }

  public String getBorrowerId() {
    return borrowerId;
  }
}
