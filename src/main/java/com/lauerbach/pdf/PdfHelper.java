package com.lauerbach.pdf;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.lauerbach.pdf.template.Group;
import com.lauerbach.pdf.template.ListComponent;
import com.lauerbach.pdf.template.PrintComponent;

public class PdfHelper {

	enum ReportState {
		start, iterating, finish, breakPageAndContinue, nextPage;
	}

	private File pdfFile;
	private Object globalData;

	ListContext activeContext = null;
	HashMap<String, ListContext> contextMap = new HashMap<String, ListContext>();

	ReportState reportState = ReportState.start;

	private PDDocument doc;
	private PDDocumentInformation info;
	private PDPage page;
	private float pageHeight = 0;
	private float pageWidth = 0;
	private PDPageContentStream contentStream;

	private PDType1Font defaultFont;
	private Float defaultFontSize = 14f;

	static final String hex = "0123456789ABCDEF";
	static final HashMap<String, String> colorMap = new HashMap<String, String>();

	public PdfHelper(File file, Object globalData) {
		this.pdfFile = file;
		this.globalData = globalData;
	}

	// *************** Document handling ***************

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
		pageWidth = page.getMediaBox().getWidth();
		doc.addPage(page);
		contentStream = new PDPageContentStream(doc, page);

	}

	public void endDoc() throws IOException {
		contentStream.close();
		doc.save(pdfFile);
		doc.close();
	}

	// *************** Data handling ***************

	private Object getRecursiveValue(ListContext componentContext, Object data, String expression) {
		int pos = expression.indexOf('.');
		String name;
		String subExpression = null;
		if (pos < 0) {
			name = expression;
		} else if (pos == 0) {
			// loopks like an error
			return null;
		} else {
			name = expression.substring(0, pos);
			subExpression = expression.substring(pos + 1);
		}
		try {
			Object localValue = null;
			// TODO
			if (componentContext != null) {
				localValue = componentContext.getLocalVariable(name);
			}
			if (localValue != null) {
				// fine
			} else if ("PARENT".equals(name) && componentContext != null && componentContext.parent != null) {
				// referenze parent context
				// TODO
				localValue = getRecursiveValue(componentContext.parent, componentContext.parent.localData,
						subExpression);
				localValue = "TODO";
			} else if (data == null) {
				return null;
			} else if (data instanceof Map) {
				localValue = ((Map) data).get(name);
			} else {
				localValue = BeanUtils.getProperty(data, name);
			}
			if (subExpression != null && subExpression.trim().length() > 0) {
				return getRecursiveValue(null, localValue, subExpression);
			} else {
				return localValue;
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}

	// TODO
	public Object getValue(String name) {
		return getRecursiveValue(activeContext, globalData, name);
	}

	// *************** printing methods component implementations ***************

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
		System.out.println("text " + x + " " + y + " " + value);

		contentStream.beginText();
		setColor(color);

		contentStream.newLineAtOffset(x, pageHeight - y - fontSize);
		contentStream.setFont(defaultFont, fontSize);
		contentStream.showText(value);
		contentStream.endText();

		float stringWidth = defaultFont.getStringWidth(value) / 1000 * fontSize;
		float stringHeight = defaultFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		return new PrintedBounds(relX, relY, relX + stringWidth, relY + stringHeight);
	}

	public PrintedBounds printNumber(float offsetX, float offsetY, Float relX, Float relY, Float w, Float fontSize,
			String color, TextFormat textFormat, String decimalFormat, Object value) throws IOException {
		if (fontSize == null) {
			fontSize = defaultFontSize;
		}
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			value = Double.parseDouble((String) value);
		}

		if (decimalFormat == null) {
			decimalFormat = "#0.00";
		}
		DecimalFormat formatter = new DecimalFormat(decimalFormat);

		float x = offsetX + relX;
		float y = offsetY + relY;

		String textValue = formatter.format(value);

		System.out.println("number " + x + " " + y + " " + textValue);

		contentStream.beginText();

		float stringWidth = defaultFont.getStringWidth(textValue) / 1000 * 14f;
		float stringHeight = defaultFont.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

		setColor(color);

		if (textFormat == TextFormat.left || w == null || w == 0f) {
			contentStream.newLineAtOffset(x, pageHeight - y - fontSize);
		} else {
			contentStream.newLineAtOffset(x + w - stringWidth, pageHeight - y - fontSize);
			stringWidth = w;
		}

		contentStream.setFont(defaultFont, fontSize);
		contentStream.showText(textValue);
		contentStream.endText();

		return new PrintedBounds(relX, relY, relX + stringWidth, relY + stringHeight);
	}

	public PrintedBounds printTextArea(float offsetX, float offsetY, Float relX, Float relY, Float w, Float h,
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

		System.out.println("textArea " + x + " " + y + " " + w + " " + h + " " + textFormat.name() + ": " + value);

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
		System.out.println("line " + x + " " + y + " " + w + " " + h);

		setColor(color);
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(x, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y - h);
		contentStream.stroke();
		return new PrintedBounds(relX, relY - lineWidth / 2, relX + w, relY + h + lineWidth / 2);
	}

	public PrintedBounds printRectangle(float offsetX, float offsetY, float relX, float relY, float w, float h,
			float lineWidth, String color) throws IOException {
		float x = offsetX + relX;
		float y = offsetY + relY;
		System.out.println("rect " + x + " " + y + " " + w + " " + h);
		setColor(color);
		contentStream.setLineWidth(lineWidth);
		contentStream.moveTo(x, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y);
		contentStream.lineTo(x + w, pageHeight - y - h);
		contentStream.lineTo(x, pageHeight - y - h);
		contentStream.lineTo(x, pageHeight - y + lineWidth / 2);
		contentStream.stroke();
		return new PrintedBounds(relX, relY, relX + w, relY + h);
	}

	public PrintedBounds printImage(float offsetX, float offsetY, Float relX, Float relY, Float w, Float h, String src,
			String url) throws IOException {
		float x = offsetX + relX;
		float y = offsetY + relY;

		PDImageXObject pdImage = null;
		if (src != null) {
			System.out.println("image(" + src + ") " + x + " " + y + " " + w + " " + h);
			pdImage = PDImageXObject.createFromFile(src, doc);
		} else if (url != null && url.trim().length() > 0) {
			System.out.println("image(" + url + ") " + x + " " + y + " " + w + " " + h);
			BufferedInputStream is = new BufferedInputStream(new URL(url).openStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			is.transferTo(bos);
			is.close();
			bos.close();
			pdImage = PDImageXObject.createFromByteArray(doc, bos.toByteArray(), url);
		}

		int ih = pdImage.getHeight();
		int iw = pdImage.getWidth();

		float ratio = 0;

		if (ih == 0 || iw == 0) {
			return null;
		} else {
			ratio = (float) ih / (float) iw;
		}

		Float pw = w, ph = h;

		if (w != null && w > 0 && (h == null || h == 0)) {
			ph = w * ratio;
			pw = w;
		} else if ((w == null || w == 0) && h != null && h > 0) {
			pw = h / ratio;
			ph = h;
		} else {
			ph = w * ratio;
			pw = w;
			if (ph > h) {
				pw = h / ratio;
				ph = h;
			}
		}
		contentStream.drawImage(pdImage, x, pageHeight - y - ph, pw, ph);
		return new PrintedBounds(relX, relY, relX + pw, relY + ph);
	}

	public void printPage(String id, List<PrintComponent> children) throws IOException {
		startDoc();
		while (reportState != ReportState.finish) {
			printGroup(id, 0, 0, null, null, null, null, 0, null, children);
			if (reportState == ReportState.breakPageAndContinue) {
				newPage();
				reportState = ReportState.iterating;
			}
		}
		endDoc();
	}

	public PrintedBounds printGroup(String id, float offsetX, float offsetY, Float relX, Float relY, Float w, Float h,
			float borderWidth, String borderColor, List<PrintComponent> children) throws IOException {
		float x = offsetX + ((relX != null) ? relX : 0f);
		float y = offsetY + ((relY != null) ? relY : 0f);
		System.out.println("group(" + id + ") " + x + " " + y + " " + w + " " + h);

		PrintedBounds bounds = new PrintedBounds(0, 0, 0, 0);
		Iterator<PrintComponent> i = children.iterator();
		while (i.hasNext()) {
			PrintComponent c = i.next();
			PrintedBounds childBound = c.print(this, x, y);
			System.out.println(" " + childBound);
			if (childBound != null) {
				bounds.merge(childBound);
			}
		}
		if (borderWidth != 0) {
			try {
				printRectangle(offsetX + x, offsetY + y, bounds.getLeft(), bounds.getTop(), bounds.getWidth(),
						bounds.getHeight(), borderWidth, borderColor);
			} catch (IOException e) {
			}
		}
		// return new PrintedBounds(relX, relY, relX + w, relY + h);
		return bounds;
	}

	public PrintedBounds printList(String id, float offsetX, float offsetY, ListComponent listComponent,
			java.util.Collection<Object> repeatedData) throws IOException {
		// Get Alltributes from compoennt
		Float relX = listComponent.getX();
		Float relY = listComponent.getY();
		Float w = listComponent.getW();
		Float h = listComponent.getH();
		// borderWidth= list.getBorderWith;

		// calculate position on paper
		float x = offsetX + relX;
		float y = offsetY + relY;
		Float breakAtY = null;
		System.out.println("list(" + id + ") " + x + " " + y + " " + w + " " + h);

		PrintedBounds bounds = new PrintedBounds(0, 0, 0, 0);

		PrintedBounds childBounds = null;

		float yEndOfFirstHeader = 0;
		Group header = (reportState == ReportState.start) ? listComponent.getFirstHeader()
				: listComponent.getOtherHeader();
		if (header != null) {
			System.out.println("  firstHeader");
			childBounds = header.print(this, x, y);
			if (childBounds != null) {
				bounds.merge(childBounds);
				yEndOfFirstHeader = y + childBounds.getHeight();
			}
		} else {
			System.out.println("  no firdstHeader");
		}

		PrintedBounds repeatedBlockBounds = new PrintedBounds(0, 0, 0, 0);

		float repX = offsetX + relX;
		float repY = yEndOfFirstHeader;

		this.activeContext = getContext(listComponent.getId(), repeatedData);

		if (listComponent.getRepeatBlock() != null) {
			int printedItemCount = 0;
			while (this.activeContext.hashNext()) {
				System.out.println("  repeatedBlock");
				Object repeatedDataEntry = this.activeContext.next();

				activeContext.setLocalVariable("item", repeatedDataEntry);

				childBounds = listComponent.getRepeatBlock().print(this, repX, repY);
				if (childBounds != null) {
					bounds.merge(childBounds);
					repY = repY + childBounds.getHeight();
					printedItemCount++;
				}

				if (listComponent.getBreakAt() != null && repY - offsetY > listComponent.getBreakAt()) {
					reportState = ReportState.breakPageAndContinue;
					break;
				} else if (listComponent.getMaxRows() != null && printedItemCount >= listComponent.getMaxRows()) {
					reportState = ReportState.breakPageAndContinue;
					break;
				}
			}
			if (!this.activeContext.hashNext() && this.activeContext.getParent() == null) {
				popContext(listComponent.getId());
				reportState = ReportState.finish;
			}
		} else {
			System.out.println("  no repeatedBlock");
		}

		float yBeginOfFooter = repY;
		float yEndOfFooter = repY;

		Group footer = (reportState == ReportState.breakPageAndContinue) ? listComponent.getSubtotalFooter()
				: listComponent.getTotalFooter();
		if (footer != null) {
			if (footer.getY() != null) {
				yBeginOfFooter = y + footer.getY();
			}
			System.out.println("  totalFooter");
			childBounds = footer.print(this, x, yBeginOfFooter);
			if (childBounds != null) {
				bounds.merge(childBounds);
				yEndOfFooter = yBeginOfFooter + childBounds.getHeight();
			}
		} else {
			System.out.println("  no totalFooter");
		}

		int borderWidth = 1;
		String borderColor = "blue";
		if (borderWidth != 0) {
			try {
				printRectangle(x, y, 0, 0, bounds.getWidth(), yEndOfFooter - y, borderWidth, borderColor);
			} catch (IOException e) {
			}
		}
		return new PrintedBounds(relX, relY, relX + w, relY + h);
	}

	// ************** PRIVATE Methods **************

	private ListContext getContext(String id, Collection<Object> repeatedData) {
		ListContext context = contextMap.get(id);
		if (context == null) {
			context = new ListContext(this.activeContext, id, repeatedData);
			contextMap.put(id, context);
		}
		this.activeContext = context;
		return this.activeContext;
	}

	private ListContext popContext(String id) {
		contextMap.remove(this.activeContext.getId());
		this.activeContext = this.activeContext.getParent();
		return this.activeContext;
	}

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

	static {
		colorMap.put("black", "000000");
		colorMap.put("red", "FF0000");
		colorMap.put("green", "00FF00");
		colorMap.put("blue", "0000FF");
	}

}
