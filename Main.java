import java.sql.Connection;

import admin.BookList;
import admin.BookReturn;
import admin.BorrowerList;
import auth.Login;
import auth.Register;
import auth.RegisterRole;
import auth.RegisterStudent;
import auth.RegisterTeacher;
import database.Database;

public class Main {
  public static void main(String[] args) throws Exception {

    System.out.println("Initializing system...");

    // NOTE: Change the database if necessary
    Database db = new Database("mysql", "root", "", "library");
    Connection conn = db.getConnection();

    // new BookReturn(conn);
    // new BorrowerList(conn);
    // new BookList(conn);
    // new Login(conn);
    new RegisterRole(conn);
    // new RegisterStudent(conn);
  }
}
