package ad.freiburg.pdfparser.parser.operators.text;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.state.PDTextState;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * Tw: Set the word spacing to wordSpace, which shall be a number expressed in unscaled text space
 * units.
 * 
 * @author Claudius Korzen
 */
public class SetWordSpacing extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    COSNumber wordSpacing = (COSNumber) args.get(0);
    PDTextState textState = this.parser.getGraphicsState().getTextState();
    textState.setWordSpacing(wordSpacing.floatValue());
  }

  @Override
  public String getName() {
    return "Tw";
  }
}
