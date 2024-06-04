package entities;

import java.util.Date;

public class BorrowedBook extends Book {
  private Date borrowedAt;
  private Date dueDate;
  private double penaltyAmount;
  private int bookBorrowId;

  public BorrowedBook(int id, String isbn, String title, String category, String authorId, boolean borrowed, String imageUrl, String authorFullName, String borrowerId, Date borrowedAt, Date dueDate, double penaltyAmount, int bookBorrowId, int copyright, String publisherName) {
    super(id, isbn, title, category, authorId, borrowed, imageUrl, authorFullName, borrowerId, copyright, publisherName);
    this.borrowedAt = borrowedAt;
    this.dueDate = dueDate;
    this.penaltyAmount = penaltyAmount;
    this.bookBorrowId = bookBorrowId;
  }

  public Date getBorrowedAt() {
    return borrowedAt;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public double getPenaltyAmount() {
    return penaltyAmount;
  }

  public int getBookBorrowId(){
    return bookBorrowId;
  }
}
