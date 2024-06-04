package entities;

/**
 * Student
 */
public class Student {
  private String studentId;
  private String sectionLevelId;
  private String userId;
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffixName;
  private String sectionName;
  private String yearLevelName;

  public Student(String studentId, String sectionLevelId, String userId, String firstName, String middleName,
      String lastName, String suffixName, String sectionName, String yearLevelName) {
    this.studentId = studentId;
    this.sectionLevelId = sectionLevelId;
    this.userId = userId;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.suffixName = suffixName;
    this.sectionName = sectionName;
    this.yearLevelName = yearLevelName;
  }

  public String getStudentId() {
    return studentId;
  }

  public String getSectionLevelId() {
    return sectionLevelId;
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

  public String getSectionName() {
    return sectionName;
  }

  public String getYearLevelName() {
    return yearLevelName;
  }

  public String getFullName() {
    return this.getFirstName() + " "
        + (this.getMiddleName() != null ? this.getMiddleName() + " " : "")
        + this.getLastName() + (this.getSuffixName() != null ? " " + this.getSuffixName() : "");
  }

}
