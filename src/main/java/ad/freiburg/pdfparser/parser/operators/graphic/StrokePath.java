package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import ad.freiburg.pdfparser.model.PdfColor;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.model.PdfPosition;
import ad.freiburg.pdfparser.model.PdfShape;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;
import ad.freiburg.pdfparser.utils.MathUtils;
import ad.freiburg.pdfparser.utils.PdfColorManager;

/**
 * S: Stroke the path.
 * 
 * @author Claudius Korzen
 */
public class StrokePath extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    int windingRule = -1;
    if (args.size() > 0) {
      windingRule = ((COSNumber) args.get(0)).intValue();
    }

    // Compute the color of the path.
    PDColor c;
    PDColorSpace cs;
    if (windingRule < 0) {
      c = this.parser.getGraphicsState().getStrokingColor();
      cs = this.parser.getGraphicsState().getStrokingColorSpace();
    } else {
      c = this.parser.getGraphicsState().getNonStrokingColor();
      cs = this.parser.getGraphicsState().getNonStrokingColorSpace();
    }
    PdfColor color = PdfColorManager.getOrCreatePdfColor(c, cs);

    // Iterate through the single path operations to compute, e.g., the bounding box.
    GeneralPath path = this.parser.getLinePath();
    PathIterator itr;
    int prevSegment = -1;
    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    float maxX = -Float.MAX_VALUE;
    float maxY = -Float.MAX_VALUE;
    int roundingPrecision = this.parser.getFloatingPointPrecision();

    for (itr = path.getPathIterator(null); !itr.isDone(); itr.next()) {
      float[] coordinates = new float[6];
      int currentSegment = itr.currentSegment(coordinates);

      // Don't consider a MOVETO operation, if it is the last operation in path.
      if (prevSegment == PathIterator.SEG_MOVETO && currentSegment != PathIterator.SEG_MOVETO) {
        if (this.parser.getLinePathLastMoveToPosition()[0] < minX) {
          minX = this.parser.getLinePathLastMoveToPosition()[0];
        }
        if (this.parser.getLinePathLastMoveToPosition()[0] > maxX) {
          maxX = this.parser.getLinePathLastMoveToPosition()[0];
        }
        if (this.parser.getLinePathLastMoveToPosition()[1] < minY) {
          minY = this.parser.getLinePathLastMoveToPosition()[1];
        }
        if (this.parser.getLinePathLastMoveToPosition()[1] > maxY) {
          maxY = this.parser.getLinePathLastMoveToPosition()[1];
        }
      }

      float[] pathPosition = this.parser.getLinePathPosition();

      switch (currentSegment) {
        case PathIterator.SEG_CLOSE:
          float[] lastMoveTo = this.parser.getLinePathLastMoveToPosition();
          this.parser.setLinePathPosition(lastMoveTo);
          break;
        case PathIterator.SEG_CUBICTO:
          float[] curveEnd = Arrays.copyOfRange(coordinates, 4, 6);

          // Compute the position of the shape.
          float curveMinX = MathUtils.round(pathPosition[0], roundingPrecision);
          float curveMinY = MathUtils.round(pathPosition[1], roundingPrecision);
          float curveMaxX = MathUtils.round(curveEnd[0], roundingPrecision);
          float curveMaxY = MathUtils.round(curveEnd[1], roundingPrecision);
          PdfPosition curvePos = new PdfPosition(page, curveMinX, curveMinY, curveMaxX, curveMaxY);

          PdfShape curveShape = new PdfShape();
          curveShape.setPosition(curvePos);
          curveShape.setColor(color);

          this.parser.handlePdfShape(pdf, page, curveShape);

          this.parser.setLinePathPosition(curveEnd);
          break;
        case PathIterator.SEG_LINETO:
          float[] lineEnd = Arrays.copyOf(coordinates, 2);

          // Compute the position of the shape.
          float lineMinX = MathUtils.round(pathPosition[0], roundingPrecision);
          float lineMinY = MathUtils.round(pathPosition[1], roundingPrecision);
          float lineMaxX = MathUtils.round(lineEnd[0], roundingPrecision);
          float lineMaxY = MathUtils.round(lineEnd[1], roundingPrecision);
          PdfPosition linePos = new PdfPosition(page, lineMinX, lineMinY, lineMaxX, lineMaxY);

          PdfShape lineShape = new PdfShape();
          lineShape.setPosition(linePos);
          lineShape.setColor(color);
          this.parser.handlePdfShape(pdf, page, lineShape);

          this.parser.setLinePathPosition(lineEnd);
          break;
        case PathIterator.SEG_MOVETO:
          float[] pos = Arrays.copyOf(coordinates, 2);
          this.parser.setLinePathLastMoveToPosition(pos);
          this.parser.setLinePathPosition(pos);
          break;
        case PathIterator.SEG_QUADTO:
          float[] quadEnd = Arrays.copyOfRange(coordinates, 2, 4);

          // Compute the position of the shape.
          float quadMinX = MathUtils.round(pathPosition[0], roundingPrecision);
          float quadMinY = MathUtils.round(pathPosition[1], roundingPrecision);
          float quadMaxX = MathUtils.round(quadEnd[0], roundingPrecision);
          float quadMaxY = MathUtils.round(quadEnd[1], roundingPrecision);
          PdfPosition quadPos = new PdfPosition(page, quadMinX, quadMinY, quadMaxX, quadMaxY);

          PdfShape quadShape = new PdfShape();
          quadShape.setPosition(quadPos);
          quadShape.setColor(color);
          this.parser.handlePdfShape(pdf, page, quadShape);

          this.parser.setLinePathPosition(quadEnd);
          break;
        default:
          break;
      }
      prevSegment = currentSegment;
    }
    path.reset();

  }

  @Override
  public String getName() {
    return "S";
  }
}
