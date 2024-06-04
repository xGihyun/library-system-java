package entities;

/**
 * Section
 */
public class Section {
  private String id;
  private String name;
  private String level;
  private String sectionLevelId;

  public Section(String id, String name, String level, String sectionLevelId) {
    this.id = id;
    this.name = name;
    this.level = level;
    this.sectionLevelId = sectionLevelId;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSectionLevelId() {
    return sectionLevelId;
  }

  @Override
  public String toString() {
    return name;
  }

}
