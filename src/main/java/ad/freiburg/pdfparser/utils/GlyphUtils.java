package ad.freiburg.pdfparser.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.encoding.GlyphList;

/**
 * A collection of some utility methods to manage and process additional glyphs.
 * 
 * @author Claudius Korzen
 */
public class GlyphUtils {
  /**
   * The logger.
   */
  protected static Logger LOG = LogManager.getLogger(GlyphUtils.class);

  /**
   * The additional glyphs.
   */
  protected static final GlyphList additionalGlyphs;
  static {
    additionalGlyphs = readAdditionalGlyphs("org/apache/pdfbox/resources/glyphlist/additional.txt");
  }

  // ==============================================================================================

  /**
   * Returns a list of additional glyphs.
   * 
   * @return A list of additional glyphs.
   */
  public static GlyphList getAdditionalGlyphs() {
    return additionalGlyphs;
  }

  /**
   * Reads additional glyphs from file.
   * 
   * @param path The path to the file to read from.
   * 
   * @return An instance of GlyphList that included the additional glyphs.
   * 
   * @TODO: Further investigation what this method actually does.
   * @TODO: Check, if this method works properly.
   */
  protected static GlyphList readAdditionalGlyphs(String path) {
    LOG.debug("Reading additional glyphs from path '" + path + "'.");

    GlyphList glyphList = null;
    ClassLoader classLoader = GlyphList.class.getClassLoader();
    try (InputStream in = classLoader.getResourceAsStream(path)) {
      glyphList = new GlyphList(GlyphList.getAdobeGlyphList(), in);
    } catch (IOException e) {
      LOG.warn("An error occurred while reading additional glyphs.", e);
    }

    LOG.debug("Reading additional glyphs from path done.");
    return glyphList;
  }
}
