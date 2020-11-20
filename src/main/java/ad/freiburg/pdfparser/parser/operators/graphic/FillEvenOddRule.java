package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSFloat;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;


/**
 * f*: Fill path using even odd rule.
 * 
 * @author Claudius Korzen
 */
public class FillEvenOddRule extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    // Call operation "stroke path" with given windingRule.
    // Use COSFloat, because COSInteger is private.
    args.add(new COSFloat(Path2D.WIND_EVEN_ODD));
    // Stroke path
    this.parser.processOperator(pdf, page, "S", args);
  }

  @Override
  public String getName() {
    return "f*";
  }
}
