package ad.freiburg.pdfparser.parser.operators.color;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceColorSpace;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;


/**
 * sc,scn,SC,SCN: Sets the color to use for stroking or non-stroking operations.
 * 
 * @author Claudius Korzen
 */
public abstract class SetColor extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    PDColorSpace cs = getColorSpace();
    if (cs instanceof PDDeviceColorSpace) {
      if (args.size() < cs.getNumberOfComponents()) {
        throw new MissingOperandException(op, args);
      }
    }
    COSArray array = new COSArray();
    array.addAll(args);

    setColor(new PDColor(array, cs));
  }

  /**
   * Sets either the stroking or non-stroking color value.
   * 
   * @param color The stroking or non-stroking color value.
   */
  protected abstract void setColor(PDColor color);

  /**
   * Returns either the stroking or non-stroking color space.
   * 
   * @return The stroking or non-stroking color space.
   */
  protected abstract PDColorSpace getColorSpace();
}
