package ad.freiburg.pdfparser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.fontbox.afm.AFMParser;
import org.apache.fontbox.afm.CharMetric;
import org.apache.fontbox.afm.FontMetrics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

// TODO: This doesn't work, because the afm files are missing.

/**
 * A collection of some common useful methods dealing with AFM files.
 * 
 * @author Claudius Korzen
 */
public class AfmUtils {
  /**
   * The logger.
   */
  protected static Logger LOG = LogManager.getLogger(AfmUtils.class);

  /**
   * The additional glyphs read from external AFM files.
   */
  protected static final Map<String, FontMetricsWrapper> ADDITIONAL_AFM = readAfmFiles("afm/");

  /**
   * Returns the CharMetric object for the glyph given by glyphName.
   * 
   * @param glyphName The name of the glyph.
   * @param type1Font The type 1 font.
   * 
   * @return The CharMetric object.
   */
  public static CharMetric getCharMetric(String glyphName, PDType1Font type1Font) {
    if (type1Font != null) {
      String basename = FontUtils.computeBasename(type1Font).toLowerCase();

      FontMetricsWrapper fontMetrics = ADDITIONAL_AFM.get(basename);
      if (fontMetrics != null) {
        return fontMetrics.getCharMetricsMap().get(glyphName);
      }
    }
    return null;
  }

  /**
   * Reads additional AFM files in addition to the 14 standard fonts.
   * 
   * @param path The path to the directory of AFM files.
   * 
   * @return The map containing the font metrics read from the AFM files.
   */
  protected static Map<String, FontMetricsWrapper> readAfmFiles(String path) {
    LOG.debug("Reading additional AFM files from path '" + path + "'.");

    Map<String, FontMetricsWrapper> result = new HashMap<>();
    try {
      Map<String, InputStream> files = PathUtils.readDirectory(path);

      for (Entry<String, InputStream> file : files.entrySet()) {
        String name = file.getKey();
        try (InputStream stream = file.getValue()) {
          String basename = PathUtils.getBasename(name).toLowerCase();

          try {
            AFMParser parser = new AFMParser(stream);
            FontMetrics fontMetrics = parser.parse();

            // Put the result into map.
            result.put(basename, new FontMetricsWrapper(fontMetrics));
          } catch (IOException e) {
            continue;
          }
        }
      }
    } catch (IOException e) {
      LOG.warn("An error occurred while reading additional AFM files.", e);
    }

    LOG.debug("Reading additional AFM files from path done.");
    LOG.debug("# read additional AFM files: " + result.size());

    return result;
  }

}
