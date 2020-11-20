package ad.freiburg.pdfparser.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ad.freiburg.pdfparser.model.PdfFont;
import ad.freiburg.pdfparser.model.PdfFontFace;

/**
 * A class to manage the font faces of a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfFontFaceManager {
  /**
   * The logger.
   */
  protected static Logger LOG = LogManager.getLogger(PdfFontFaceManager.class);

  /**
   * A map of the already known font faces, per name.
   */
  protected static Map<String, PdfFontFace> knownFontFaces = new HashMap<>();

  // ==============================================================================================

  /**
   * Checks if an {@link PdfFontFace} object has already been created for the given font and font
   * size. If so, it returns this object. Otherwise, creates a new {@link PdfFontFace} object and
   * returns it.
   * 
   * @param font     The font to process.
   * @param fontSize The font size to process.
   * 
   * @return The {@link PdfFontFace} object corresponding to the given font.
   */
  public static PdfFontFace getOrCreatePdfFontFace(PdfFont font, float fontSize) {
    if (font == null) {
      return null;
    }

    // Check if the font face is already known.
    PdfFontFace knownFontFace = getKnownPdfFontFace(font, fontSize);
    if (knownFontFace != null) {
      return knownFontFace;
    }

    // The font face is not known. Create a new font face.
    PdfFontFace newFontFace = new PdfFontFace(font, fontSize);

    // Add the new font face to the map of known font faces.
    knownFontFaces.put(font.getId() + ":" + fontSize, newFontFace);
    LOG.debug("A new font face was registered: " + newFontFace);

    return newFontFace;
  }

  /**
   * Returns the {@link PdfFontFace} object corresponding to the given color, if such an object
   * exists.
   * 
   * @param font     The font to check.
   * @param fontSize The font size to check.
   * 
   * @return the {@link PdfFontFace} object corresponding to the given font and font size, if such
   *         an object exists. Returns null otherwise.
   */
  protected static PdfFontFace getKnownPdfFontFace(PdfFont font, float fontSize) {
    return knownFontFaces.get(font.getId() + ":" + fontSize);
  }
}
