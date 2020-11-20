package ad.freiburg.pdfparser.parser.operators.graphic;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDGraphicsState;
import org.apache.pdfbox.util.Matrix;
import ad.freiburg.pdfparser.model.PdfColor;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfFigure;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.model.PdfPosition;
import ad.freiburg.pdfparser.model.PdfShape;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;
import ad.freiburg.pdfparser.utils.ColorUtils;
import ad.freiburg.pdfparser.utils.MathUtils;
import ad.freiburg.pdfparser.utils.PdfColorManager;

/**
 * Do: Invoke a named xobject.
 *
 * @author Claudius Korzen
 */
public class DrawObject extends OperatorProcessor {
  /**
   * The logger.
   */
  protected static Logger LOG = LogManager.getLogger(DrawObject.class);

  // ==============================================================================================

  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    // Get the PDXObject from the page resources.
    COSName name = (COSName) args.get(0);
    PDXObject xobject = this.parser.getResources().getXObject(name);

    // Check if the object represents a form object.
    if (xobject instanceof PDFormXObject) {
      PDFormXObject form = (PDFormXObject) xobject;

      this.parser.saveGraphicsState();

      // If there is an optional form matrix, we have to map the form space to the user space.
      Matrix matrix = form.getMatrix();
      if (matrix != null) {
        Matrix ctm = this.parser.getGraphicsState().getCurrentTransformationMatrix();
        Matrix xctm = matrix.multiply(ctm);
        this.parser.getGraphicsState().setCurrentTransformationMatrix(xctm);
      }

      // Clip to the form's BBox.
      if (form.getBBox() != null) {
        PDGraphicsState graphicsState = this.parser.getGraphicsState();
        PDRectangle bbox = form.getBBox();
        GeneralPath bboxPath = this.parser.transformedPDRectanglePath(bbox);
        graphicsState.intersectClippingPath(bboxPath);
      }

      // Parse the stream of the form.
      if (form.getCOSObject().getLength() > 0) {
        this.parser.processStream(pdf, page, form);
      }

      // Restore the graphics state.
      this.parser.restoreGraphicsState();

      return;
    }

    // Check if the object represents an image object.
    if (xobject instanceof PDImageXObject) {
      PDImageXObject image = (PDImageXObject) xobject;
      int width = image.getWidth();
      int height = image.getHeight();

      Matrix ctm = this.parser.getCurrentTransformationMatrix().clone();
      AffineTransform ctmAT = ctm.createAffineTransform();
      ctmAT.scale(1f / width, 1f / height);
      Matrix at = new Matrix(ctmAT);

      // Compute the position of the image.
      int precision = this.parser.getFloatingPointPrecision();
      float minX = MathUtils.round(ctm.getTranslateX(), precision);
      float minY = MathUtils.round(ctm.getTranslateY(), precision);
      float maxX = MathUtils.round(ctm.getTranslateX() + width * at.getScaleX(), precision);
      float maxY = MathUtils.round(ctm.getTranslateY() + width * at.getScaleY(), precision);
      PdfPosition position = new PdfPosition(page, minX, minY, maxX, maxY);

      // Check, if the image consists of only one color. If so, consider it as a shape.
      int[] colorRgb = ColorUtils.getExclusiveColor(image.getImage());

      if (colorRgb != null) {
        PdfColor color = PdfColorManager.getOrCreatePdfColor(colorRgb);
        LOG.debug("Consider the image as shape, as it solely consists of the color " + color + ".");

        // Consider the image as a shape.
        PdfShape shape = new PdfShape();
        shape.setPosition(position);
        shape.setColor(color);

        this.parser.handlePdfShape(pdf, page, shape);
      } else {
        // Consider the image as a normal figure.
        PdfFigure figure = new PdfFigure();
        figure.setPosition(position);

        this.parser.handlePdfFigure(pdf, page, figure);
      }
    }

    // Primarily, we handled a form object *always* as a figure. But that's wrong, because a form
    // can contain text (in a substream) which appears as "normal" body text in a PDF, see
    // KI_2018.pdf for an example. Here is the old, obsolete code for that:

    // if (xobject instanceof PDFormXObject) {
    // PDFormXObject form = (PDFormXObject) xobject;

    // // if there is an (optional) form matrix, we have to map the form space to the
    // // user space
    // Matrix matrix = form.getMatrix();

    // if (matrix != null) {
    // this.engine.getGraphicsState().getCurrentTransformationMatrix().concatenate(matrix);
    // }

    // float formWidth = form.getBBox().getWidth();
    // float formHeight = form.getBBox().getHeight();

    // Matrix ctm = this.engine.getGraphicsState().getCurrentTransformationMatrix().clone();

    // // TODO: Check if ur and ll are indeed ur and ll.
    // float minX = ctm.getTranslateX();
    // float minY = ctm.getTranslateY();
    // float maxX = minX + ctm.getScaleX() * formWidth;
    // float maxY = minY + ctm.getScaleY() * formHeight;

    // // Round the values.
    // minX = PdfActUtils.round(minX, FLOATING_NUMBER_PRECISION);
    // minY = PdfActUtils.round(minY, FLOATING_NUMBER_PRECISION);
    // maxX = PdfActUtils.round(maxX, FLOATING_NUMBER_PRECISION);
    // maxY = PdfActUtils.round(maxY, FLOATING_NUMBER_PRECISION);

    // Point ll = new Point(minX, minY);
    // Point ur = new Point(maxX, maxY);
    // Position position = new Position(page, ll, ur);

    // // TODO: A PDFormXObject isn't necessarily a figure (but can be).
    // Figure figure = new Figure();
    // figure.setPosition(position);
    // this.engine.handlePdfFigure(pdf, page, figure);
  }

  @Override
  public String getName() {
    return "Do";
  }
}
