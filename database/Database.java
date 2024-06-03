package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
  private Connection conn;
  private String username;
  private String password;
  private String dbName;
  private String url;

  public Database(String db, String username, String password, String dbName) throws Exception {
    try {
      // Load the JDBC driver
      Class.forName("org.mariadb.jdbc.Driver");

      this.username = username;
      this.password = password;
      this.dbName = dbName;
      this.url = "jdbc:" + db + "://localhost/" + dbName;
      this.conn = DriverManager.getConnection(url, username, password);

      System.out.println("Database connected.");
    } catch (SQLException e) {
      throw new SQLException("Failed to connect to the database.", e);
    }
  }

  public Connection getConnection() {
    return this.conn;
  }
}
