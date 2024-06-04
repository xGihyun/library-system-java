package entities;

public class Book {
  private int id;
  private int copyright;
  private String publisherName;
  private String isbn;
  private String title;
  private String category;
  private String authorId;
  private String imageUrl;
  private String authorFullName;
  private String borrowerId;
  private boolean borrowed;

  public Book(int id, String isbn, String title, String category, String authorId, boolean borrowed, String imageUrl, String authorFullName, String borrowerId, int copyright, String publisherName) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.category = category;
    this.authorId = authorId;
    this.borrowed = borrowed;
    this.imageUrl = imageUrl;
    this.authorFullName = authorFullName;
    this.borrowerId = borrowerId;
    this.publisherName = publisherName;
    this.copyright = copyright;
  }

  public int getId() {
    return id;
  }

  public int getCopyright() {
    return copyright;
  }

  public String getPublisherName() {
    return publisherName;
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
