package ad.freiburg.pdfparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A shape (e.g., a line or a curve) in a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfShape extends PdfElement {
  /**
   * The position of this shape.
   */
  protected PdfPosition position;

  /**
   * The color of this shape.
   */
  protected PdfColor color;

  // ==============================================================================================

  /**
   * Returns the position of this shape in the PDF document.
   * 
   * @return The position of this shape in the PDF document.
   */
  public PdfPosition getPosition() {
    return this.position;
  }

  /**
   * Sets the position of this shape in the PDF document.
   * 
   * @param position The position of this shape in the PDF document.
   */
  public void setPosition(PdfPosition position) {
    this.position = position;
  }

  // ==============================================================================================

  /**
   * Returns the color of this shape.
   * 
   * @return The color of this shape.
   */
  public PdfColor getColor() {
    return this.color;
  }

  /**
   * Sets the color of this shape.
   * 
   * @param color The color of this shape.
   */
  public void setColor(PdfColor color) {
    this.color = color;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfShape(pos: " + getPosition() + ", color: " + getColor() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfShape) {
      PdfShape otherShape = (PdfShape) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherShape.getPosition());
      builder.append(getColor(), otherShape.getColor());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    builder.append(getColor());
    return builder.hashCode();
  }
}
