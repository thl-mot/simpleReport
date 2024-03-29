package com.lauerbach.pdf.template;

import java.io.IOException;

import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;
import com.lauerbach.pdf.TextFormat;

public class Text extends ValuePrintComponent {

	protected String color;
	protected Float fontSize = null;

	protected String textFormat = "left";

	@Override
	public PrintedBounds print(PdfHelper helper, float offsetX, float offsetY) {
		PrintedBounds b = null;
		try {
			b = helper.printText(offsetX, offsetY, x, y, this.fontSize, color, TextFormat.fromName(textFormat),
					getPrintValueAsString(helper));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
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

	@XmlAttribute
	public String getTextFormat() {
		return textFormat;
	}

	public void setTextFormat(String textFormat) {
		this.textFormat = textFormat;
	}

}
