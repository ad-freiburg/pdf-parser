package ad.freiburg.pdfparser.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A collection of some common useful methods dealing with colors.
 * 
 * @author Claudius Korzen
 */
public class ColorUtils {
  /**
   * Transforms the given packed RGB value into an array of three values in range [0,255],
   * representing the R, G and B values.
   * 
   * @param pixel The packed RGB value.
   * 
   * @return The RGB values in an array of three values.
   */
  public static int[] toRgbArray(int pixel) {
    int alpha = (pixel >> 24) & 0xff;
    int red = ((pixel >> 16) & 0xff);
    int green = ((pixel >> 8) & 0xff);
    int blue = ((pixel) & 0xff);
    return new int[] {red, green, blue, alpha};
  }

  /**
   * Checks if the given image consists only of a single color and returns the color if so. Returns
   * null if there are at least two different colors in the image.
   * 
   * @param im The image to process.
   * 
   * @return The color, if the image consists only of a single color; null otherwise.
   * @throws IOException If something went wrong on reading the image.
   */
  public static int[] getExclusiveColor(BufferedImage im) throws IOException {
    if (im == null) {
      return null;
    }

    int lastRgb = Integer.MAX_VALUE;
    for (int i = 0; i < im.getWidth(); i++) {
      for (int j = 0; j < im.getHeight(); j++) {
        int rgb = im.getRGB(i, j);
        if (lastRgb != Integer.MAX_VALUE && lastRgb != rgb) {
          return null;
        }
        lastRgb = rgb;
      }
    }

    if (lastRgb == Integer.MAX_VALUE) {
      return null;
    }

    return toRgbArray(lastRgb);
  }
}
