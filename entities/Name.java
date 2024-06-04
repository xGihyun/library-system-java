package entities;

/**
 * Name
 */
public class Name {
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffixName;
  
  public Name(String firstName, String middleName, String lastName, String suffixName) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.suffixName = suffixName;
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
