package examples;
package database;

import java.sql.*;
import database.Database;

public class Select {

  public static void main(String[] args) {
    Database db = new Database("root", "", "enrollment");
    Statement stmt = null;
    ResultSet rs = null;

    System.out.println("Connecting to database...");

    try {
      // Load the JDBC driver
      Class.forName("org.mariadb.jdbc.Driver");

      String dbName = "enrollment"; // Change this
      String username = "root";
      String password = "";
      String url = "jdbc:mariadb://localhost/" + dbName;

      // Connect to the database
      conn = DriverManager.getConnection(url, username, password);

      // Create a statement object
      stmt = conn.createStatement();

      // Define the select * query
      String sql = "SELECT * FROM users";

      // Execute the query and get the results
      rs = stmt.executeQuery(sql);

      // Process the results (assuming you have multiple columns)
      while (rs.next()) {
        int id = rs.getInt("id"); // Get the value of the "id" column

        System.out.println("ID: " + id);
      }

      System.out.println("Connected to database.");
    } catch (Exception e) {
      throw new IllegalStateException("Cannot connect the database!", e);
    }

  }

}
