package ad.freiburg.pdfparser.exception;

/**
 * The exception to throw on any errors while serializing a PDF file.
 * 
 * @author Claudius Korzen
 */
public class PdfSerializerException extends Exception {
  /**
   * The serial id.
   */
  protected static final long serialVersionUID = -1208363363395692674L;

  /**
   * Creates a new serializer exception.
   * 
   * @param message The error message to show when the exception was caught.
   */
  public PdfSerializerException(String message) {
    super(message);
  }

  /**
   * Creates a new serializer exception.
   * 
   * @param message The error message to show when the exception was caught.
   * @param cause   The cause of this exception (this can be used to trace the error).
   */
  public PdfSerializerException(String message, Throwable cause) {
    super(message, cause);
  }
}
