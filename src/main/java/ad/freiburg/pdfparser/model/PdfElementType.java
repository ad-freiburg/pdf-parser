package ad.freiburg.pdfparser.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An enumeration of all available element types in a PDF document.
 *
 * @author Claudius Korzen
 */
public enum PdfElementType {
  /**
   * The type "characters".
   */
  CHARACTERS("characters"),

  /**
   * The type "figures".
   */
  FIGURES("figures"),

  /**
   * The type "shapes".
   */
  SHAPES("shapes");

  // ==============================================================================================

  /**
   * The name of this type.
   */
  protected String name;

  /**
   * The types per name.
   */
  protected static Map<String, PdfElementType> index;

  static {
    index = new HashMap<>();

    // Fill the map of types per name.
    for (PdfElementType type : values()) {
      index.put(type.getName(), type);
    }
  }

  /**
   * Creates a new type.
   * 
   * @param name The name of the type.
   */
  private PdfElementType(String name) {
    this.name = name;
  }

  // ==============================================================================================

  /**
   * Returns the name of this type.
   * 
   * @return The name of this type.
   */
  public String getName() {
    return this.name;
  }

  // ==============================================================================================

  /**
   * Returns a set of the names of all types.
   * 
   * @return A set of the names of all types.
   */
  public static Set<String> getNames() {
    return index.keySet();
  }

  /**
   * Returns a collection of all available types.
   * 
   * @return A collection of all available types.
   */
  public static Collection<PdfElementType> getTypes() {
    return index.values();
  }

  /**
   * Checks if the given name is a name of an existing type.
   * 
   * @param name The name to check.
   * 
   * @return True, if the given name is a name of an existing type.
   */
  public static boolean isValidType(String name) {
    return index.containsKey(name.toLowerCase());
  }

  /**
   * Returns a set of the types that are associated with the given names.
   * 
   * @param names The names of the types to fetch.
   *
   * @return The set of the fetched types.
   */
  public static Set<PdfElementType> fromStrings(String... names) {
    return fromStrings(Arrays.asList(names));
  }

  /**
   * Returns a set of the types that are associated with the given names.
   * 
   * @param names The names of the types to fetch.
   *
   * @return A set of the fetched types.
   */
  public static Set<PdfElementType> fromStrings(List<String> names) {
    if (names == null || names.isEmpty()) {
      return null;
    }

    Set<PdfElementType> types = new HashSet<>();
    for (String name : names) {
      PdfElementType type = fromString(name);
      if (type != null) {
        types.add(type);
      }
    }
    return types;
  }

  /**
   * Returns the type that is associated with the given name.
   * 
   * @param name The name of the type to fetch.
   *
   * @return The type that is associated with the given name.
   */
  public static PdfElementType fromString(String name) {
    if (!isValidType(name)) {
      throw new IllegalArgumentException(name + " isn't a valid type.");
    }
    return index.get(name.toLowerCase());
  }
}
