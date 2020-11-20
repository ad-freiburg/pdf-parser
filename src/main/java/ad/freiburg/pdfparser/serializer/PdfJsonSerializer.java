package ad.freiburg.pdfparser.serializer;

import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.B;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.CHARACTER;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.CHARACTERS;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.COLOR;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.COLORS;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.DEFAULT_ENCODING;
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
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.R;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.SHAPE;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.SHAPES;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.TEXT;
import static ad.freiburg.pdfparser.serializer.PdfSerializerConstants.WIDTH;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
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
 * A serializer to serialize a document in JSON format.
 *
 * @author Claudius Korzen
 */
public class PdfJsonSerializer implements PdfSerializer {
  /**
   * The indentation length.
   */
  protected static final int INDENT_LENGTH = 2;

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
   * Creates a new serializer that serializes a PDF document in JSON format.
   */
  public PdfJsonSerializer() {
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

    // The JSON object to serialize the PDF document.
    JSONObject json = new JSONObject();

    // Serialize the PDF elements.
    serializePdfElements(pdf, json, types);

    // Serialize the used fonts.
    JSONArray fontsJson = serializeFonts(this.usedFonts);
    if (fontsJson != null && fontsJson.length() > 0) {
      json.put(FONTS, fontsJson);
    }

    // Serialize the used colors.
    JSONArray colorsJson = serializeColors(this.usedColors);
    if (colorsJson != null && colorsJson.length() > 0) {
      json.put(COLORS, colorsJson);
    }

    // Serialize the metadata of the pages.
    JSONArray pagesJson = serializePages(pdf.getPages());
    if (pagesJson != null && pagesJson.length() > 0) {
      json.put(PAGES, pagesJson);
    }

    try {
      // Serialize the JSON object.
      return json.toString(INDENT_LENGTH).getBytes(DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new PdfSerializerException("Couldn't serialize the PDF document.", e);
    }
  }

  // ==============================================================================================

  /**
   * Serializes the PDF elements of the given types and writes them to the given JSON object.
   * 
   * @param pdf   The PDF document to process.
   * @param json  The JSON object to write the serialization to.
   * @param types The types of elements to serialize.
   */
  public void serializePdfElements(PdfDocument pdf, JSONObject json,
          Collection<PdfElementType> types) {
    for (PdfElementType type : types) {
      switch (type) {
        case CHARACTERS:
          serializeCharacters(pdf, json);
          break;
        case FIGURES:
          serializeFigures(pdf, json);
          break;
        case SHAPES:
          serializeShapes(pdf, json);
          break;
        default:
          break;
      }
    }
  }

  // ==============================================================================================

  /**
   * Serializes the characters of the given PDF document and writes them to the given JSON object.
   * 
   * @param pdf  The PDF document to process.
   * @param json The JSON object to write the serialization to.
   */
  protected void serializeCharacters(PdfDocument pdf, JSONObject json) {
    JSONArray result = new JSONArray();

    if (pdf != null) {
      for (PdfPage page : pdf.getPages()) {
        for (PdfCharacter character : page.getCharacters()) {
          JSONObject characterJson = serializeCharacter(character);
          if (characterJson != null) {
            result.put(characterJson);
          }
        }
      }
    }

    json.put(CHARACTERS, result);
  }

  /**
   * Serializes the given character.
   * 
   * @param character The character to serialize.
   *
   * @return A JSON object representing the serialized character.
   */
  protected JSONObject serializeCharacter(PdfCharacter character) {
    if (character == null) {
      return null;
    }

    JSONObject charJson = new JSONObject();

    // Serialize the position.
    JSONObject serialized = serializePosition(character.getPosition());
    if (serialized != null && serialized.length() > 0) {
      charJson.put(POSITION, serialized);
    }

    // Serialize the font face.
    PdfFontFace fontFace = character.getFontFace();
    if (fontFace != null) {
      PdfFont font = fontFace.getFont();
      float size = fontFace.getFontSize();
      if (font != null) {
        String fontId = font.getId();
        if (fontId != null && size > 0) {
          JSONObject fontJson = new JSONObject();
          fontJson.put(ID, fontId);
          fontJson.put(FONTSIZE, size);
          charJson.put(FONT, fontJson);
          this.usedFonts.add(font);
        }
      }
    }

    // Serialize the color.
    PdfColor color = character.getColor();
    if (color != null) {
      String colorId = color.getId();
      if (colorId != null) {
        JSONObject colorJson = new JSONObject();
        colorJson.put(ID, colorId);
        charJson.put(COLOR, colorJson);
        this.usedColors.add(color);
      }
    }

    // Serialize the text.
    String text = character.getText();
    if (text != null) {
      charJson.put(TEXT, text);
    }

    JSONObject result = new JSONObject();
    if (charJson != null && charJson.length() > 0) {
      result.put(CHARACTER, charJson);
    }

    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the figures of the given PDF document and writes them to the given JSON object.
   * 
   * @param pdf  The PDF document to process.
   * @param json The JSON object to write the serialization to.
   */
  protected void serializeFigures(PdfDocument pdf, JSONObject json) {
    JSONArray result = new JSONArray();

    if (pdf != null) {
      for (PdfPage page : pdf.getPages()) {
        for (PdfFigure figure : page.getFigures()) {
          JSONObject figureJson = serializeFigure(figure);
          if (figureJson != null) {
            result.put(figureJson);
          }
        }
      }
    }
    
    json.put(FIGURES, result);
  }

  /**
   * Serializes the given figure.
   * 
   * @param character The figure to serialize.
   *
   * @return A JSON object representing the serialized figure.
   */
  protected JSONObject serializeFigure(PdfFigure figure) {
    if (figure == null) {
      return null;
    }

    JSONObject figureJson = new JSONObject();

    // Serialize the position.
    JSONObject serialized = serializePosition(figure.getPosition());
    if (serialized != null && serialized.length() > 0) {
      figureJson.put(POSITION, serialized);
    }

    JSONObject result = new JSONObject();
    if (figureJson != null && figureJson.length() > 0) {
      result.put(FIGURE, figureJson);
    }

    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the shapes of the given PDF document and writes them to the given JSON object.
   * 
   * @param pdf  The PDF document to process.
   * @param json The JSON object to write the serialization to.
   */
  protected void serializeShapes(PdfDocument pdf, JSONObject json) {
    JSONArray result = new JSONArray();

    if (pdf != null) {
      for (PdfPage page : pdf.getPages()) {
        for (PdfShape shape : page.getShapes()) {
          JSONObject shapeJson = serializeShape(shape);
          if (shapeJson != null) {
            result.put(shapeJson);
          }
        }
      }
    }

    json.put(SHAPES, result);
  }

  /**
   * Serializes the given shape.
   * 
   * @param shape The shape to serialize.
   *
   * @return A JSON object representing the serialized shape.
   */
  protected JSONObject serializeShape(PdfShape shape) {
    JSONObject shapeJson = new JSONObject();

    if (shape != null) {
      // Serialize the position.
      JSONObject serialized = serializePosition(shape.getPosition());
      if (serialized != null && serialized.length() > 0) {
        shapeJson.put(POSITION, serialized);
      }

      // Serialize the color.
      PdfColor color = shape.getColor();
      if (color != null) {
        String colorId = color.getId();
        if (colorId != null) {
          JSONObject colorJson = new JSONObject();
          colorJson.put(ID, colorId);
          shapeJson.put(COLOR, colorJson);
          this.usedColors.add(color);
        }
      }
    }

    JSONObject result = new JSONObject();
    if (shapeJson != null && shapeJson.length() > 0) {
      result.put(SHAPE, shapeJson);
    }

    return result;
  }

  // ==============================================================================================

  /**
   * Serializes the given PDF position.
   * 
   * @param position The position to serialize.
   * 
   * @return A JSON object representing the serialized position.
   */
  protected JSONObject serializePosition(PdfPosition position) {
    if (position == null) {
      return null;
    }

    JSONObject positionJson = new JSONObject();

    PdfPage page = position.getPage();
    int pageNumber = page.getPageNumber();
    Rectangle rect = position.getRectangle();

    if (pageNumber > 0 && rect != null) {
      positionJson.put(PAGE, pageNumber);

      // If we pass primitive floats here, the values would be casted to
      // double values (yielding in inaccurate numbers). So transform the
      // values to Float objects.
      positionJson.put(MIN_X, Float.valueOf(rect.getMinX()));
      positionJson.put(MIN_Y, Float.valueOf(rect.getMinY()));
      positionJson.put(MAX_X, Float.valueOf(rect.getMaxX()));
      positionJson.put(MAX_Y, Float.valueOf(rect.getMaxY()));
    }

    return positionJson;
  }

  // ==============================================================================================

  /**
   * Serializes the given fonts.
   * 
   * @param fonts The fonts to serialize.
   * 
   * @return A JSON array representing the serialized fonts.
   */
  protected JSONArray serializeFonts(Set<PdfFont> fonts) {
    JSONArray result = new JSONArray();
    if (fonts != null) {
      for (PdfFont font : fonts) {
        JSONObject fontJson = serializeFont(font);
        if (fontJson != null && fontJson.length() > 0) {
          result.put(fontJson);
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given font.
   * 
   * @param font The font to serialize.
   * 
   * @return A JSON object representing the serialized font.
   */
  protected JSONObject serializeFont(PdfFont font) {
    JSONObject fontJson = new JSONObject();
    if (font != null) {
      String fontId = font.getId();
      if (fontId != null) {
        fontJson.put(ID, fontId);
      }

      String name = font.getNormalizedName();
      if (name != null) {
        fontJson.put(NAME, name);
      }
        
      boolean isBold = font.isBold();
      fontJson.put(IS_BOLD, isBold);

      boolean isItalic = font.isItalic();
      fontJson.put(IS_ITALIC, isItalic);

      boolean isType3 = font.isType3Font();
      fontJson.put(IS_TYPE3, isType3);
    }
    return fontJson;
  }

  // ==============================================================================================
  // Methods to serialize colors.

  /**
   * Serializes the given colors.
   * 
   * @param colors The colors to serialize.
   * 
   * @return A JSON array representing the serialized fonts.
   */
  protected JSONArray serializeColors(Set<PdfColor> colors) {
    JSONArray result = new JSONArray();

    if (colors != null) {
      for (PdfColor color : colors) {
        if (color != null) {
          JSONObject colorJson = serializeColor(color);
          if (colorJson != null && colorJson.length() > 0) {
            result.put(colorJson);
          }
        }
      }
    }
    return result;
  }

  /**
   * Serializes the given color.
   * 
   * @param color The color to serialize.
   * 
   * @return A JSON object representing the serialized color.
   */
  protected JSONObject serializeColor(PdfColor color) {
    JSONObject colorJson = new JSONObject();
    if (color != null) {
      String colorId = color.getId();
      int[] rgb = color.getRGB();

      if (colorId != null && rgb != null && rgb.length == 3) {
        colorJson.put(ID, colorId);
        colorJson.put(R, rgb[0]);
        colorJson.put(G, rgb[1]);
        colorJson.put(B, rgb[2]);
      }
    }
    return colorJson;
  }

  // ==============================================================================================
  // Methods to serialize the metadata of pages.

  /**
   * Serializes the given pages.
   * 
   * @param pages The pages to serialize.
   * 
   * @return A JSON array representing the serialized pages.
   */
  protected JSONArray serializePages(List<PdfPage> pages) {
    JSONArray result = new JSONArray();

    if (pages != null) {
      for (PdfPage page : pages) {
        if (page != null) {
          JSONObject pageJson = serializePage(page);
          if (pageJson != null && pageJson.length() > 0) {
            result.put(pageJson);
          }
        }
      }
    }

    return result;
  }

  /**
   * Serializes the given page.
   * 
   * @param page The page to serialize.
   * 
   * @return A JSON object representing the serialized page.
   */
  protected JSONObject serializePage(PdfPage page) {
    JSONObject pageJson = new JSONObject();
    if (page != null) {
      pageJson.put(ID, page.getPageNumber());
      pageJson.put(WIDTH, page.getWidth());
      pageJson.put(HEIGHT, page.getHeight());
    }
    return pageJson;
  }
}
