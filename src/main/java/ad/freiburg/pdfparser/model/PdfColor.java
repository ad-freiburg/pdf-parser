package ad.freiburg.pdfparser.model;

import java.util.Arrays;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A color in a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfColor extends PdfResource {
  /**
   * The name of this color.
   */
  protected String name;

  /**
   * The RGB values of this color, each value in the range [0, 255].
   */
  protected int[] rgb;

  // ==============================================================================================

  /**
   * Creates a new color.
   */
  public PdfColor() {
    this.rgb = new int[3];
  }

  /**
   * Creates a new color from the given RGB values.
   * 
   * @param rgb The RGB values given as an array of three int values, each in the range [0, 255].
   */
  public PdfColor(int[] rgb) {
    this.rgb = rgb;
  }

  /**
   * Creates a new color from the given RGB values.
   * 
   * @param r The R value, given as an int in range [0, 255].
   * @param g The G value, given as an int in range [0, 255].
   * @param b The B value, given as an int in range [0, 255].
   */
  public PdfColor(int r, int g, int b) {
    this();
    this.rgb[0] = r;
    this.rgb[1] = g;
    this.rgb[2] = b;
  }

  // ==============================================================================================

  /**
   * Returns the name of this color.
   * 
   * @return The name of this color.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of this color.
   * 
   * @param name The name of this color.
   */
  public void setName(String name) {
    this.name = name;
  }

  // ==============================================================================================

  /**
   * Returns the RGB values of this color.
   * 
   * @return The RGB values as an array of three int values, each in the range [0, 255].
   */
  public int[] getRGB() {
    return this.rgb;
  }

  /**
   * Sets the RGB values of this color.
   * 
   * @param rgb The RGB values given as an array of three int values, each in the range [0, 255].
   */
  public void setRGB(final int[] rgb) {
    this.rgb = rgb;
  }

  /**
   * Sets the RGB values of this color.
   * 
   * @param r The R value, given as an int in range [0, 255].
   * @param g The G value, given as an int in range [0, 255].
   * @param b The B value, given as an int in range [0, 255].
   */
  public void setRGB(int r, int g, int b) {
    this.rgb[0] = r;
    this.rgb[1] = g;
    this.rgb[2] = b;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfColor(" + Arrays.toString(this.rgb) + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfColor) {
      PdfColor otherColor = (PdfColor) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getName(), otherColor.getName());
      builder.append(getRGB(), otherColor.getRGB());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getName());
    builder.append(getRGB());
    return builder.hashCode();
  }
}
