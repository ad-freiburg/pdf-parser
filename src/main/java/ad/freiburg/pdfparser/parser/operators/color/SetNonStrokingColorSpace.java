package ad.freiburg.pdfparser.parser.operators.color;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * cs: Sets the non-stroking color space.
 * 
 * @author Claudius Korzen
 */
public class SetNonStrokingColorSpace extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    COSName name = (COSName) args.get(0);
    PDColorSpace cs = this.parser.getResources().getColorSpace(name);
    this.parser.getGraphicsState().setNonStrokingColorSpace(cs);
    this.parser.getGraphicsState().setNonStrokingColor(cs.getInitialColor());
  }

  @Override
  public String getName() {
    return "cs";
  }
}
