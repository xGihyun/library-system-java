package admin;

import assets.Colors;
import entities.*;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * BookReturn
 */
public class BookReturn extends JFrame {

  private Connection conn;
  private User user;

  public BookReturn(Connection conn) {
    this.conn = conn;
    this.user = Session.getInstance().getUser();

    // SwingUtilities.invokeLater();
  }

  private void display() {
    setTitle("Book Return");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);
  }
}
