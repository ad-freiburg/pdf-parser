package ad.freiburg.pdfparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A figure in a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfFigure extends PdfElement {
  /**
   * The position of this figure in the PDF document.
   */
  protected PdfPosition position;

  // ==============================================================================================

  /**
   * Returns the position of this figure in the PDF document.
   * 
   * @return The position of this figure in the PDF document.
   */
  public PdfPosition getPosition() {
    return this.position;
  }

  /**
   * Sets the position of this figure in the PDF document.
   * 
   * @param position The position of this figure in the PDF document.
   */
  public void setPosition(PdfPosition position) {
    this.position = position;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfFigure(pos: " + getPosition() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfFigure) {
      PdfFigure otherFigure = (PdfFigure) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPosition(), otherFigure.getPosition());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPosition());
    return builder.hashCode();
  }

}
