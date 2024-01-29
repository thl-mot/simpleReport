package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;

public abstract class PrintComponent {
	String id;
	Float x, y, w, h;
	
	PrintComponent parent;

	public abstract PrintedBounds print(PdfHelper helper, float offsetX, float offsetY);
	
	@XmlAttribute
	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	@XmlAttribute
	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	@XmlAttribute
	public Float getW() {
		return w;
	}

	public void setW(Float w) {
		this.w = w;
	}

	@XmlAttribute
	public Float getH() {
		return h;
	}

	public void setH(Float h) {
		this.h = h;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PrintComponent getParent() {
		return parent;
	}

	public void setParent(PrintComponent parent) {
		this.parent = parent;
	}

	
	
}
