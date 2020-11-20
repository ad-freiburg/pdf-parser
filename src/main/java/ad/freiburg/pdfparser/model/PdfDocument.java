package ad.freiburg.pdfparser.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A PDF document.
 * 
 * @author Claudius Korzen
 */
public class PdfDocument {
  /**
   * The path to the PDF file.
   */
  protected Path path;

  /**
   * The pages of this PDF document.
   */
  protected List<PdfPage> pages;

  // ==============================================================================================

  /**
   * Creates a new PDF document.
   * 
   * @param path The path to the PDF file.
   */
  public PdfDocument(String path) {
    this(Paths.get(path));
  }

  /**
   * Creates a new PDF document.
   * 
   * @param path The path to the PDF file.
   */
  public PdfDocument(File path) {
    this(path.toPath());
  }

  /**
   * Creates a new PDF document.
   * 
   * @param path The path to the PDF file.
   */
  public PdfDocument(Path path) {
    this.pages = new ArrayList<>();
    this.path = path;
  }

  // ==============================================================================================

  /**
   * Returns the path to the PDF file.
   * 
   * @return The path to the PDF file.
   */
  public Path getPath() {
    return this.path;
  }

  /**
   * Sets the path to the PDF file.
   * 
   * @param path The path to the PDF file.
   */
  public void setPath(Path path) {
    this.path = path;
  }

  // ==============================================================================================

  /**
   * Returns the path to the PDF file.
   * 
   * @return The path to the PDF file.
   */
  public File getFile() {
    return this.path != null ? this.path.toFile() : null;
  }

  /**
   * Sets the path to the PDF file.
   * 
   * @param file The path to the PDF file.
   */
  public void setFile(File file) {
    this.path = file != null ? file.toPath() : null;
  }

  // ==============================================================================================

  /**
   * Returns the pages of this PDF document.
   * 
   * @return The pages of this PDF document.
   */
  public List<PdfPage> getPages() {
    return this.pages;
  }

  /**
   * Returns the first page of this PDF document.
   * 
   * @return The first page of this PDF document, or null if this document contains no pages.
   */
  public PdfPage getFirstPage() {
    if (this.pages == null || this.pages.isEmpty()) {
      return null;
    }
    return this.pages.get(0);
  }

  /**
   * Returns the last page of this PDF document.
   * 
   * @return The last page of this PDF document, or null if this document contains no pages.
   */
  public PdfPage getLastPage() {
    if (this.pages == null || this.pages.isEmpty()) {
      return null;
    }
    return this.pages.get(this.pages.size() - 1);
  }

  /**
   * Sets the pages of this PDF document.
   * 
   * @param pages The pages to set.
   */
  public void setPages(List<PdfPage> pages) {
    this.pages = pages;
  }

  /**
   * Adds the given page to this PDF document.
   * 
   * @param page The page to add.
   */
  public void addPage(PdfPage page) {
    this.pages.add(page);
  }

  // ==============================================================================================

  @Override
  public String toString() {
    return "PdfDocument(" + this.path + ")";
  }

  // ==============================================================================================

  @Override
  public boolean equals(Object other) {
    if (other instanceof PdfDocument) {
      PdfDocument otherDocument = (PdfDocument) other;

      EqualsBuilder builder = new EqualsBuilder();
      builder.append(getPath(), otherDocument.getPath());

      return builder.isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(getPath());
    return builder.hashCode();
  }
}
