package ad.freiburg.pdfparser.exception;

/**
 * The exception to throw on any errors while parsing a PDF file.
 * 
 * @author Claudius Korzen
 */
public class PdfParserException extends Exception {
  /**
   * The serial id.
   */
  private static final long serialVersionUID = 8374410494551741319L;

  /**
   * Creates a new parser exception.
   * 
   * @param message The error message to show when the exception was caught.
   */
  public PdfParserException(String message) {
    super(message);
  }

  /**
   * Creates a new parser exception.
   * 
   * @param message The error message to show when the exception was caught.
   * @param cause   The cause of this exception (this can be used to trace the error).
   */
  public PdfParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
