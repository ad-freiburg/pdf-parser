package ad.freiburg.pdfparser.parser.operators.graphic;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.parser.operators.OperatorProcessor;

/**
 * gs: Set the specified parameters in the graphics state. dictName shall be the name of a graphics
 * state parameter dictionary in the ExtGState sub-dictionary of the current resource dictionary.
 * 
 * @author Claudius Korzen
 */
public class SetGraphicsStateParameters extends OperatorProcessor {
  @Override
  public void process(PdfDocument pdf, PdfPage page, Operator op, List<COSBase> args)
          throws IOException {
    // set parameters from graphics state parameter dictionary
    COSName dictName = (COSName) args.get(0);
    PDResources resources = this.parser.getResources();
    PDExtendedGraphicsState gs = resources.getExtGState(dictName);
    gs.copyIntoGraphicsState(this.parser.getGraphicsState());
  }

  @Override
  public String getName() {
    return "gs";
  }
}
