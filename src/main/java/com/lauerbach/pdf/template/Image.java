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

@XmlRootElement( name="image")
@XmlAccessorType( XmlAccessType.PROPERTY)
public class Image extends PrintComponent {

	private String src;

	@XmlAttribute
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@Override
	public PrintedBounds print( float offsetX, float offsetY, PrintContext context) {
		PdfHelper helper = context.getHelper();
		PrintedBounds b= null;
		try {
			b= helper.printImage(offsetX, offsetY, x, y, w, h, src);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

}
