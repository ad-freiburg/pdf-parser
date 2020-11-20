package ad.freiburg.pdfparser.parser.operators.text;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * Tz: Set the horizontal scaling, to (scale / 100). scale shall be a number specifying the
 * percentage of the normal width. Initial value: 100
 * 
 * @author Claudius Korzen
 */
public class SetTextHorizontalScaling extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (args.size() < 1) {
      throw new MissingOperandException(op, args);
    }

    COSNumber scaling = (COSNumber) args.get(0);
    PDTextState textState = this.parser.getGraphicsState().getTextState();
    textState.setHorizontalScaling(scaling.floatValue());
  }

  @Override
  public String getName() {
    return "Tz";
  }
}
