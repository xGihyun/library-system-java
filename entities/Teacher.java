package entities;

/**
 * Teacher
 */
public class Teacher {
  private String userId;
  private String employeeId;
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffixName;
  private String departmentName;

  public Teacher(String userId, String employeeId, String firstName, String middleName, String lastName,
      String suffixName, String departmentName) {
    this.userId = userId;
    this.employeeId = employeeId;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.suffixName = suffixName;
    this.departmentName = departmentName;
  }

  public String getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getSuffixName() {
    return suffixName;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public String getFullName() {
    return this.getFirstName() + " "
        + (this.getMiddleName() != null ? this.getMiddleName() + " " : "")
        + this.getLastName() + (this.getSuffixName() != null ? " " + this.getSuffixName() : "");
  }
}
