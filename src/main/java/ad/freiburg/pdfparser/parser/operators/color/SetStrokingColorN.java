package ad.freiburg.pdfparser.parser.operators.color;

/**
 * SCN: Sets the color to use for stroking stroking operations. Supports Pattern, Separation,
 * DeviceN and ICCBased color spaces.
 *
 * @author Claudius Korzen
 */
public class SetStrokingColorN extends SetStrokingColor {
  @Override
  public String getName() {
    return "SCN";
  }
}
