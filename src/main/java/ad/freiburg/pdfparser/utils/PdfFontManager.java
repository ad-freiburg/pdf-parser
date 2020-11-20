package ad.freiburg.pdfparser.utils;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import ad.freiburg.pdfparser.model.PdfFont;


/**
 * A class to manage the fonts of a PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfFontManager {
  /**
   * The logger.
   */
  protected static Logger LOG = LogManager.getLogger(PdfFontManager.class);

  protected static final String AFM_FILE_FIELD_DELIMITER = "\t";
  protected static final String AFM_FILE_PATH = "afm.map";
  protected static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

  /**
   * A map of the already known fonts, per name.
   */
  // protected static Map<String, PdfFont> knownFonts = readWellKnownFontsFromFile();
  protected static Map<String, PdfFont> knownFonts = new HashMap<>();

  // ==============================================================================================

  /**
   * Checks if an {@link PdfFont} object has already been created for the given PDFont. If so,
   * returns this object. Otherwise, creates a new {@link PdfFont} object and returns it.
   * 
   * @param font The font to check.
   * 
   * @return The {@link PdfFont} object corresponding to the given font.
   */
  public static PdfFont getOrCreatePdfFont(PDFont font) {
    if (font == null) {
      return null;
    }

    // Check if the font is already known.
    PdfFont knownFont = getKnownPdfFont(font);
    if (knownFont != null) {
      return knownFont;
    }

    // The font is not known. Create a new font.
    PdfFont newFont = new PdfFont();
    newFont.setId("font-" + knownFonts.size());
    newFont.setNormalizedName(computeNormalizedName(font));
    newFont.setBasename(computeBasename(newFont));
    newFont.setIsBold(computeIsBold(newFont));
    newFont.setIsItalic(computeIsItalic(newFont));
    newFont.setIsType3Font(computeIsType3Font(font));

    // Add the new font to the map of known fonts.
    knownFonts.put(newFont.getNormalizedName(), newFont);
    LOG.debug("A new font was registered: " + newFont);

    return newFont;
  }

  // ==============================================================================================

  // /**
  // * Reads some font specifications from file. This method was introduced to get meta data about
  // * fonts like "cmr9", from which we can't derive from the font name alone, if the font is bold
  // (or
  // * italic).
  // *
  // * @return The well-known fonts per name.
  // */
  // protected static Map<String, PdfFont> readWellKnownFontsFromFile() {
  // Map<String, PdfFont> knownFonts = new HashMap<>();

  // // Read the AFM file that contains some metadata about common fonts.
  // ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
  // InputStream afm = classLoader.getResourceAsStream(AFM_FILE_PATH);

  // LOG.debug("Reading the AFM file '" + AFM_FILE_PATH + "'.");

  // if (afm != null) {
  // try (BufferedReader br = new BufferedReader(new InputStreamReader(afm, DEFAULT_ENCODING))) {
  // String line;
  // while ((line = br.readLine()) != null) {
  // if (line.trim().isEmpty()) {
  // continue;
  // }

  // String[] fields = line.split(AFM_FILE_FIELD_DELIMITER);
  // if (fields.length != 5) {
  // continue;
  // }

  // // Create the font from the line.
  // PdfFont font = new PdfFont();
  // font.setId("font-" + knownFonts.size());
  // font.setNormalizedName(fields[0]);
  // font.setBasename(computeBasename(font));
  // font.setFontFamilyName(fields[2].trim());
  // font.setIsBold(fields[3].trim().equals("1"));
  // font.setIsItalic(fields[4].trim().equals("1"));
  // font.setIsType3Font(false);

  // knownFonts.put(font.getNormalizedName(), font);
  // LOG.trace("Read font: " + font);
  // }
  // } catch (IOException e) {
  // LOG.warn("An error occurred on reading the AFM file.", e);
  // }
  // }

  // LOG.debug("Reading the AFM file done.");
  // LOG.debug("# read fonts: " + knownFonts.size());
  // return knownFonts;
  // }

  /**
   * Checks if an {@link PdfFont} object has already been created for the given font.
   * 
   * @param rgb The font to check.
   *
   * @return True, if an {@link PdfFont} object has already been created for the given color, false
   *         otherwise.
   */
  protected static boolean isKnownPdfFont(PDFont font) {
    return getKnownPdfFont(font) != null;
  }

  /**
   * Returns the {@link PdfFont} object corresponding to the given color, if such an object exists.
   * 
   * @param rgb The font to check.
   *
   * @return The {@link PdfFont} object corresponding to the given font, if such an object exists.
   *         Returns null otherwise.
   */
  protected static PdfFont getKnownPdfFont(PDFont font) {
    if (font == null) {
      return null;
    }
    return knownFonts.get(computeNormalizedName(font));
  }

  // ==============================================================================================

  /**
   * Computes the normalized name of the given font, that is: the lower cased font name without the
   * prefix (the part before "+"). For example, the normalized name of "LTSLOS+NimbusSanL-Bold" is
   * "nimbussanl-bold".
   * 
   * @param font The font to process.
   * 
   * @return The normalized name of the font.
   */
  protected static String computeNormalizedName(PDFont font) {
    boolean isType3Font = computeIsType3Font(font);
    if (isType3Font) {
      return "type3";
    }

    String normalized = font.getName().toLowerCase();

    // Eliminate the prefix up to the "+".
    int indexPlus = normalized.indexOf("+");
    if (indexPlus > -1) {
      normalized = normalized.substring(indexPlus + 1);
    }

    return normalized;
  }

  // ==============================================================================================

  /**
   * Computes the basename of the given font, that is: the normalized name without the part after
   * the "-" symbol. For example, the basename of "LTSLOS+NimbusSanL-Bold" is "nimbussanl".
   * 
   * @param font The font to process.
   *
   * @return The basename of the given font.
   */
  public static String computeBasename(PdfFont font) {
    // Compute the basename from the name: "LTSLOS+NimbusSanL-Bold"
    String basename = font.getNormalizedName();

    // Eliminate trailing characters starting at the "-": nimbussanl
    int indexMinus = basename.indexOf("-");
    if (indexMinus > -1) {
      basename = basename.substring(0, indexMinus);
    }

    // Replace all other special characters and digits.
    return basename.replaceAll("[0-9\\-, ]", "");
  }

  /**
   * Checks if the given PDFont is a Type3 font.
   * 
   * @param font The font to check.
   * 
   * @return True, if the given font is a Type3 font, false otherwise.
   */
  public static boolean computeIsType3Font(PDFont font) {
    return font instanceof PDType3Font;
  }

  /**
   * Checks if the given font represents a bold-faced font.
   * 
   * @param font The font to check.
   * 
   * @return True, if the given font represents a bold-faced font, false otherwise.
   */
  public static boolean computeIsBold(PdfFont font) {
    String fontName = font.getNormalizedName();

    if (fontName != null) {
      int lengthBefore = fontName.length();
      fontName = fontName.replaceAll("bold", "").replaceAll("black", "");
      int lengthAfter = fontName.length();
      return lengthAfter < lengthBefore;
    }

    return false;
  }

  /**
   * Checks if the given font represents an italic-faced font.
   * 
   * @param font The font to check.
   * @return True, if the given font represents an italic-faced font.
   */
  public static boolean computeIsItalic(PdfFont font) {
    String fontName = font.getNormalizedName();

    if (fontName != null) {
      int lengthBefore = fontName.length();
      fontName = fontName.replaceAll("ital", "").replaceAll("oblique", "");
      int lengthAfter = fontName.length();
      return lengthAfter < lengthBefore;
    }

    return false;
  }
}
