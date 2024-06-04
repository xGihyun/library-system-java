package auth;

import assets.Colors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class RegisterRole extends JFrame {
  private Connection conn;

  public RegisterRole(Connection conn) {
    this.conn = conn;

    System.out.println("Register your role");

    SwingUtilities.invokeLater(this::displayRoleOption);
  }

  private void displayRoleOption() {

    System.out.println("Displaying role options");

    setTitle("Register Role");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    JPanel imagePanel = new JPanel();
    imagePanel.setBackground(Colors.BASE);
    add(imagePanel);

    JLabel imageLabel = new JLabel();
    // ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("../assets/images/bocchi.jpg"));
    // imageLabel.setIcon(imageIcon);
    imagePanel.add(imageLabel);

    JPanel roleOptionPanel = new JPanel();
    roleOptionPanel.setBackground(Colors.BASE);
    roleOptionPanel.setLayout(new GridBagLayout());
    add(roleOptionPanel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(20, 0, 20, 0); // gap between buttons
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    Font font = new Font("Arial", Font.BOLD, 24);

    // Create icon for buttons
    ImageIcon teacherIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("../assets/images/bocchi.jpg")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
    ImageIcon studentIcon = new ImageIcon(new ImageIcon(this.getClass().getResource("../assets/images/bocchi.jpg")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));

    JButton teacherButton = new JButton("Teacher", teacherIcon);
    teacherButton.setFont(font);
    teacherButton.setBackground(Colors.TEXT);
    teacherButton.setForeground(Colors.MANTLE);
    teacherButton.setHorizontalTextPosition(SwingConstants.CENTER);
    teacherButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    teacherButton.setPreferredSize(new Dimension(300, 300));
    roleOptionPanel.add(teacherButton, gbc);

    teacherButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new RegisterTeacher(conn);
      }
    });

    gbc.gridy = 1;
    gbc.insets = new Insets(20, 0, 20, 0); // gap between buttons

    JButton studentButton = new JButton("Student", studentIcon);
    studentButton.setFont(font);
    studentButton.setBackground(Colors.TEXT);
    studentButton.setForeground(Colors.MANTLE);
    studentButton.setHorizontalTextPosition(SwingConstants.CENTER);
    studentButton.setVerticalTextPosition(SwingConstants.BOTTOM);
    studentButton.setPreferredSize(new Dimension(300, 300));
    roleOptionPanel.add(studentButton, gbc);

    studentButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new RegisterStudent(conn);
      }
    });

    roleOptionPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

    // Display the frame
    setVisible(true);
  }
}
