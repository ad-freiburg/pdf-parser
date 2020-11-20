package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.GeneralPath;
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
 * c: Append curved segment to path.
 * 
 * @author Claudius Korzen
 */
public class CurveTo extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    COSNumber x1 = (COSNumber) args.get(0);
    COSNumber y1 = (COSNumber) args.get(1);
    Point p1 = new Point(x1.floatValue(), y1.floatValue());
    this.parser.transform(p1);

    COSNumber x2 = (COSNumber) args.get(2);
    COSNumber y2 = (COSNumber) args.get(3);
    Point p2 = new Point(x2.floatValue(), y2.floatValue());
    this.parser.transform(p2);

    COSNumber x3 = (COSNumber) args.get(4);
    COSNumber y3 = (COSNumber) args.get(5);
    Point p3 = new Point(x3.floatValue(), y3.floatValue());
    this.parser.transform(p3);

    GeneralPath path = this.parser.getLinePath();
    if (path.getCurrentPoint() == null) {
      path.moveTo(p3.getX(), p3.getY());
    } else {
      path.curveTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }
  }

  @Override
  public String getName() {
    return "c";
  }
}
