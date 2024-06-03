package admin;

import assets.Colors;

import javax.swing.*;
import java.awt.*;

public class BookList extends JFrame {
  public BookList() {
    setTitle("Book List");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // setLayout(new BorderLayout());

    getContentPane().setBackground(Colors.BASE);

    JLabel welcomeLabel = new JLabel("Welcome to the Book List!");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
    welcomeLabel.setForeground(Colors.TEXT);

    add(welcomeLabel, BorderLayout.CENTER);

    setVisible(true);
  }
}
