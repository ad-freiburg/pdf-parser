package ad.freiburg.pdfparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A position in a page of a PDF document, that is: a pair of a page and a bounding box (represented
 * by a rectangle).
 * 
 * @author Claudius Korzen
 */
public class PdfPosition {
  /**
   * The page.
   */
  protected PdfPage page;

  /**
   * The rectangle.
   */
  protected Rectangle rectangle;

  /**
   * Creates a new position.
   * 
   * @param page      The page of the position.
   * @param rectangle The rectangle of the position.
   */
  public PdfPosition(PdfPage page, Rectangle rectangle) {
    this.page = page;
    this.rectangle = rectangle;
  }

  /**
   * Creates a new position.
   * 
   * @param page The page.
   * @param minX The x coordinate of the lower left corner of the position.
   * @param minY The y coordinate of the lower left corner of the position.
   * @param maxX The x coordinate of the upper right corner of the position.
   * @param maxY The y coordinate of the upper right corner of the position.
   * 
   */
  public PdfPosition(PdfPage page, float minX, float minY, float maxX, float maxY) {
    this.page = page;
    this.rectangle = new Rectangle(minX, minY, maxX, maxY);
  }

  /**
   * Creates a new position.
   * 
   * @param page   The page.
   * @param point1 The lower left corner of the position.
   * @param point2 The upper right corner of the position.
   */
  public PdfPosition(PdfPage page, Point point1, Point point2) {
    this.page = page;
    this.rectangle = new Rectangle(point1, point2);
  }

  // ==============================================================================================

  /**
   * Returns the bounding box of this position (represented by a rectangle).
   * 
   * @return The bounding box of this position (represented by a rectangle).
   */
  public Rectangle getRectangle() {
    return this.rectangle;
  }

  /**
   * Sets the bounding box of this position (represented by a rectangle).
   * 
   * @param rectangle The bounding box of this position (represented by a rectangle).
   */
  public void setRectangle(Rectangle rectangle) {
    this.rectangle = rectangle;
  }

  // ==============================================================================================

  /**
   * Returns the page of this position.
   * 
   * @return The page of this position.
   */
  public PdfPage getPage() {
    return this.page;
  }

  /**
   * Returns the page number of the page of this position.
   * 
   * @return The page number of the page of this position, or 0 if there is no page given.
   */
  public int getPageNumber() {
    return getPage() != null ? getPage().getPageNumber() : 0;
  }

  /**
   * Sets the page of this position.
   * 
   * @param page The page of this position.
   */
  public void setPage(PdfPage page) {
    this.page = page;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfPosition(page: " + getPage() + ", rect: " + getRectangle() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfPosition) {
      PdfPosition otherPosition = (PdfPosition) other;

      EqualsBuilder build = new EqualsBuilder();
      build.append(getRectangle(), otherPosition.getRectangle());
      // Using getPage() here results in an infinite loop.
      build.append(getPageNumber(), otherPosition.getPageNumber());

      return build.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getRectangle());
    // Using getPage() here results in an infinite loop.
    builder.append(getPageNumber());
    return builder.hashCode();
  }
}
