package com.lauerbach.pdf.template;

import java.io.IOException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;
import com.lauerbach.pdf.TextFormat;

@XmlRootElement(name = "number")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Number extends Text {

	private String format = "0.00";

	public Number() {
		super();
		textFormat="right";
	}
	
	@XmlAttribute
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public PrintedBounds print(float offsetX, float offsetY, PrintContext context) {
		PdfHelper helper = context.getHelper();
		PrintedBounds b = null;
		try {
			b = helper.printNumber(offsetX, offsetY, x, y, w, this.fontSize, color, TextFormat.fromName(textFormat),
					format, value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

}
