package com.lauerbach.pdf.template;

import java.io.IOException;

import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

public class Line extends PrintComponent {

	String color = "000000";
	float lineWidth= 2;

	@Override
	public PrintedBounds print(float offsetX, float offsetY, PrintContext context) {
		PdfHelper helper = context.getHelper();
		PrintedBounds b= null;
		try {
			b= helper.printLine(offsetX, offsetY, x, y, w, h, lineWidth, color);
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
	public float getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	
	
}
