package com.lauerbach.pdf.template;

import java.io.IOException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;

@XmlRootElement(name = "image")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Image extends PrintComponent {

	private String src;
	private String url;

	@Override
	public PrintedBounds print(PdfHelper helper, float offsetX, float offsetY) {
		PrintedBounds b = null;
		try {
			b = helper.printImage(offsetX, offsetY, x, y, w, h, src, url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	@XmlAttribute
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	@XmlAttribute
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
