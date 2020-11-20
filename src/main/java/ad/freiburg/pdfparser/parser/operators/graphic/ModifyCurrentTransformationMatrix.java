package ad.freiburg.pdfparser.parser.operators.graphic;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.MissingOperandException;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.util.Matrix;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * cm: Modify the current transformation matrix (CTM) by concatenating the specified matrix.
 * Although the operands specify a matrix, they shall be written as six separate numbers, not as an
 * array.
 * 
 * @author Claudius Korzen.
 */
public class ModifyCurrentTransformationMatrix extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (args.size() < 6) {
      throw new MissingOperandException(op, args);
    }

    // Concatenate matrix to current transformation matrix
    float a = ((COSNumber) args.get(0)).floatValue();
    float b = ((COSNumber) args.get(1)).floatValue();
    float c = ((COSNumber) args.get(2)).floatValue();
    float d = ((COSNumber) args.get(3)).floatValue();
    float e = ((COSNumber) args.get(4)).floatValue();
    float f = ((COSNumber) args.get(5)).floatValue();

    Matrix matrix = new Matrix(a, b, c, d, e, f);

    this.parser.getCurrentTransformationMatrix().concatenate(matrix);
  }

  @Override
  public String getName() {
    return "cm";
  }
}
