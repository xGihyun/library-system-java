package auth;

import assets.Colors;
import entities.Session;
import entities.User;
import admin.BookList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
  private Connection conn;

  public Login(Connection conn) {
    this.conn = conn;

    createAndShowLoginForm();
  }

  public void createAndShowLoginForm() {
    SwingUtilities.invokeLater(() -> {
      setTitle("Login Form");
      setSize(1280, 768);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setLayout(new GridLayout(1, 2));
      setLocationRelativeTo(null);

      // Left panel for the login form
      JPanel loginPanel = new JPanel();
      loginPanel.setBackground(Colors.BASE);
      loginPanel.setLayout(new GridBagLayout());
      add(loginPanel);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);

      Font font = new Font("Arial", Font.PLAIN, 18);

      JLabel emailLabel = new JLabel("Email:");
      emailLabel.setFont(font);
      emailLabel.setForeground(Colors.TEXT);
      gbc.gridx = 0;
      gbc.gridy = 0;
      loginPanel.add(emailLabel, gbc);

      JTextField emailText = new JTextField(20);
      emailText.setFont(font);
      // emailText.setBackground(Colors.MANTLE);
      // emailText.setForeground(Colors.TEXT);
      gbc.gridx = 1;
      gbc.gridy = 0;
      loginPanel.add(emailText, gbc);

      JLabel passwordLabel = new JLabel("Password:");
      passwordLabel.setFont(font);
      passwordLabel.setForeground(Colors.TEXT);
      gbc.gridx = 0;
      gbc.gridy = 1;
      loginPanel.add(passwordLabel, gbc);

      JPasswordField passwordText = new JPasswordField(20);
      passwordText.setFont(font);
      // passwordText.setBackground(Colors.MANTLE);
      // passwordText.setForeground(Colors.TEXT);
      gbc.gridx = 1;
      gbc.gridy = 1;
      loginPanel.add(passwordText, gbc);

      JButton loginButton = new JButton("Login");
      loginButton.setFont(font);
      loginButton.setBackground(Colors.BLUE);
      loginButton.setForeground(Colors.BASE);
      loginButton.setFocusPainted(false);
      loginButton.setBorderPainted(false);
      gbc.gridx = 1;
      gbc.gridy = 2;
      loginPanel.add(loginButton, gbc);

      loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String email = emailText.getText();
          String password = new String(passwordText.getPassword());

          try {
            User user = authenticateUser(conn, email, password);

            if (user != null) {
              JOptionPane.showMessageDialog(Login.this, "Login successful");

              dispose();

              Session.getInstance().setUser(user);

              new BookList(conn);
            } else {
              JOptionPane.showMessageDialog(Login.this, "Invalid email or password");
            }
          } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(Login.this, "Database connection error");
          }
        }
      });

      // Add this after the login button creation in the Login class

      // Create the register button with modern look
      JButton registerButton = new JButton("Register");
      registerButton.setFont(font);
      registerButton.setBackground(Colors.GREEN);
      registerButton.setForeground(Colors.BASE);
      registerButton.setFocusPainted(false);
      registerButton.setBorderPainted(false);
      gbc.gridx = 1;
      gbc.gridy = 3;
      loginPanel.add(registerButton, gbc);

      // Action listener for the register button
      registerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          dispose();
          new Register(conn);
        }
      });

      // Right panel for the image
      JPanel imagePanel = new JPanel();
      imagePanel.setBackground(Colors.OVERLAY1);
      add(imagePanel);

      JLabel imageLabel = new JLabel();
      ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("../assets/images/bocchi.jpg"));
      imageLabel.setIcon(imageIcon);
      imagePanel.add(imageLabel);

      setVisible(true);
    });
  }

  private User authenticateUser(Connection connection, String email, String password) {
    String query = "SELECT * FROM users WHERE email = ? AND password = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, email);
      stmt.setString(2, password);

      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        String id = rs.getString("id");
        String firstName = rs.getString("first_name");
        String middleName = rs.getString("middle_name");
        String lastName = rs.getString("last_name");
        String suffixName = rs.getString("suffix_name");
        String role = rs.getString("role");
        String avatarUrl = rs.getString("avatar_url");
        String userEmail = rs.getString("email");

        User user = new User(id, firstName, middleName, lastName, suffixName, role, avatarUrl, userEmail);

        return user;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}
