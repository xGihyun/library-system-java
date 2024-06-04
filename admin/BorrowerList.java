package admin;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

import assets.Colors;
import entities.Student;
import entities.Teacher;
import views.Sidebar;

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

    add(new Sidebar(conn, this), BorderLayout.WEST);

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

    List<Teacher> teachers = fetchTeachers();
    displayTeachers(teacherPanel, teachers);

    add(tabbedPane, BorderLayout.CENTER);

    setVisible(true);
  }

  private List<Student> fetchStudents() {
    List<Student> students = new ArrayList<>();

    String query = "SELECT stu.id AS student_id, stu.section_level_id, stu.user_id, "
        + " u.first_name, u.middle_name, u.last_name, u.suffix_name, "
        + " sec.name AS section_name, yl.name AS year_level_name, COUNT(bb.id) AS book_borrow_count"
        + " FROM students stu"
        + " JOIN users u ON u.id = stu.user_id"
        + " JOIN section_levels seclvl ON seclvl.id = stu.section_level_id"
        + " JOIN sections sec ON sec.id = seclvl.section_id"
        + " JOIN year_levels yl ON yl.id = seclvl.year_level_id"
        + " JOIN book_borrows bb ON bb.user_id = stu.user_id"
        + " WHERE bb.returned_at IS NULL"
        + " GROUP BY stu.id";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String studentId = rs.getString("student_id");
        String sectionLevelId = rs.getString("section_level_id");
        String userId = rs.getString("user_id");
        String firstName = rs.getString("first_name");
        String middleName = rs.getString("middle_name");
        String lastName = rs.getString("last_name");
        String suffixName = rs.getString("suffix_name");
        String sectionName = rs.getString("section_name");
        String yearLevelName = rs.getString("year_level_name");
        int bookBorrowCount = rs.getInt("book_borrow_count");

        students.add(new Student(studentId, sectionLevelId, userId, firstName, middleName, lastName, suffixName,
            sectionName, yearLevelName));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return students;
  }

  private List<Teacher> fetchTeachers() {
    List<Teacher> teachers = new ArrayList<>();

    String query = "SELECT t.id AS teacher_id, t.user_id, d.name AS department_name,"
        + " u.first_name, u.middle_name, u.last_name, u.suffix_name, "
        + " COUNT(bb.id) AS book_borrow_count"
        + " FROM teachers t"
        + " JOIN users u ON u.id = t.user_id"
        + " JOIN departments d ON d.id = t.department_id"
        + " JOIN book_borrows bb ON bb.user_id = t.user_id"
        + " WHERE bb.returned_at IS NULL"
        + " GROUP BY t.id";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String employeeId = rs.getString("teacher_id");
        String userId = rs.getString("user_id");
        String firstName = rs.getString("first_name");
        String middleName = rs.getString("middle_name");
        String lastName = rs.getString("last_name");
        String suffixName = rs.getString("suffix_name");
        String departmentName = rs.getString("department_name");
        int bookBorrowCount = rs.getInt("book_borrow_count");

        teachers.add(new Teacher(userId, employeeId, firstName, middleName, lastName, suffixName, departmentName));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return teachers;
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

  private void displayTeachers(JPanel panel, List<Teacher> teachers) {
    String[] columnNames = { "Name", "Employee ID", "Department" };
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    for (Teacher teacher : teachers) {

      Object[] row = new Object[] { teacher.getFullName(), teacher.getEmployeeId(), teacher.getDepartmentName() };

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
    table.setOpaque(true);
    table.setFillsViewportHeight(true);
    table.setBackground(Colors.MANTLE);
    table.setForeground(Colors.TEXT);
    table.setSelectionBackground(Colors.OVERLAY1);
    table.setSelectionForeground(Colors.TEXT);
  }
}
