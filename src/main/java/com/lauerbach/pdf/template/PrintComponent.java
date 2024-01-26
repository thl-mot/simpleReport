package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

public abstract class PrintComponent {
	float x, y, w, h;

	public abstract PrintedBounds print(float offsetX, float offsetY, PrintContext context);

	@XmlAttribute
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	@XmlAttribute
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@XmlAttribute
	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	@XmlAttribute
	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}

}
