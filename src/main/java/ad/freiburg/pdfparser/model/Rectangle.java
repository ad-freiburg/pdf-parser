package ad.freiburg.pdfparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A simple rectangle in a two-dimensional space.
 * 
 * @author Claudius Korzen.
 */
public class Rectangle {
  /**
   * The x-coordinate of the lower left corner.
   */
  protected float minX = Float.MAX_VALUE;

  /**
   * The y-coordinate of the lower left corner.
   */
  protected float minY = Float.MAX_VALUE;

  /**
   * The x-coordinate of the upper right corner.
   */
  protected float maxX = -Float.MAX_VALUE;

  /**
   * The y-coordinate of the upper right corner.
   */
  protected float maxY = -Float.MAX_VALUE;

  /**
   * Creates a new rectangle with the lower left corner (0,0) and the upper right corner (0,0).
   */
  public Rectangle() {
    this(0, 0, 0, 0);
  }

  /**
   * Creates a copy of the given rectangle.
   *
   * @param rect The rectangle to copy.
   */
  public Rectangle(Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a copy of the given rectangle.
   * 
   * @param rect The rectangle to copy.
   */
  public Rectangle(java.awt.Rectangle rect) {
    this(rect.getMinX(), rect.getMinY(), rect.getMaxX(), rect.getMaxY());
  }

  /**
   * Creates a new rectangle spanned by the given lower left and upper right corner.
   *
   * @param ll The lower left corner.
   * @param ur The upper right corner.
   */
  public Rectangle(Point ll, Point ur) {
    this(ll.getX(), ll.getY(), ur.getX(), ur.getY());
  }

  /**
   * Creates a new rectangle with the lower left (minX, minY) and the upper right (maxX, maxY).
   * 
   * @param minX The x-coordinate of the lower left corner.
   * @param minY The y-coordinate of the lower left corner.
   * @param maxX The x-coordinate of the upper right corner.
   * @param maxY The y-coordinate of the upper right corner.
   */
  public Rectangle(float minX, float minY, float maxX, float maxY) {
    setMinX(minX);
    setMinY(minY);
    setMaxX(maxX);
    setMaxY(maxY);
  }

  /**
   * Creates a new rectangle with the lower left (minX, minY) and the upper right (maxX, maxY).
   * 
   * @param minX The x-coordinate of the lower left corner.
   * @param minY The y-coordinate of the lower left corner.
   * @param maxX The x-coordinate of the upper right corner.
   * @param maxY The y-coordinate of the upper right corner.
   */
  public Rectangle(double minX, double minY, double maxX, double maxY) {
    this((float) minX, (float) minY, (float) maxX, (float) maxY);
  }

  /**
   * Creates a new rectangle from the union of the given rectangles.
   * 
   * @param rectangles The rectangles to process.
   * 
   * @return A new instance of {@link Rectangle} representing the bounding box around the union of
   *         the given rectangles.
   */
  public static Rectangle fromUnion(Rectangle... rectangles) {
    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;

    for (Rectangle rect : rectangles) {
      if (rect.getMinX() < minX) {
        minX = rect.getMinX();
      }

      if (rect.getMinY() < minY) {
        minY = rect.getMinY();
      }

      if (rect.getMaxX() > maxX) {
        maxX = rect.getMaxX();
      }

      if (rect.getMaxY() > maxY) {
        maxY = rect.getMaxY();
      }
    }

    return new Rectangle(minX, minY, maxX, maxY);
  }

  // ==============================================================================================

  /**
   * Returns the x-coordinate of the lower left corner.
   * 
   * @return The x-coordinate of the lower left corner.
   */
  public float getMinX() {
    return this.minX;
  }

  /**
   * Sets the x-coordinate of the lower left corner.
   * 
   * @param minX The x-coordinate of the lower left corner.
   */
  public void setMinX(float minX) {
    this.minX = minX;
  }

  // ==============================================================================================

  /**
   * Returns the y-coordinate of the lower left corner.
   * 
   * @return The y-coordinate of the lower left corner.
   */
  public float getMinY() {
    return this.minY;
  }

  /**
   * Sets the y-coordinate of the lower left corner.
   * 
   * @param minY The y-coordinate of the lower left corner.
   */
  public void setMinY(float minY) {
    this.minY = minY;
  }

  // ==============================================================================================

  /**
   * Returns the x-coordinate of the upper right corner.
   * 
   * @return The x-coordinate of the upper right corner.
   */
  public float getMaxX() {
    return this.maxX;
  }

  /**
   * Sets the x-coordinate of the upper right corner.
   * 
   * @param maxX The x-coordinate of the upper right corner.
   */
  public void setMaxX(float maxX) {
    this.maxX = maxX;
  }

  // ==============================================================================================

  /**
   * Returns the y-coordinate of the upper right corner.
   * 
   * @return The y-coordinate of the upper right corner.
   */
  public float getMaxY() {
    return this.maxY;
  }

  /**
   * Sets the y-coordinate of the upper right corner.
   * 
   * @param maxY The y-coordinate of the upper right corner.
   */
  public void setMaxY(float maxY) {
    this.maxY = maxY;
  }

  // ==============================================================================================

  /**
   * Returns the lower left corner of this rectangle.
   * 
   * @return The lower left corner of this rectangle.
   */
  public Point getLowerLeft() {
    return new Point(this.minX, this.minY);
  }

  /**
   * Returns the lower right corner of this rectangle.
   * 
   * @return The lower right corner of this rectangle.
   */
  public Point getLowerRight() {
    return new Point(this.maxX, this.minY);
  }

  /**
   * Returns the upper left corner of this rectangle.
   * 
   * @return The upper left corner of this rectangle.
   */
  public Point getUpperLeft() {
    return new Point(this.minX, this.maxY);
  }

  /**
   * Returns the upper right corner of this rectangle.
   * 
   * @return The upper right corner of this rectangle.
   */
  public Point getUpperRight() {
    return new Point(this.maxX, this.maxY);
  }

  // ==============================================================================================

  /**
   * Returns the midpoint of this rectangle.
   * 
   * @return The midpoint of this rectangle.
   */
  public Point getMidpoint() {
    return new Point(getXMidpoint(), getYMidpoint());
  }

  /**
   * Returns the x-coordinate of the midpoint of this rectangle.
   * 
   * @return The x-coordinate of the midpoint of this rectangle.
   */
  public float getXMidpoint() {
    return getMinX() + (getWidth() / 2f);
  }

  /**
   * Returns the y-coordinate of the midpoint of this rectangle.
   * 
   * @return The y-coordinate of the midpoint of this rectangle.
   */
  public float getYMidpoint() {
    return getMinY() + (getHeight() / 2f);
  }

  // ==============================================================================================

  /**
   * Returns the width of this rectangle.
   * 
   * @return The width of this rectangle.
   */
  public float getWidth() {
    return getMaxX() - getMinX();
  }

  /**
   * Returns the height of this rectangle.
   * 
   * @return The height of this rectangle.
   */
  public float getHeight() {
    return getMaxY() - getMinY();
  }

  // ==============================================================================================

  /**
   * Returns the area of this rectangle.
   * 
   * @return The area of this rectangle.
   */
  public float getArea() {
    return getWidth() * getHeight();
  }

  // ==============================================================================================

  /**
   * Extends this rectangle by the given rectangle.
   * 
   * @param rect The rectangle to extend this rectangle with.
   */
  public void extend(Rectangle rect) {
    if (rect == null) {
      return;
    }
    setMinX(Math.min(getMinX(), rect.getMinX()));
    setMaxX(Math.max(getMaxX(), rect.getMaxX()));
    setMinY(Math.min(getMinY(), rect.getMinY()));
    setMaxY(Math.max(getMaxY(), rect.getMaxY()));
  }

  /**
   * Merges this rectangle with the given rectangle and returns the resulting rectangle, that is:
   * the minimum bounding box that contains both rectangles. Note that this method doesn't modify
   * any coordinates of this rectangle, but returns a new instance of {@link Rectangle}.
   * 
   * @param rect The other rectangle.
   * 
   * @return A rectangle representing the minimum bounding box around this rectangle and the other
   *         rectangle.
   */
  public Rectangle union(Rectangle rect) {
    if (rect == null) {
      return null;
    }

    float minX = Math.min(getMinX(), rect.getMinX());
    float maxX = Math.max(getMaxX(), rect.getMaxX());
    float minY = Math.min(getMinY(), rect.getMinY());
    float maxY = Math.max(getMaxY(), rect.getMaxY());

    return new Rectangle(minX, minY, maxX, maxY);
  }

  /**
   * Intersects this rectangle with the given rectangle and returns the rectangle representing the
   * intersection. Note that this method doesn't modify any coordinates of this rectangle, but
   * returns a new instance of {@link Rectangle}.
   * 
   * @param rect The other rectangle.
   * 
   * @return A rectangle representing the intersection of this rectangle and the other rectangle or
   *         null if both rectangles don't overlap.
   */
  public Rectangle intersection(Rectangle rect) {
    if (rect == null) {
      return null;
    }

    float maxMinX = Math.max(getMinX(), rect.getMinX());
    float minMaxX = Math.min(getMaxX(), rect.getMaxX());
    float maxMinY = Math.max(getMinY(), rect.getMinY());
    float minMaxY = Math.min(getMaxY(), rect.getMaxY());

    if (minMaxX <= maxMinX) {
      return null;
    }

    if (minMaxY <= maxMinY) {
      return null;
    }

    return new Rectangle(maxMinX, maxMinY, minMaxX, minMaxY);
  }

  // ==============================================================================================

  /**
   * Checks if this rectangle completely contains the given rectangle.
   * 
   * @param rect The other rectangle.
   * 
   * @return True, when this rectangle completely contains the given rectangle, false otherwise.
   */
  public boolean contains(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    if (rect.getMinX() < getMinX()) {
      return false;
    }
    if (rect.getMaxX() > getMaxX()) {
      return false;
    }
    if (rect.getMinY() < getMinY()) {
      return false;
    }
    if (rect.getMaxY() > getMaxY()) {
      return false;
    }
    return true;
  }

  // ==============================================================================================

  /**
   * Checks if this rectangle overlaps the given rectangle horizontally and vertically (if there is
   * an area that share both rectangles).
   * 
   * @param rect The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle, false otherwise.
   */
  public boolean overlaps(Rectangle rect) {
    return overlapsHorizontally(rect) && overlapsVertically(rect);
  }

  /**
   * Computes the overlap ratio of this rectangle with the given rectangle (= the area of the
   * intersection of both rectangles / area of this rectangle).
   *
   * @param rect The other rectangle.
   * 
   * @return The overlap ratio of this rectangle with the given rectangle.
   */
  public float getOverlapRatio(Rectangle rect) {
    if (getArea() <= 0) {
      return 0;
    }
    return (getHorizontalOverlapLength(rect) * getVerticalOverlapLength(rect)) / getArea();
  }

  // ==============================================================================================

  /**
   * Checks if this rectangle overlaps the given rectangle horizontally.
   * 
   * @param rect The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle horizontally, false otherwise.
   */
  public boolean overlapsHorizontally(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return getMaxX() >= rect.getMinX() && getMinX() <= rect.getMaxX();
  }

  /**
   * Computes the length of the horizontal overlap between this rectangle and the given rectangle.
   * 
   * @param rect The other geometric object.
   * 
   * @return The length of the vertical overlap.
   */
  public float getHorizontalOverlapLength(Rectangle rect) {
    if (rect != null) {
      float minMaxX = Math.min(getMaxX(), rect.getMaxX());
      float maxMinX = Math.max(getMinX(), rect.getMinX());
      return Math.max(0, minMaxX - maxMinX);
    }
    return 0;
  }

  // ==============================================================================================

  /**
   * Checks if this rectangle overlaps the given rectangle vertically.
   * 
   * @param rect The other rectangle.
   * 
   * @return True, if this rectangle overlaps the given rectangle vertically, false otherwise.
   */
  public boolean overlapsVertically(Rectangle rect) {
    if (rect == null) {
      return false;
    }
    return getMinY() <= rect.getMaxY() && getMaxY() >= rect.getMinY();
  }

  /**
   * Computes the length of the vertical overlap between this rectangle and the given rectangle.
   * 
   * @param rect The other rectangle.
   * 
   * @return The length of the vertical overlap.
   */
  public float getVerticalOverlapLength(Rectangle rect) {
    if (rect != null) {
      float minMaxY = Math.min(getMaxY(), rect.getMaxY());
      float maxMinY = Math.max(getMinY(), rect.getMinY());
      return Math.max(0, minMaxY - maxMinY);
    }
    return 0;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "Rectangle(" + getMinX() + "," + getMinY() + "," + getMaxX() + "," + getMaxY() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof Rectangle) {
      Rectangle otherRectangle = (Rectangle) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getMinX(), otherRectangle.getMinX());
      builder.append(getMinY(), otherRectangle.getMinY());
      builder.append(getMaxX(), otherRectangle.getMaxX());
      builder.append(getMaxY(), otherRectangle.getMaxY());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getMinX());
    builder.append(getMinY());
    builder.append(getMaxX());
    builder.append(getMaxY());
    return builder.hashCode();
  }
}
