package ad.freiburg.pdfparser.serializer;

import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.B;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.CHARACTER;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.CHARACTERS;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.COLOR;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.COLORS;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.DEFAULT_ENCODING;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.DOCUMENT;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.FIGURE;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.FIGURES;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.FONT;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.FONTS;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.FONTSIZE;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.G;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.HEIGHT;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.ID;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.IS_BOLD;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.IS_ITALIC;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.IS_TYPE3;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.MAX_X;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.MAX_Y;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.MIN_X;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.MIN_Y;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.NAME;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.PAGE;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.PAGES;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.POSITION;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.POSITIONS;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.R;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.SHAPE;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.SHAPES;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.TEXT;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.WIDTH;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringEscapeUtils;
import ad.freiburg.pdfparser.exception.PdfSerializerException;
import ad.freiburg.pdfparser.model.PdfCharacter;
import ad.freiburg.pdfparser.model.PdfColor;
import ad.freiburg.pdfparser.model.PdfDocument;
import ad.freiburg.pdfparser.model.PdfElementType;
import ad.freiburg.pdfparser.model.PdfFigure;
import ad.freiburg.pdfparser.model.PdfFont;
import ad.freiburg.pdfparser.model.PdfFontFace;
import ad.freiburg.pdfparser.model.PdfPage;
import ad.freiburg.pdfparser.model.PdfPosition;
import ad.freiburg.pdfparser.model.PdfShape;
import ad.freiburg.pdfparser.model.Rectangle;


/**
 * An implementation of {@link PdfXmlSerializer} that serializes a PDF document in XML format.
 *
 * @author Claudius Korzen
 */
public class PdfXmlSerializer implements PdfSerializer {
  /**
   * The indentation length.
   */
  protected static final int INDENT_LENGTH = 2;

  /**
   * The line delimiter to use on joining the individual lines.
   */
  protected static final String LINE_DELIMITER = System.lineSeparator();

  // ==============================================================================================

  /**
   * The fonts of the PDF elements which were in fact serialized.
   */
  protected Set<PdfFont> usedFonts;

  /**
   * The colors of the PDF elements which were in fact serialized.
   */
  protected Set<PdfColor> usedColors;

  // ==============================================================================================
  // Constructors.

  /**
   * Creates a new serializer that serializes a PDF document in XML format.
   */
  public PdfXmlSerializer() {
    this.usedFonts = new HashSet<>();
    this.usedColors = new HashSet<>();
  }

  // ==============================================================================================

  @Override
  public byte[] serialize(PdfDocument pdf) throws PdfSerializerException {
    return serialize(pdf, PdfElementType.getTypes());
  }

  @Override
  public byte[] serialize(PdfDocument pdf, Collection<PdfElementType> types)
          throws PdfSerializerException {
    if (pdf == null) {
      return null;
    }

    // The current indentation level.
    int level = 0;

    // The individual lines of the serialization.
    List<String> resultLines = new ArrayList<>();

    // Start the XML document.
    resultLines.add(start(DOCUMENT, level));

    // Serialize the PDF elements.
    List<String> elementsLines = serializePdfElements(level + 1, pdf, types);
    if (elementsLines != null && !elementsLines.isEmpty()) {
      resultLines.addAll(elementsLines);
    }

    // Serialize the used fonts.
    List<String> fontsLines = serializeFonts(level + 2, this.usedFonts);
    if (fontsLines != null && !fontsLines.isEmpty()) {
      resultLines.add(start(FONTS, level + 1));
      resultLines.addAll(fontsLines);
      resultLines.add(end(FONTS, level + 1));
    }

    // Serialize the used colors.
    List<String> colorsLines = serializeColors(level + 2, this.usedColors);
    if (colorsLines != null && !colorsLines.isEmpty()) {
      resultLines.add(start(COLORS, level + 1));
      resultLines.addAll(colorsLines);
      resultLines.add(end(COLORS, level + 1));
    }

    // Serialize the metadata of the pages.
    List<String> pagesLines = serializePages(level + 2, pdf.getPages());
    if (pagesLines != null && !pagesLines.isEmpty()) {
      resultLines.add(start(PAGES, level + 1));
      resultLines.addAll(pagesLines);
      resultLines.add(end(PAGES, level + 1));
    }

    // End the XML document.
    resultLines.add(end(DOCUMENT, level));

    try {
      return String.join(LINE_DELIMITER, resultLines).getBytes(DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new PdfSerializerException("Couldn't serialize the PDF document.", e);
    }
  }

  // ==============================================================================================

  /**
   * Serializes the PDF elements of the given types.
   * 
   * @param pdf   The PDF document to process.
   * @param types The types of elements to serialize.
   * 
   * @return A list of text lines representing the serialized elements.
   */
  protected List<String> serializePdfElements(int level, PdfDocument pdf,
          Collection<PdfElementType> types) {
    List<String> lines = new ArrayList<>();
    for (PdfElementType type : types) {
      switch (type) {
        case CHARACTERS:
          lines.addAll(serializeCharacters(level, pdf));
          break;
        case FIGURES:
          lines.addAll(serializeFigures(level, pdf));
          break;
        case SHAPES:
          lines.addAll(serializeShapes(level, pdf));
          break;
        default:
          break;
      }
    }
    return lines;
  }

  // ==============================================================================================

  /**
   * Serializes the characters of the given PDF document.
   * 
   * @param level The current indentation level.
   * @param pdf   The PDF document to process.
   * 
   * @return A list of text lines representing the serialized characters.
   */
  protected List<String> serializeCharacters(int level, PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      result.add(start(CHARACTERS, level));
      for (PdfPage page : pdf.getPages()) {
        for (PdfCharacter character : page.getCharacters()) {
          List<String> characterLines = serializeCharacter(level + 1, character);
          if (characterLines != null) {
            result.addAll(characterLines);
          }
        }
      }
      result.add(end(CHARACTERS, level));
    }

    return result;
  }

  /**
   * Serializes the given character.
   * 
   * @param level     The current indentation level.
   * @param character The character to serialize.
   *
   * @return A list of text lines representing the serialized characters.
   */
  protected List<String> serializeCharacter(int level, PdfCharacter character) {
    if (character == null) {
      return null;
    }

    List<String> charLines = new ArrayList<>();
    // Serialize the position.
    List<String> serialized = serializePosition(level + 1, character.getPosition());
    if (serialized != null) {
      charLines.addAll(serialized);
    }

    // Serialize the font face.
    PdfFontFace fontFace = character.getFontFace();
    if (fontFace != null) {
      PdfFont font = fontFace.getFont();
      float size = fontFace.getFontSize();
      if (font != null) {
        String fontId = font.getId();
        if (fontId != null && size > 0) {
          charLines.add(start(FONT, level + 1));
          charLines.add(start(ID, level + 2) + text(fontId) + end(ID));
          charLines.add(start(FONTSIZE, level + 2) + text(size) + end(FONTSIZE));
          charLines.add(end(FONT, level + 1));
          this.usedFonts.add(font);
        }
      }
    }

    // Serialize the color.
    PdfColor color = character.getColor();
    if (color != null) {
      String colorId = color.getId();
      if (colorId != null) {
        charLines.add(start(COLOR, level + 1));
        charLines.add(start(ID, level + 2) + text(colorId) + end(ID));
        charLines.add(end(COLOR, level + 1));
        this.usedColors.add(color);
      }
    }

    // Serialize the text.
    String text = character.getText();
    if (text != null) {
      charLines.add(start(TEXT, level + 1) + text(text) + end(TEXT));
    }

    List<String> result = new ArrayList<>();
    if (charLines != null && !charLines.isEmpty()) {
      result.add(start(CHARACTER, level));
      result.addAll(charLines);
      result.add(end(CHARACTER, level));
    }

    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the figures of the given PDF document.
   * 
   * @param level The current indentation level.
   * @param pdf   The PDF document to process.
   * 
   * @return A list of text lines representing the serialized figures.
   */
  protected List<String> serializeFigures(int level, PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      result.add(start(FIGURES, level));
      for (PdfPage page : pdf.getPages()) {
        for (PdfFigure figure : page.getFigures()) {
          List<String> figureLines = serializeFigure(level + 1, figure);
          if (figureLines != null) {
            result.addAll(figureLines);
          }
        }
      }
      result.add(end(FIGURES, level));
    }

    return result;
  }

  /**
   * Serializes the given figure.
   * 
   * @param level  The current indentation level.
   * @param figure The figure to serialize.
   *
   * @return A list of text lines representing the serialized figure.
   */
  protected List<String> serializeFigure(int level, PdfFigure figure) {
    if (figure == null) {
      return null;
    }

    List<String> figureLines = new ArrayList<>();

    // Serialize the position.
    List<String> serialized = serializePosition(level + 1, figure.getPosition());
    if (serialized != null) {
      figureLines.addAll(serialized);
    }

    List<String> result = new ArrayList<>();
    if (figureLines != null && !figureLines.isEmpty()) {
      result.add(start(FIGURE, level));
      result.addAll(figureLines);
      result.add(end(FIGURE, level));
    }
    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the shapes of the given PDF document.
   * 
   * @param level The current indentation level.
   * @param pdf   The PDF document to process.
   * 
   * @return A list of text lines representing the text lines of the serialized shapes.
   */
  protected List<String> serializeShapes(int level, PdfDocument pdf) {
    List<String> result = new ArrayList<>();

    if (pdf != null) {
      result.add(start(SHAPES, level));
      for (PdfPage page : pdf.getPages()) {
        for (PdfShape shape : page.getShapes()) {
          List<String> shapeLines = serializeShape(level + 1, shape);
          if (shapeLines != null) {
            result.addAll(shapeLines);
          }
        }
      }
      result.add(end(SHAPES, level));
    }

    return result;
  }

  /**
   * Serializes the given shape.
   * 
   * @param level The current indentation level.
   * @param shape The shape to serialize.
   *
   * @return A list of text lines representing the serialized shape.
   */
  protected List<String> serializeShape(int level, PdfShape shape) {
    if (shape == null) {
      return null;
    }

    List<String> shapeLines = new ArrayList<>();

    // Serialize the position of the shape, if there is any.
    List<String> serialized = serializePosition(level + 1, shape.getPosition());
    if (serialized != null) {
      shapeLines.addAll(serialized);
    }

    // Serialize the color of the shape, if there is any.
    PdfColor color = shape.getColor();
    if (color != null) {
      String colorId = color.getId();
      if (colorId != null) {
        shapeLines.add(start(COLOR, level + 1));
        shapeLines.add(start(ID, level + 2) + text(colorId) + end(ID));
        shapeLines.add(end(COLOR, level + 1));
        this.usedColors.add(color);
      }
    }

    // Wrap the serialized lines with a tag that describes a shape.
    List<String> result = new ArrayList<>();
    if (shapeLines != null && !shapeLines.isEmpty()) {
      result.add(start(SHAPE, level));
      result.addAll(shapeLines);
      result.add(end(SHAPE, level));
    }
    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the given PDF position.
   * 
   * @param level    The current indentation level.
   * @param position The position to serialize.
   * 
   * @return A list of text lines representing the serialized position.
   */
  protected List<String> serializePosition(int level, PdfPosition position) {
    if (position == null) {
      return null;
    }

    List<String> result = new ArrayList<>();

    PdfPage page = position.getPage();
    int pageNumber = page.getPageNumber();
    Rectangle rect = position.getRectangle();

    if (pageNumber > 0 && rect != null) {
      result.add(start(POSITION, level));
      result.add(start(PAGE, level + 1) + text(pageNumber) + end(PAGE));
      result.add(start(MIN_X, level + 1) + text(rect.getMinX()) + end(MIN_X));
      result.add(start(MIN_Y, level + 1) + text(rect.getMinY()) + end(MIN_Y));
      result.add(start(MAX_X, level + 1) + text(rect.getMaxX()) + end(MAX_X));
      result.add(start(MAX_Y, level + 1) + text(rect.getMaxY()) + end(MAX_Y));
      result.add(end(POSITION, level));
    }

    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the given fonts.
   *
   * @param level The current indentation level.
   * @param fonts The fonts to serialize.
   * 
   * @return A list of text lines representing the serialized fonts.
   */
  protected List<String> serializeFonts(int level, Set<PdfFont> fonts) {
    List<String> result = new ArrayList<>();
    if (fonts != null) {
      for (PdfFont font : fonts) {
        List<String> fontLines = serializeFont(level, font);
        if (fontLines != null) {
          result.addAll(fontLines);
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given font.
   * 
   * @param level The current indentation level.
   * @param font  The font to serialize.
   * 
   * @return A list of strings representing the serialized font.
   */
  protected List<String> serializeFont(int level, PdfFont font) {
    if (font == null) {
      return null;
    }
    List<String> result = new ArrayList<>();
    
    result.add(start(FONT, level));
    
    String fontId = font.getId();
    if (fontId != null) {
      result.add(start(ID, level + 1) + text(fontId) + end(ID));
    }

    String fontName = font.getNormalizedName();
    if (fontName != null) {
      result.add(start(NAME, level + 1) + text(fontName) + end(NAME));
    }
    
    boolean isBold = font.isBold();
    result.add(start(IS_BOLD, level + 1) + text(isBold) + end(IS_BOLD));
      
    boolean isItalic = font.isItalic();
    result.add(start(IS_ITALIC, level + 1) + text(isItalic) + end(IS_ITALIC));

    boolean isType3 = font.isType3Font();
    result.add(start(IS_TYPE3, level + 1) + text(isType3) + end(IS_TYPE3));

    result.add(end(FONT, level));
    
    return result;
  }

  // ==============================================================================================
  // Methods to serialize colors.

  /**
   * Serializes the given colors.
   * 
   * @param level  The current indentation level.
   * @param colors The colors to serialize.
   * 
   * @return A list of text lines representing the serialized colors.
   */
  protected List<String> serializeColors(int level, Set<PdfColor> colors) {
    List<String> result = new ArrayList<>();

    if (colors != null) {
      for (PdfColor color : colors) {
        if (color != null) {
          List<String> colorLines = serializeColor(level, color);
          if (colorLines != null) {
            result.addAll(colorLines);
          }
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given color.
   * 
   * @param level The current indentation level.
   * @param color The color to serialize.
   * 
   * @return A list of text lines representing the serialized color.
   */
  protected List<String> serializeColor(int level, PdfColor color) {
    if (color == null) {
      return null;
    }

    List<String> result = new ArrayList<>();
    int[] rgb = color.getRGB();

    if (rgb != null && rgb.length == 3) {
      result.add(start(COLOR, level));
      result.add(start(ID, level + 1) + text(color.getId()) + end(ID));
      result.add(start(R, level + 1) + text(rgb[0]) + end(R));
      result.add(start(G, level + 1) + text(rgb[1]) + end(G));
      result.add(start(B, level + 1) + text(rgb[2]) + end(B));
      result.add(end(COLOR, level));
    }
    return result;
  }

  // ==============================================================================================
  // Methods to serialize the page information.

  /**
   * Serializes the metadata of the given pages.
   * 
   * @param level The current indentation level.
   * @param pages The pages to serialize.
   * 
   * @return A list of text lines representing the serialized pages.
   */
  protected List<String> serializePages(int level, List<PdfPage> pages) {
    List<String> result = new ArrayList<>();

    if (pages != null) {
      for (PdfPage page : pages) {
        if (page != null) {
          List<String> pageLines = serializePage(level, page);
          if (pageLines != null) {
            result.addAll(pageLines);
          }
        }
      }
    }
    return result;
  }

  /**
   * Serializes the metadata of the given page.
   * 
   * @param level The current indentation level.
   * @param page  The page to serialize.
   * 
   * @return A list of text lines representing the serialized page.
   */
  protected List<String> serializePage(int level, PdfPage page) {
    if (page == null) {
      return null;
    }

    List<String> result = new ArrayList<>();
    result.add(start(PAGE, level));
    result.add(start(ID, level + 1) + text(page.getPageNumber()) + end(ID));
    result.add(start(WIDTH, level + 1) + text(page.getWidth()) + end(WIDTH));
    result.add(start(HEIGHT, level + 1) + text(page.getHeight()) + end(HEIGHT));
    result.add(end(PAGE, level));

    return result;
  }

  // ==============================================================================================

  /**
   * Wraps the given text in an XML start tag and indents it by the given indentation level.
   * 
   * @param text  The text to wrap.
   * @param level The indentation level.
   * 
   * @return The given text wrapped in an XML start tag, indented by the given indentation level.
   */
  protected String start(String text, int level) {
    String indent = repeat(" ", level * INDENT_LENGTH);
    return indent + "<" + text + ">";
  }

  /**
   * Wraps the given text in an XML end tag.
   * 
   * @param text The text to wrap.
   * 
   * @return The given text wrapped in an XML end tag.
   */
  protected String end(String text) {
    return end(text, 0);
  }

  /**
   * Wraps the given text in an XML end tag and indents it by the given indentation level.
   * 
   * @param text  The text to wrap.
   * @param level The indentation level.
   * 
   * @return The given text wrapped in an XML end tag, indented by the given indentation level.
   */
  protected String end(String text, int level) {
    String indent = repeat(" ", level * INDENT_LENGTH);
    return indent + "</" + text + ">";
  }

  /**
   * Transform the given object to an XML escaped string.
   * 
   * @param obj The object to process.
   * 
   * @return The XML escaped string.
   */
  protected String text(Object obj) {
    return text(obj, 0);
  }

  /**
   * Transform the given object to an XML escaped string and indents it by the given indentation
   * level.
   * 
   * @param obj   The object to process.
   * @param level The indentation level.
   *
   * @return The XML escaped string, indented by the given indentation level.
   */
  protected String text(Object obj, int level) {
    String indent = repeat(" ", level * INDENT_LENGTH);
    String text = StringEscapeUtils.escapeXml11(obj.toString());
    return indent + text;
  }

  // ==============================================================================================

  /**
   * Repeats the given string $repeats-times.
   * 
   * @param string  The string to repeat.
   * @param repeats The number of repeats.
   * @return The string containing the given string $repeats-times.
   */
  public static String repeat(String string, int repeats) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < repeats; i++) {
      sb.append(string);
    }
    return sb.toString();
  }
}
