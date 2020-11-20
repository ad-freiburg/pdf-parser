package ad.freiburg.pdfparser.utils;

/**
 * A collection of some common useful math methods.
 * 
 * @author Claudius Korzen
 */
public class MathUtils {
  /**
   * Returns true, if the two given float numbers are equal, with respect to a tolerance of 0.001.
   * 
   * @param float1 The first float to compare.
   * @param float2 The second float to compare.
   * 
   * @return True, if the two given float number are almost equal, with respect to a tolerance of
   *         0.001.
   */
  public static boolean isEqual(float float1, float float2) {
    return isEqual(float1, float2, 0.001f);
  }

  /**
   * Returns true, if the two given float number are almost equal (with respect to the given
   * tolerance).
   * 
   * @param f1        The first float to compare.
   * @param f2        The second float to compare.
   * @param tolerance The tolerance.
   *
   * @return True, if the two given float number are almost equal.
   */
  public static boolean isEqual(float f1, float f2, float tolerance) {
    return Math.abs(f1 - f2) <= tolerance;
  }

  /**
   * Returns true, if the first given float is larger (with respect to the given tolerance) than the
   * second given float.
   * 
   * @param f1        The first float to compare.
   * @param f2        The second float to compare.
   * @param tolerance The tolerance.
   * 
   * @return True, if the first float is larger than the second float.
   */
  public static boolean isLarger(float f1, float f2, float tolerance) {
    return f1 - f2 > tolerance;
  }

  /**
   * Returns true, if the first given float is larger than or equal to the second given float (with
   * respect to the given tolerance).
   * 
   * @param f1        The first float to compare.
   * @param f2        The second float to compare.
   * @param tolerance The tolerance.
   * 
   * @return True, if the first float is larger than the second float.
   */
  public static boolean isLargerOrEqual(float f1, float f2, float tolerance) {
    return isLarger(f1, f2, tolerance) || isEqual(f1, f2, tolerance);
  }

  /**
   * Returns true, if the first given float is smaller (with respect to the given tolerance) than
   * the second given float.
   * 
   * @param f1        The first float to compare.
   * @param f2        The second float to compare.
   * @param tolerance The tolerance.
   * 
   * @return True, if the first float is smaller than the second float.
   */
  public static boolean isSmaller(float f1, float f2, float tolerance) {
    return f2 - f1 > tolerance;
  }

  /**
   * Returns true, if the first given float is smaller than or equal to the second given float (with
   * respect to the given tolerance).
   * 
   * @param f1        The first float to compare.
   * @param f2        The second float to compare.
   * @param tolerance The tolerance.
   * 
   * @return True, if the first float is smaller than the second float.
   */
  public static boolean isSmallerOrEqual(float f1, float f2, float tolerance) {
    return isSmaller(f1, f2, tolerance) || isEqual(f1, f2, tolerance);
  }

  /**
   * Rounds the given number and returns a floating number with the given number of decimal places.
   * If numDecimalPlaces < 0, the number is not rounded.
   * 
   * @param number           The number to round.
   * @param numDecimalPlaces The number of decimal places.
   * 
   * @return The rounded number.
   */
  public static float round(float number, int numDecimalPlaces) {
    if (numDecimalPlaces < 0) {
      return number;
    }
    if (numDecimalPlaces == 0) {
      return Math.round(number);
    }
    float decimal = numDecimalPlaces * 10;
    return Math.round(number * decimal) / decimal;
  }
}
