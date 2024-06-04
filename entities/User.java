package entities;

/**
 * User
 */
public class User {
  private String id;
  private String firstName;
  private String middleName;
  private String lastName;
  private String suffixName;
  private String role;
  private String avatarUrl;
  private String email;


  // NOTE: An enum would be better!
  // private Role role;

  // private enum Role {
  //   Admin("admin"),
  //   Student("student"),
  //   Teacher("teacher");
  //
  //   private final String role;
  //
  //   Role(String role) {
  //     this.role = role;
  //   }
  //
  //   public String getRoleName() {
  //     return role;
  //   }
  // };

  public User(String id, String firstName, String middleName, String lastName, String suffixName, String role, String avatarUrl, String email) {
    this.id = id;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.suffixName = suffixName;
    this.role = role;
    this.avatarUrl = avatarUrl;
    this.email = email;
  }

  public String getId() {
    return id;
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

  public String getRole() {
    return role;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public String getEmail() {
    return email;
  }
}
