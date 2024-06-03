package auth;

import assets.Colors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register extends JFrame {
  private Connection conn;

  public Register(Connection conn) {
    this.conn = conn;

    createAndShowRegisterForm();
  }

  public void createAndShowRegisterForm() {
    SwingUtilities.invokeLater(() -> {
      setTitle("Register Form");
      setSize(1280, 768);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setLayout(new GridLayout(1, 2));
      setLocationRelativeTo(null);

      JPanel registrationPanel = new JPanel();
      registrationPanel.setBackground(Colors.BASE);
      registrationPanel.setLayout(new GridBagLayout());
      add(registrationPanel);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);

      Font font = new Font("Arial", Font.PLAIN, 18);

      // Create labels and text fields for user information
      JLabel firstNameLabel = new JLabel("First Name:");
      firstNameLabel.setFont(font);
      firstNameLabel.setForeground(Colors.TEXT);
      gbc.gridx = 0;
      gbc.gridy = 0;
      registrationPanel.add(firstNameLabel, gbc);

      JTextField firstNameText = new JTextField(20);
      firstNameText.setFont(font);
      firstNameText.setBackground(Colors.MANTLE);
      firstNameText.setForeground(Colors.TEXT);
      gbc.gridx = 1;
      gbc.gridy = 0;
      registrationPanel.add(firstNameText, gbc);

      JLabel lastNameLabel = new JLabel("Last Name:");
      lastNameLabel.setFont(font);
      lastNameLabel.setForeground(Colors.TEXT);
      gbc.gridx = 0;
      gbc.gridy = 1;
      registrationPanel.add(lastNameLabel, gbc);

      JTextField lastNameText = new JTextField(20);
      lastNameText.setFont(font);
      lastNameText.setBackground(Colors.MANTLE);
      lastNameText.setForeground(Colors.TEXT);
      gbc.gridx = 1;
      gbc.gridy = 1;
      registrationPanel.add(lastNameText, gbc);

      JLabel emailLabel = new JLabel("Email:");
      emailLabel.setFont(font);
      emailLabel.setForeground(Colors.TEXT);
      gbc.gridx = 0;
      gbc.gridy = 2;
      registrationPanel.add(emailLabel, gbc);

      JTextField emailText = new JTextField(20);
      emailText.setFont(font);
      emailText.setBackground(Colors.MANTLE);
      emailText.setForeground(Colors.TEXT);
      gbc.gridx = 1;
      gbc.gridy = 2;
      registrationPanel.add(emailText, gbc);

      JLabel passwordLabel = new JLabel("Password:");
      passwordLabel.setFont(font);
      passwordLabel.setForeground(Colors.TEXT);
      gbc.gridx = 0;
      gbc.gridy = 3;
      registrationPanel.add(passwordLabel, gbc);

      JPasswordField passwordText = new JPasswordField(20);
      passwordText.setFont(font);
      passwordText.setBackground(Colors.MANTLE);
      passwordText.setForeground(Colors.TEXT);
      gbc.gridx = 1;
      gbc.gridy = 3;
      registrationPanel.add(passwordText, gbc);

      JButton registerButton = new JButton("Register");
      registerButton.setFont(font);
      registerButton.setBackground(Colors.BLUE);
      registerButton.setForeground(Colors.BASE);
      registerButton.setFocusPainted(false);
      registerButton.setBorderPainted(false);
      gbc.gridx = 1;
      gbc.gridy = 4;
      registrationPanel.add(registerButton, gbc);

      // Action listener for the register button
      registerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String firstName = firstNameText.getText();
          String lastName = lastNameText.getText();
          String email = emailText.getText();
          String password = new String(passwordText.getPassword());

          try {
            boolean isRegistered = registerUser(conn, firstName, lastName, email, password);

            if (isRegistered) {
              JOptionPane.showMessageDialog(Register.this, "Register successful");

              dispose();

              new Login(conn);
            } else {
              JOptionPane.showMessageDialog(Register.this, "Register failed");
            }
          } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(Register.this, "Database connection error");
          }
        }
      });

      // Right panel for the image
      JPanel imagePanel = new JPanel();
      imagePanel.setBackground(Colors.OVERLAY1);
      add(imagePanel);

      // Load an image (replace with an actual image path)
      JLabel imageLabel = new JLabel();
      ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("../assets/images/bocchi.jpg"));
      imageLabel.setIcon(imageIcon);
      imagePanel.add(imageLabel);

      // Display the frame
      setVisible(true);
    });

  }

  private boolean registerUser(Connection connection, String firstName, String lastName, String email,
      String password) {
    String query = "INSERT INTO users (first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, 'student')";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setString(1, firstName);
      preparedStatement.setString(2, lastName);
      preparedStatement.setString(3, email);
      preparedStatement.setString(4, password);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
