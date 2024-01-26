package com.lauerbach.pdf;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;

import com.lauerbach.pdf.template.Template;

public class PdfHelper {

	private PDDocument doc;
	private PDDocumentInformation info;
	private PDPage page;
	private float pageHeight = 0;
	private PDPageContentStream contentStream;

	private String pdfFileName;
	private PDType1Font defaultFont;
	private Float defaultFontSize = 14f;

	static final String hex = "0123456789ABCDEF";
	static final HashMap<String, String> colorMap = new HashMap<String, String>();

	private float hexToColor(String substring) {
		return (float) (hex.indexOf(substring.charAt(0)) * 16 + hex.indexOf(substring.charAt(1))) * 1f / 255f;
	}

	private void setColor(String color) throws IOException {
		if (colorMap.containsKey(color)) {
			color = colorMap.get(color);
		}
		if (color == null || color.length() != 6) {
			contentStream.setStrokingColor(0, 0, 0);
			contentStream.setNonStrokingColor(0, 0, 0);
		} else {
			float colR = hexToColor(color.substring(0, 2));
			float colG = hexToColor(color.substring(2, 4));
			float colB = hexToColor(color.substring(4, 6));
			contentStream.setStrokingColor(colR, colG, colB);
			contentStream.setNonStrokingColor(colR, colG, colB);
		}
	}

	public PdfHelper(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public void startDoc() throws IOException {
		doc = new PDDocument();
		info = doc.getDocumentInformation();
		info.setAuthor("Thomas Lauerbach");
		doc.setDocumentInformation(info);
		this.defaultFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
		newPage();
	}

	public void newPage() throws IOException {
		if (page != null) {
			contentStream.close();
		}
		page = new PDPage(PDRectangle.A4);
		pageHeight = page.getMediaBox().getHeight();
		doc.addPage(page);
		contentStream = new PDPageContentStream(doc, page);
	}

	public void endDoc() throws IOException {
		contentStream.close();
		doc.save(new File(pdfFileName));
		doc.close();
	}

	public void open() throws IOException {
		Runtime.getRuntime().exec("okular " + pdfFileName);
	}

	public PrintedBounds printText(float offsetX, float offsetY, float relX, float relY, Float fontSize, String color,
			TextFormat textFormat, String value) throws IOException {
		if (fontSize == null) {
			fontSize = defaultFontSize;
		}
		if (value == null) {
			return null;
		}
		float x = offsetX + relX;
		float y = offsetY + relY;

		contentStream.beginText();
		setColor(color);

		contentStream.newLineAtOffset(x, pageHeight - y - fontSize);
		contentStream.setFont(defaultFont, fontSize);
		contentStream.showText(value);
		contentStream.endText();

		float stringWidth = defaultFont.getStringWidth(value) / 1000 * 14f;

		System.out.println("text " + x + " " + (pageHeight - y) + " " + value);

		return new PrintedBounds(relX, relY, relX + stringWidth, relY + fontSize);
	}

	public PrintedBounds printTextArea(float offsetX, float offsetY, float relX, float relY, float w, float h,
			Float fontSize, String color, TextFormat textFormat, String value) throws IOException {
		if (fontSize == null) {
			fontSize = defaultFontSize;
		}
		if (value == null) {
			return null;
		}
		float x = offsetX + relX;
		float y = offsetY + relY;

		float width = w;

		contentStream.beginText();

		setColor(color);

		contentStream.newLineAtOffset(x, pageHeight - y - fontSize);
		contentStream.setFont(defaultFont, fontSize);

		float printedHeight = 0;

		float LEADING = 1.5f * fontSize;
		List<String> lines = parseLines(value, defaultFont, fontSize, width);
		// contentStream.newLineAtOffset(x, y);
		for (String line : lines) {
			float charSpacing = 0;
			if (textFormat == TextFormat.block) {
				if (line.length() > 1) {
					float size = fontSize * defaultFont.getStringWidth(line) / 1000;
					float free = width - size;
					if (free > 0 && !lines.get(lines.size() - 1).equals(line)) {
						charSpacing = free / (line.length() - 1);
					}
				}
			}
			contentStream.setCharacterSpacing(charSpacing);
			contentStream.showText(line);
			contentStream.newLineAtOffset(x, -LEADING);
			printedHeight += LEADING;
		}
		contentStream.endText();

		System.out.println("textArea " + x + " " + (pageHeight - y) + " " + w + " " + h + " " + value);

		return new PrintedBounds(relX, relY, relX + width, relY + printedHeight);
	}

	private List<String> parseLines(String text, PDFont font, float fontSize, float width) throws IOException {
		List<String> lines = new ArrayList<String>();
		int lastSpace = -1;
		while (text.length() > 0) {
			int spaceIndex = text.indexOf(' ', lastSpace + 1);
			if (spaceIndex < 0)
				spaceIndex = text.length();
			String subString = text.substring(0, spaceIndex);
			float size = fontSize * font.getStringWidth(subString) / 1000;
			if (size > width) {
				if (lastSpace < 0) {
					lastSpace = spaceIndex;
				}
				subString = text.substring(0, lastSpace);
				lines.add(subString);
				text = text.substring(lastSpace).trim();
				lastSpace = -1;
			} else if (spaceIndex == text.length()) {
				lines.add(text);
				text = "";
			} else {
				lastSpace = spaceIndex;
			}
		}
		return lines;
	}

	public PrintedBounds printLine(float offsetX, float offsetY, float relX, float relY, float w, float h,
			float lineWidth, String color) throws IOException {
		float x = offsetX + relX;
		float y = offsetY + relY;

		setColor(color);
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(x, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y - h);
		contentStream.stroke();
		System.out.println("line " + x + " " + (h - y) + " " + w + " " + h);
		return new PrintedBounds(relX, relY - lineWidth / 2, relX + w, relY + h + lineWidth / 2);
	}

	public PrintedBounds printRectangle(float offsetX, float offsetY, float relX, float relY, float w, float h,
			float lineWidth, String color) throws IOException {
		float x = offsetX + relX;
		float y = offsetY + relY;
		setColor(color);
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(x, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y - h);
		contentStream.lineTo(x, pageHeight - y - h);
		contentStream.lineTo(x, pageHeight - y + lineWidth / 2);
		contentStream.stroke();
		System.out.println("rect " + x + " " + (h - y) + " " + w + " " + h);
		return new PrintedBounds(relX, relY, relX + w, relY + h);
	}

	public PrintedBounds printGroup(float offsetX, float offsetY, float relX, float relY, float w, float h,
			float lineWidth, String color) throws IOException {
		float x = offsetX + relX;
		float y = offsetY + relY;
		setColor(color);
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(x, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y - h);
		contentStream.lineTo(x, pageHeight - y - h);
		contentStream.lineTo(x, pageHeight - y + lineWidth / 2);
		contentStream.stroke();
		System.out.println("rect " + x + " " + (h - y) + " " + w + " " + h);
		return new PrintedBounds(relX, relY, relX + w, relY + h);
	}
	
	
	public static void main(String[] args) throws JAXBException, IOException {
		PdfHelper helper = new PdfHelper("test.pdf");

		Template t = Template.parse(new File("test.template.xml"));
		t.print(new PrintContext(helper, null));
		helper.open();

	}

	static {
		colorMap.put("black", "000000");
		colorMap.put("red", "FF0000");
		colorMap.put("green", "00FF00");
		colorMap.put("blue", "0000FF");
	}

}
