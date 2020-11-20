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
 * v: Append curved segment to path with the initial point replicated.
 * 
 * @author Claudius Korzen
 */
public class CurveToReplicateInitialPoint extends OperatorProcessor {

  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    COSNumber x2 = (COSNumber) args.get(0);
    COSNumber y2 = (COSNumber) args.get(1);
    Point p2 = new Point(x2.floatValue(), y2.floatValue());
    this.parser.transform(p2);

    COSNumber x3 = (COSNumber) args.get(2);
    COSNumber y3 = (COSNumber) args.get(3);
    Point p3 = new Point(x3.floatValue(), y3.floatValue());
    this.parser.transform(p3);

    GeneralPath path = this.parser.getLinePath();
    if (path.getCurrentPoint() == null) {
      path.moveTo(p3.getX(), p3.getY());
    } else {
      float currentX = (float) path.getCurrentPoint().getX();
      float currentY = (float) path.getCurrentPoint().getY();
      path.curveTo(currentX, currentY, p2.getX(), p2.getY(), p3.getX(), p3.getY());
    }
  }

  @Override
  public String getName() {
    return "v";
  }
}
