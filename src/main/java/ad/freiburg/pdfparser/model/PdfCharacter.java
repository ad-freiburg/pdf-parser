package ad.freiburg.pdfparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A character in a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfCharacter extends PdfElement {
  /**
   * The text of this character.
   */
  protected String text;

  /**
   * The position of this character in the PDF document.
   */
  protected PdfPosition position;

  /**
   * The font face (= font and font size) of this character.
   */
  protected PdfFontFace fontFace;

  /**
   * The color of this character.
   */
  protected PdfColor color;

  /**
   * The rank of this character in the extraction order of elements.
   */
  protected int extractionRank;

  // ==============================================================================================

  /**
   * Returns the text of this character.
   * 
   * @return The text of this character.
   */
  public String getText() {
    return this.text;
  }

  /**
   * Sets the text of this character.
   * 
   * @param text The text of this character.
   */
  public void setText(String text) {
    this.text = text;
  }

  // ==============================================================================================

  /**
   * Returns the position of this character in the PDF document.
   * 
   * @return The position of this character in the PDF document.
   */
  public PdfPosition getPosition() {
    return this.position;
  }

  /**
   * Sets the position of this character in the PDF document.
   * 
   * @param position The position of this character in the PDF document.
   */
  public void setPosition(PdfPosition position) {
    this.position = position;
  }

  // ==============================================================================================

  /**
   * Returns the font face of this character.
   * 
   * @return The font face of this character.
   */
  public PdfFontFace getFontFace() {
    return this.fontFace;
  }

  /**
   * Sets the font face of this character.
   * 
   * @param fontFace The font face of this character.
   */
  public void setFontFace(PdfFontFace fontFace) {
    this.fontFace = fontFace;
  }

  // ==============================================================================================

  /**
   * Returns the color of this character.
   * 
   * @return The color of this character.
   */
  public PdfColor getColor() {
    return this.color;
  }

  /**
   * Sets the color of this character.
   * 
   * @param color The color of this character.
   */
  public void setColor(PdfColor color) {
    this.color = color;
  }

  // ==============================================================================================

  /**
   * Returns the rank of this character in the extraction order of PDF elements.
   * 
   * @return The rank of this character in the extraction order of PDF elements.
   */
  public int getExtractionRank() {
    return this.extractionRank;
  }

  /**
   * Sets the rank of this character in the extraction order of PDF elements.
   * 
   * @param rank The rank of this character in the extraction order of PDF elements.
   */
  public void setExtractionRank(int rank) {
    this.extractionRank = rank;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfCharacter(" + getText() + ", " + getPosition() + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfCharacter) {
      PdfCharacter otherCharacter = (PdfCharacter) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getText(), otherCharacter.getText());
      builder.append(getPosition(), otherCharacter.getPosition());
      builder.append(getFontFace(), otherCharacter.getFontFace());
      builder.append(getColor(), otherCharacter.getColor());
      builder.append(getExtractionRank(), otherCharacter.getExtractionRank());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getText());
    builder.append(getPosition());
    builder.append(getFontFace());
    builder.append(getColor());
    builder.append(getExtractionRank());
    return builder.hashCode();
  }
}
