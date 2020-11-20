package ad.freiburg.pdfparser.parser.operators.text;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.Matrix;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * BT: Begin a text object, initializing the text matrix, Tm, and the text line matrix to the
 * identity matrix.
 *
 * @author Claudius Korzen
 */
public class BeginText extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    this.parser.setTextMatrix(new Matrix());
    this.parser.setTextLineMatrix(new Matrix());
    // context.beginText();
  }

  @Override
  public String getName() {
    return "BT";
  }
}
