package auth;

import assets.Colors;
import entities.Section;
import entities.YearLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class RegisterStudent extends JFrame {
  private Connection conn;

  public RegisterStudent(Connection conn) {
    this.conn = conn;
    SwingUtilities.invokeLater(this::displayForm);
  }

  private List<YearLevel> fetchYearLevels() {
    List<YearLevel> yearLevels = new ArrayList<>();
    String query = "SELECT * FROM year_levels";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String id = rs.getString("id");
        String name = rs.getString("name");
        yearLevels.add(new YearLevel(id, name));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return yearLevels;
  }

  private List<Section> fetchSections(String yearLevelId) {
    List<Section> sections = new ArrayList<>();
    String query = "SELECT s.id, s.name, s.level, sl.id AS section_level_id FROM sections s"
        +
        " JOIN section_levels sl ON sl.section_id = s.id"
        +
        " JOIN year_levels yl ON yl.id = sl.year_level_id"
        +
        " WHERE yl.id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, yearLevelId);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String level = rs.getString("level");
        String sectionLevelId = rs.getString("section_level_id");

        sections.add(new Section(id, name, level, sectionLevelId));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return sections;
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
    JLabel firstNameLabel = new JLabel("* First Name:");
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

    JLabel middleNameLabel = new JLabel("Middle Name:");
    middleNameLabel.setFont(font);
    middleNameLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 1;
    registrationPanel.add(middleNameLabel, gbc);

    JTextField middleNameText = new JTextField(20);
    middleNameText.setFont(font);
    middleNameText.setBackground(Colors.MANTLE);
    middleNameText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 1;
    registrationPanel.add(middleNameText, gbc);

    JLabel lastNameLabel = new JLabel("* Last Name:");
    lastNameLabel.setFont(font);
    lastNameLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 2;
    registrationPanel.add(lastNameLabel, gbc);

    JTextField lastNameText = new JTextField(20);
    lastNameText.setFont(font);
    lastNameText.setBackground(Colors.MANTLE);
    lastNameText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 2;
    registrationPanel.add(lastNameText, gbc);

    JLabel suffixNameLabel = new JLabel("Suffix Name:");
    suffixNameLabel.setFont(font);
    suffixNameLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 3;
    registrationPanel.add(suffixNameLabel, gbc);

    JTextField suffixNameText = new JTextField(20);
    suffixNameText.setFont(font);
    suffixNameText.setBackground(Colors.MANTLE);
    suffixNameText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 3;
    registrationPanel.add(suffixNameText, gbc);

    JLabel emailLabel = new JLabel("* Email:");
    emailLabel.setFont(font);
    emailLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 4;
    registrationPanel.add(emailLabel, gbc);

    JTextField emailText = new JTextField(20);
    emailText.setFont(font);
    emailText.setBackground(Colors.MANTLE);
    emailText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 4;
    registrationPanel.add(emailText, gbc);

    JLabel passwordLabel = new JLabel("* Password:");
    passwordLabel.setFont(font);
    passwordLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 5;
    registrationPanel.add(passwordLabel, gbc);

    JPasswordField passwordText = new JPasswordField(20);
    passwordText.setFont(font);
    passwordText.setBackground(Colors.MANTLE);
    passwordText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 5;
    registrationPanel.add(passwordText, gbc);

    JLabel studentIdLabel = new JLabel("* Student ID:");
    studentIdLabel.setFont(font);
    studentIdLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 6;
    registrationPanel.add(studentIdLabel, gbc);

    JTextField studentIdText = new JTextField(20);
    studentIdText.setFont(font);
    studentIdText.setBackground(Colors.MANTLE);
    studentIdText.setForeground(Colors.TEXT);
    gbc.gridx = 1;
    gbc.gridy = 6;
    registrationPanel.add(studentIdText, gbc);

    JLabel yearLevelLabel = new JLabel("* Year Level:");
    yearLevelLabel.setFont(font);
    yearLevelLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 7;
    registrationPanel.add(yearLevelLabel, gbc);

    JComboBox<YearLevel> yearLevelComboBox = new JComboBox<>();
    yearLevelComboBox.setFont(font);
    yearLevelComboBox.setPreferredSize(new Dimension(346, 30));

    List<YearLevel> yearLevels = fetchYearLevels();
    for (YearLevel yearLevel : yearLevels) {
      yearLevelComboBox.addItem(yearLevel);
    }

    gbc.gridx = 1;
    gbc.gridy = 7;
    registrationPanel.add(yearLevelComboBox, gbc);

    JLabel sectionLabel = new JLabel("* Section:");
    sectionLabel.setFont(font);
    sectionLabel.setForeground(Colors.TEXT);
    gbc.gridx = 0;
    gbc.gridy = 8;
    registrationPanel.add(sectionLabel, gbc);

    JComboBox<Section> sectionComboBox = new JComboBox<>();
    sectionComboBox.setFont(font);
    sectionComboBox.setPreferredSize(new Dimension(346, 30));

    List<Section> sections = fetchSections(yearLevels.get(0).getId());
    for (Section section : sections) {
      sectionComboBox.addItem(section);
    }

    yearLevelComboBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        YearLevel selectedYearLevel = (YearLevel) yearLevelComboBox.getSelectedItem();

        sectionComboBox.removeAllItems();

        if (selectedYearLevel != null) {
          List<Section> sections = fetchSections(selectedYearLevel.getId());
          for (Section section : sections) {
            sectionComboBox.addItem(section);
          }
        }
      }

    });

    // List<Section> sections = fetchSections();
    // for (Section section : sections) {
    // sectionComboBox.addItem(section);
    // }

    gbc.gridx = 1;
    gbc.gridy = 8;
    registrationPanel.add(sectionComboBox, gbc);

    JButton registerButton = new JButton("Register");
    registerButton.setFont(font);
    registerButton.setBackground(Colors.BLUE);
    registerButton.setForeground(Colors.BASE);
    registerButton.setFocusPainted(false);
    registerButton.setBorderPainted(false);
    gbc.gridx = 1;
    gbc.gridy = 9;
    registrationPanel.add(registerButton, gbc);

    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String firstName = firstNameText.getText();
        String middleName = middleNameText.getText();
        String lastName = lastNameText.getText();
        String suffixName = suffixNameText.getText();
        String email = emailText.getText();
        String password = new String(passwordText.getPassword());
        String studentId = studentIdText.getText();
        Section section = (Section) sectionComboBox.getSelectedItem();
        // YearLevel yearLevel = (YearLevel) yearLevelComboBox.getSelectedItem();

        try {
          String userId = registerUser(conn, firstName, middleName, lastName, suffixName, email, password);
          if (userId != null) {
            boolean result = registerStudent(conn, studentId, userId, section.getSectionLevelId());
            if (result) {
              JOptionPane.showMessageDialog(RegisterStudent.this, "Register successful");
              dispose();
              new Login(conn);
            } else {
              JOptionPane.showMessageDialog(RegisterStudent.this, "Register failed");
            }
          } else {
            JOptionPane.showMessageDialog(RegisterStudent.this, "Register failed");
          }
        } catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(RegisterStudent.this, "Database connection error");
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
    gbc.gridy = 10;
    registrationPanel.add(loginButton, gbc);

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new Login(conn);
      }
    });

    JPanel imagePanel = new JPanel();
    imagePanel.setBackground(Colors.BASE);
    imagePanel.setLayout(new GridBagLayout());
    add(imagePanel);

    JLabel imageLabel = new JLabel();
    ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("../assets/images/logoForLogin.png"));
    imageLabel.setIcon(imageIcon);
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    imageLabel.setVerticalAlignment(JLabel.CENTER);
    imagePanel.add(imageLabel, new GridBagConstraints());

    setVisible(true);
  }

  private String registerUser(Connection connection, String firstName,String middleName, String lastName,String suffixName, String email, String password) {
    String userId = UUID.randomUUID().toString();
    String query = "INSERT INTO users (id, first_name, middle_name, last_name, suffix_name, email, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, 'student')";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, userId);
      stmt.setString(2, firstName);
      stmt.setString(3, middleName);
      stmt.setString(4, lastName);
      stmt.setString(5, suffixName);
      stmt.setString(6, email);
      stmt.setString(7, password);
      stmt.executeUpdate();

      return userId;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean registerStudent(Connection conn, String studentId, String userId, String sectionLevelId) {
    String query = "INSERT INTO students (id, section_level_id, user_id) VALUES (?, ?, ?)";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, studentId);
      stmt.setString(2, sectionLevelId);
      stmt.setString(3, userId);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
