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
 * Ts: Set the text rise to rise, which shall be a number expressed in unscaled text space units.
 * 
 * @author Claudius Korzen
 */
public class SetTextRise extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (args.isEmpty()) {
      throw new MissingOperandException(op, args);
    }

    COSNumber rise = (COSNumber) args.get(0);
    PDTextState textState = this.parser.getGraphicsState().getTextState();
    textState.setRise(rise.floatValue());
  }

  @Override
  public String getName() {
    return "Ts";
  }
}
