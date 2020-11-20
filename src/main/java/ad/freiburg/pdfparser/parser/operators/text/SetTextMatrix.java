package ad.freiburg.pdfparser.parser.operators.text;

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
 * Tm: Set text matrix and the text line matrix.
 * 
 * @author Claudius Korzen
 */
public class SetTextMatrix extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    if (args.size() < 6) {
      throw new MissingOperandException(op, args);
    }

    float a = ((COSNumber) args.get(0)).floatValue();
    float b = ((COSNumber) args.get(1)).floatValue();
    float c = ((COSNumber) args.get(2)).floatValue();
    float d = ((COSNumber) args.get(3)).floatValue();
    float e = ((COSNumber) args.get(4)).floatValue();
    float f = ((COSNumber) args.get(5)).floatValue();

    // Set both matrices to
    // [ a b 0
    // c d 0
    // e f 1 ]

    Matrix matrix = new Matrix(a, b, c, d, e, f);
    this.parser.setTextMatrix(matrix);
    this.parser.setTextLineMatrix(matrix.clone());
  }

  @Override
  public String getName() {
    return "Tm";
  }
}
