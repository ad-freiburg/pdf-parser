package ad.freiburg.pdfparser.serializer;

import java.util.Collection;
import ad.freiburg.pdfparser.exception.PdfSerializerException;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfElementType;

/**
 * A serializer to serialize a PDF document in a specific format.
 *
 * @author Claudius Korzen
 */
public interface PdfSerializer {
  /**
   * Serializes *all* elements of the given PDF document.
   * 
   * @param pdf The PDF document to serialize.
   * 
   * @return The serialization as a byte array.
   * 
   * @throws PdfSerializationException If something went wrong on serializing the PDF document.
   */
  byte[] serialize(PdfDocument pdf) throws PdfSerializerException;

  /**
   * Serializes the elements with the given types of the given PDF document.
   * 
   * @param pdf   The PDF document to serialize.
   * @param types The types of elements to serialize from the PDF document.
   * 
   * @return The serialization as a byte array.
   * 
   * @throws PdfSerializationException If something went wrong on serializing the PDF document.
   */
  byte[] serialize(PdfDocument pdf, Collection<PdfElementType> types) throws PdfSerializerException;
}
