package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * n: Ends the current path without filling or stroking it.
 * 
 * @author Claudius Korzen
 */
public class EndPath extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    GeneralPath path = this.parser.getLinePath();
    if (this.parser.getClippingWindingRule() != -1) {
      path.setWindingRule(this.parser.getClippingWindingRule());
      this.parser.getGraphicsState().intersectClippingPath(path);
      this.parser.setClippingWindingRule(-1);
    }

    PathIterator itr;
    for (itr = path.getPathIterator(null); !itr.isDone(); itr.next()) {
      float[] coordinates = new float[6];
      int currentSegment = itr.currentSegment(coordinates);

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          float[] lastMoveTo = this.parser.getLinePathLastMoveToPosition();
          this.parser.setLinePathPosition(lastMoveTo);
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);
          this.parser.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);
          this.parser.setLinePathPosition(lineEnd);
          break;
        case PathIterator.SEG_MOVETO:
          float[] pos = Arrays.copyOf(coordinates, 2);
          this.parser.setLinePathLastMoveToPosition(pos);
          this.parser.setLinePathPosition(pos);
          break;
        case PathIterator.SEG_QUADTO:
          float[] quadEnd = Arrays.copyOfRange(coordinates, 2, 4);
          this.parser.setLinePathPosition(quadEnd);
          break;
        default:
          break;
      }
    }
    path.reset();
  }

  @Override
  public String getName() {
    return "n";
  }
}
