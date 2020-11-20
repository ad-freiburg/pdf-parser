package ad.freiburg.pdfparser.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import ad.freiburg.pdfparser.model.PdfColor;

/**
 * A class to manage the colors of a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfColorManager {
  /**
   * The logger.
   */
  protected static Logger LOG = LogManager.getLogger(PdfColorManager.class);

  /**
   * A map of the already known colors, per name.
   */
  protected static Map<String, PdfColor> knownColors = new HashMap<>();

  // ==============================================================================================

  /**
   * Checks if an {@link PdfColor} object has already been created for the given color. If so,
   * returns this object. Otherwise, creates a new {@link PdfColor} object and returns it.
   * 
   * @param color      The color to check.
   * @param colorSpace The belonging color space.
   * 
   * @return The {@link PdfColor} object corresponding to the given color.
   */
  public static PdfColor getOrCreatePdfColor(PDColor color, PDColorSpace colorSpace) {
    return getOrCreatePdfColor(toRGB(color, colorSpace));
  }

  /**
   * Checks if an {@link PdfColor} object has already been created for the given color. If so,
   * returns this object. Otherwise, creates an {@link PdfColor} object and returns it.
   * 
   * @param rgb The color to check, given as an array of RGB values.
   * 
   * @return The {@link PdfColor} object corresponding to the given color.
   */
  public static PdfColor getOrCreatePdfColor(int[] rgb) {
    if (rgb == null) {
      return null;
    }

    // Check if the color is already known.
    PdfColor knownColor = getKnownPdfColor(rgb);
    if (knownColor != null) {
      return knownColor;
    }

    // The color is not known. Create a new color.
    PdfColor newColor = new PdfColor();
    newColor.setId("color-" + knownColors.size());
    newColor.setName(computeColorName(rgb));
    newColor.setRGB(rgb);

    // Add the new color to the map of known colors.
    knownColors.put(newColor.getName(), newColor);
    LOG.debug("A new color was registered: " + newColor);

    return newColor;
  }

  // ==============================================================================================

  /**
   * Checks if an {@link PdfColor} object has already been created for the given color.
   * 
   * @param rgb The color to check.
   *
   * @return True, if an {@link PdfColor} object has already been created for the given color, false
   *         otherwise.
   */
  protected static boolean isKnownPdfColor(int[] rgb) {
    return getKnownPdfColor(rgb) != null;
  }

  /**
   * Returns the {@link PdfColor} object corresponding to the given color, if such an object exists.
   * 
   * @param rgb The color to check.
   *
   * @return the {@link PdfColor} object corresponding to the given color, if such an object exists.
   *         Returns null otherwise.
   */
  protected static PdfColor getKnownPdfColor(int[] rgb) {
    if (rgb == null) {
      return null;
    }
    return knownColors.get(computeColorName(rgb));
  }

  /**
   * Computes a name for the given color.
   * 
   * @param rgb The color to process.
   * 
   * @return A name for the given color.
   */
  protected static String computeColorName(int[] rgb) {
    return Arrays.toString(rgb);
  }

  // ==============================================================================================

  /**
   * Translates the the given PDF color to an RGB-array.
   * 
   * @param color      The color to process.
   * @param colorSpace The color space of the color to process.
   * 
   * @return An array of length 3, containing the R, G and B values.
   */
  protected static int[] toRGB(PDColor color, PDColorSpace colorSpace) {
    if (color == null || colorSpace == null) {
      return null;
    }

    int[] rgb = {0, 0, 0};

    try {
      float[] x = colorSpace.toRGB(color.getComponents());
      rgb[0] = (int) (x[0] * 255);
      rgb[1] = (int) (x[1] * 255);
      rgb[2] = (int) (x[2] * 255);
      return rgb;
    } catch (Exception e) {
      return rgb;
    }
  }
}
