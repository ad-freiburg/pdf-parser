package ad.freiburg.pdfparser.parser.operators.text;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.font.PDFont;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * Tf: Set the text font to font and the text font size to size. font shall be the name of a font
 * resource in the Font sub-dictionary of the current resource dictionary; size shall be a number
 * representing a scale factor.
 * 
 * @author Claudius Korzen
 */
public class SetFontAndSize extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (args.size() < 2) {
      throw new MissingOperandException(op, args);
    }

    COSName fontName = (COSName) args.get(0);
    float fontSize = ((COSNumber) args.get(1)).floatValue();
    this.parser.getGraphicsState().getTextState().setFontSize(fontSize);
    // TODO: This needs a lot of time (> 400ms) when called the first time. Reimplement it.
    PDFont font = this.parser.getResources().getFont(fontName);
    this.parser.getGraphicsState().getTextState().setFont(font);

  }

  @Override
  public String getName() {
    return "Tf";
  }
}
