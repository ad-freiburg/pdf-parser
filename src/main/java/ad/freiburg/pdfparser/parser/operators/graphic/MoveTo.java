package ad.freiburg.pdfparser.parser.operators.graphic;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.model.Point;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * m: Begins a new subpath.
 * 
 * @author Claudius Korzen
 */
public class MoveTo extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);
    Point point = new Point(x.floatValue(), y.floatValue());
    this.parser.transform(point);

    this.parser.getLinePath().moveTo(point.getX(), point.getY());
  }

  @Override
  public String getName() {
    return "m";
  }
}
