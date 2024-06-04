package entities;

/**
 * Author
 */
public class Author {
  private String id;
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffixName;

  public Author(String id, String firstName, String middleName, String lastName, String suffixName) {
    this.id = id;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.suffixName = suffixName;
  }

  public String getId() {
    return id;
  }

  public String getFullName() {
    return firstName + (middleName != null && !middleName.isEmpty() ? " " + middleName : "") + " " + lastName
        + (suffixName != null && !suffixName.isEmpty() ? " " + suffixName : "");
  }

  @Override
  public String toString() {
    return this.getFullName();
  }
}
