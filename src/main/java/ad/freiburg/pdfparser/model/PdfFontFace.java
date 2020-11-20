package ad.freiburg.pdfparser.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A font face (= font + font size) of piece of text in a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfFontFace {
  /**
   * The font.
   */
  protected PdfFont font;

  /**
   * The font size.
   */
  protected float fontSize;

  // ==============================================================================================

  /**
   * Creates a new font face (= font + font size).
   * 
   * @param font     The font.
   * @param fontSize The font size.
   */
  public PdfFontFace(PdfFont font, float fontSize) {
    this.font = font;
    this.fontSize = fontSize;
  }

  // ==============================================================================================

  /**
   * Returns the font.
   * 
   * @return The font.
   */
  public PdfFont getFont() {
    return this.font;
  }

  /**
   * Sets the font.
   * 
   * @param font The font.
   */
  public void setFont(PdfFont font) {
    this.font = font;
  }

  // ==============================================================================================

  /**
   * Returns the font size.
   * 
   * @return The font size.
   */
  public float getFontSize() {
    return this.fontSize;
  }

  /**
   * Sets the font size.
   * 
   * @param fontSize The font size.
   */
  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfFontFace(" + this.font + ", " + this.fontSize + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfFontFace) {
      PdfFontFace otherCharacter = (PdfFontFace) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getFont(), otherCharacter.getFont());
      builder.append(getFontSize(), otherCharacter.getFontSize());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getFont());
    builder.append(getFontSize());
    return builder.hashCode();
  }
}
