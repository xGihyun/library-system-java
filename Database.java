import java.sql.*;

public class Database {

  public static void main(String[] args) {
    Connection conn = null;

    try {

      String dbName = "";
      String userName = "";
      String password = "";
      String connectionUrl = "";

      conn = DriverManager.getConnection(connectionUrl);

    } catch (Exception e) {
      // TODO: handle exception
    }

  }
}
