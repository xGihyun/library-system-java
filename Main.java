import java.sql.Connection;

import admin.BookList;
import admin.BorrowerList;
import auth.Login;
import database.Database;

public class Main {
  public static void main(String[] args) throws Exception {

    System.out.println("Initializing system...");

    // NOTE: Change the database if necessary
    Database db = new Database("mysql", "root", "", "library");
    Connection conn = db.getConnection();

    // new BorrowerList(conn);
    // new BookList(conn);
    new Login(conn);
  }
}
