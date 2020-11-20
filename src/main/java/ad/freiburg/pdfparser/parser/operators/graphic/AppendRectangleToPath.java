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
 * re: Appends a rectangle to the path.
 * 
 * @author Claudius Korzen.
 */
public class AppendRectangleToPath extends OperatorProcessor {

  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    COSNumber x = (COSNumber) args.get(0);
    COSNumber y = (COSNumber) args.get(1);
    COSNumber w = (COSNumber) args.get(2);
    COSNumber h = (COSNumber) args.get(3);

    float minX = x.floatValue();
    float minY = y.floatValue();
    Point ll = new Point(minX, minY);
    this.parser.transform(ll);

    float maxX = minX + w.floatValue();
    float maxY = minY + h.floatValue();
    Point ur = new Point(maxX, maxY);
    this.parser.transform(ur);

    // To ensure that the path is created in the right direction, we have to create it by combining
    // single lines instead of creating a simple rectangle.
    GeneralPath path = this.parser.getLinePath();
    path.moveTo(ll.getX(), ll.getY());
    path.lineTo(ur.getX(), ll.getY());
    path.lineTo(ur.getX(), ur.getY());
    path.lineTo(ll.getX(), ur.getY());
    path.lineTo(ll.getX(), ll.getY());
  }

  @Override
  public String getName() {
    return "re";
  }
}
