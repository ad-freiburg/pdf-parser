package ad.freiburg.pdfparser.parser.operators.graphic;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * Q: Restore the graphics state by removing the most recently saved state from the stack and making
 * it the current state.
 * 
 * @author Claudius Korzen
 */
public class RestoreGraphicsState extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (this.parser.getGraphicsStackSize() > 1) {
      this.parser.restoreGraphicsState();
    }
    // else {
    // this shouldn't happen but it does, see PDFBOX-161
    // throw new EmptyGraphicsStackException();
    // }
  }

  @Override
  public String getName() {
    return "Q";
  }
}
