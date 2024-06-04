package admin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import assets.Colors;
import entities.Student;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BorrowerList extends JFrame {
  private Connection conn;

  public BorrowerList(Connection conn) {
    this.conn = conn;

    SwingUtilities.invokeLater(() -> {
      display();
    });
  }

  private void display() {
    setTitle("Borrower List");
    setSize(1280, 768);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    getContentPane().setBackground(Colors.BASE);
    getContentPane().setForeground(Colors.TEXT);

    JTabbedPane tabbedPane = new JTabbedPane();

    JPanel studentPanel = new JPanel(new BorderLayout());
    studentPanel.setBackground(Colors.BASE);

    JPanel teacherPanel = new JPanel(new BorderLayout());
    teacherPanel.setBackground(Colors.BASE);

    tabbedPane.addTab("Student", studentPanel);
    tabbedPane.addTab("Teacher", teacherPanel);

    // Change background color of the selected tab
    tabbedPane.addChangeListener(e -> {
      for (int i = 0; i < tabbedPane.getTabCount(); i++) {
        if (i == tabbedPane.getSelectedIndex()) {
          tabbedPane.setBackgroundAt(i, Colors.GREEN);
        } else {
          tabbedPane.setBackgroundAt(i, null);
        }
      }
    });

    tabbedPane.setBackgroundAt(0, Colors.BASE);

    // Fetch and display students
    List<Student> students = fetchStudents();
    displayStudents(studentPanel, students);

    // TODO: Fetch and display teachers (similar approach can be used for teachers)

    add(tabbedPane, BorderLayout.CENTER);

    setVisible(true);
  }

  private List<Student> fetchStudents() {
    List<Student> students = new ArrayList<>();

    String query = "SELECT stu.id AS student_id, stu.section_level_id, stu.user_id, "
        + " u.first_name, u.middle_name, u.last_name, u.suffix_name, "
        + " sec.name AS section_name, yl.name AS year_level_name"
        + " FROM students stu"
        + " JOIN users u ON u.id = stu.user_id"
        + " JOIN section_levels seclvl ON seclvl.id = stu.section_level_id"
        + " JOIN sections sec ON sec.id = seclvl.section_id"
        + " JOIN year_levels yl ON yl.id = seclvl.year_level_id";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      ResultSet resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        String studentId = resultSet.getString("student_id");
        String sectionLevelId = resultSet.getString("section_level_id");
        String userId = resultSet.getString("user_id");
        String firstName = resultSet.getString("first_name");
        String middleName = resultSet.getString("middle_name");
        String lastName = resultSet.getString("last_name");
        String suffixName = resultSet.getString("suffix_name");
        String sectionName = resultSet.getString("section_name");
        String yearLevelName = resultSet.getString("year_level_name");

        students.add(new Student(studentId, sectionLevelId, userId, firstName, middleName, lastName, suffixName,
            sectionName, yearLevelName));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return students;
  }

  private void displayStudents(JPanel panel, List<Student> students) {
    String[] columnNames = { "Name", "Student ID", "Year Level", "Section" };
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    for (Student student : students) {

      Object[] row = new Object[] { student.getFullName(), student.getStudentId(), student.getYearLevelName(),
          student.getSectionName() };

      model.addRow(row);
    }

    JTable table = new JTable(model);
    styleTable(table);

    JScrollPane scrollPane = new JScrollPane(table);
    panel.add(scrollPane, BorderLayout.CENTER);
  }

  private void styleTable(JTable table) {
    table.setFont(new Font("Arial", Font.PLAIN, 18));
    table.setRowHeight(30);
    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
    table.getTableHeader().setBackground(Colors.BLUE);
    table.getTableHeader().setForeground(Colors.BASE);
    table.setBackground(Colors.MANTLE);
    table.setForeground(Colors.TEXT);
    table.setSelectionBackground(Colors.OVERLAY1);
    table.setSelectionForeground(Colors.TEXT);
  }
}
