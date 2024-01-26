package com.lauerbach.pdf.template;

import java.io.IOException;

import javax.swing.text.StyledEditorKit.FontSizeAction;
import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;
import com.lauerbach.pdf.TextFormat;

public class Text extends PrintComponent {

	protected String color;
	protected String value;

	protected String field;
	protected Float fontSize = null;

	@Override
	public PrintedBounds print(float offsetX, float offsetY, PrintContext context) {
		PdfHelper helper = context.getHelper();
		PrintedBounds b= null;
		try {
			b= helper.printText(offsetX, offsetY, x, y, this.fontSize, color, TextFormat.left, value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@XmlAttribute
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@XmlAttribute
	public Float getFontSize() {
		return fontSize;
	}

	public void setFontSize(Float fontSize) {
		this.fontSize = fontSize;
	}

}
