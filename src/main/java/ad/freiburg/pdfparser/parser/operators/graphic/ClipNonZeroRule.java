package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * W: Set the clipping path using non zero winding rule.
 * 
 * @author Claudius Korzen
 */
public class ClipNonZeroRule extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    this.parser.setClippingWindingRule(Path2D.WIND_NON_ZERO);
  }

  @Override
  public String getName() {
    return "W";
  }
}
