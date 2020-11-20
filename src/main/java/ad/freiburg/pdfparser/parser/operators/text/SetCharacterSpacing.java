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
 * Tc: Set the character spacing to charSpace, which shall be a number expressed in unscaled text
 * space units.
 * 
 * @author Claudius Korzen
 */
public class SetCharacterSpacing extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (args.isEmpty()) {
      throw new MissingOperandException(op, args);
    }

    // There are some documents which are incorrectly structured, and have
    // a wrong number of arguments to this, so we will assume the last argument
    // in the list
    Object charSpacing = args.get(args.size() - 1);
    if (charSpacing instanceof COSNumber) {
      COSNumber characterSpacing = (COSNumber) charSpacing;
      PDTextState textState = this.parser.getGraphicsState().getTextState();
      textState.setCharacterSpacing(characterSpacing.floatValue());
    }
  }

  @Override
  public String getName() {
    return "Tc";
  }
}
