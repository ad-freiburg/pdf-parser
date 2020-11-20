package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * B: Fill and then stroke the path, using the nonzero winding number rule to determine the region
 * to fill.
 * 
 * @author Claudius Korzen
 */
public class FillNonZeroAndStrokePath extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    GeneralPath path = (GeneralPath) this.parser.getLinePath().clone();

    this.parser.processOperator(pdf, page, "f", args);
    this.parser.setLinePath(path);
    this.parser.processOperator(pdf, page, "S", args);
  }

  @Override
  public String getName() {
    return "B";
  }
}
