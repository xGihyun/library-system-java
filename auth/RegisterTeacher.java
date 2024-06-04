package auth;

import assets.Colors;
import entities.Department;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class RegisterTeacher extends JFrame {
  private Connection conn;

  public RegisterTeacher(Connection conn) {
    this.conn = conn;
    SwingUtilities.invokeLater(this::displayForm);
  }

  private List<Department> fetchDepartments() {
    List<Department> departments = new ArrayList<>();
    String query = "SELECT * FROM departments";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String id = rs.getString("id");
        String name = rs.getString("name");
        departments.add(new Department(id, name));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return departments;
  }

  private void displayForm() {
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

    JLabel employeeIdLabel = new JLabel("Employee ID:");
    employeeIdLabel.setFont(font);
    employeeIdLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 4;
    registrationPanel.add(employeeIdLabel, gbc);

    JTextField employeeIdText = new JTextField(20);
    employeeIdText.setFont(font);
    employeeIdText.setBackground(Colors.MANTLE);
    employeeIdText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 4;
    registrationPanel.add(employeeIdText, gbc);

    JLabel departmentLabel = new JLabel("Department:");
    departmentLabel.setFont(font);
    departmentLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 5;
    registrationPanel.add(departmentLabel, gbc);

    JComboBox<Department> departmentComboBox = new JComboBox<>();
    departmentComboBox.setFont(font);
    departmentComboBox.setPreferredSize(new Dimension(346, 30));

    List<Department> departments = fetchDepartments();
    for (Department department : departments) {
      departmentComboBox.addItem(department);
    }

    gbc.gridx = 1;
    gbc.gridy = 5;
    registrationPanel.add(departmentComboBox, gbc);

    JButton registerButton = new JButton("Register");
    registerButton.setFont(font);
    registerButton.setBackground(Colors.BLUE);
    registerButton.setForeground(Colors.BASE);
    registerButton.setFocusPainted(false);
    registerButton.setBorderPainted(false);
    gbc.gridx = 1;
    gbc.gridy = 6;
    registrationPanel.add(registerButton, gbc);

    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String firstName = firstNameText.getText();
        String lastName = lastNameText.getText();
        String email = emailText.getText();
        String password = new String(passwordText.getPassword());
        String employeeId = employeeIdText.getText();
        Department department = (Department) departmentComboBox.getSelectedItem();

        try {
          String userId = registerUser(conn, firstName, lastName, email, password);
          if (userId != null) {
            boolean result = registerTeacher(conn, userId, employeeId, department);
            if (result) {
              JOptionPane.showMessageDialog(RegisterTeacher.this, "Register successful");
              dispose();
              new Login(conn);
            } else {
              JOptionPane.showMessageDialog(RegisterTeacher.this, "Register failed");
            }
          } else {
            JOptionPane.showMessageDialog(RegisterTeacher.this, "Register failed");
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(RegisterTeacher.this, "Database connection error");
        }
      }
    });

    JButton loginButton = new JButton("<html>Already have an account? <u>Login</u></html>");
    loginButton.setFont(font);
    loginButton.setBackground(Colors.BASE);
    loginButton.setForeground(Colors.GREEN);
    loginButton.setFocusPainted(false);
    loginButton.setBorderPainted(false);
    gbc.gridx = 1;
    gbc.gridy = 8;
    registrationPanel.add(loginButton, gbc);

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new Login(conn);
      }
    });

    JPanel imagePanel = new JPanel();
    imagePanel.setBackground(Colors.OVERLAY1);
    add(imagePanel);

    JLabel imageLabel = new JLabel();
    ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("../assets/images/bocchi.jpg"));
    imageLabel.setIcon(imageIcon);
    imagePanel.add(imageLabel);

    setVisible(true);
  }

  private String registerUser(Connection connection, String firstName, String lastName, String email, String password) {
    String userId = UUID.randomUUID().toString();
    String query = "INSERT INTO users (id, first_name, last_name, email, password, role) VALUES (?, ?, ?, ?, ?, 'teacher')";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, userId);
      stmt.setString(2, firstName);
      stmt.setString(3, lastName);
      stmt.setString(4, email);
      stmt.setString(5, password);
      stmt.executeUpdate();
      return userId;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean registerTeacher(Connection conn, String userId, String employeeId, Department department) {
    String query = "INSERT INTO teachers (id, department_id, user_id) VALUES (?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, employeeId);
      stmt.setString(2, department.getId());
      stmt.setString(3, userId);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
