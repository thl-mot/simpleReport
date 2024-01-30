package com.lauerbach.pdf.template;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;

public class Group extends PrintComponent {
	Float borderWidth = 0f;
	String borderColor = "000000";

	List<PrintComponent> children;

	@XmlElements({ @XmlElement(name = "image", type = Image.class), @XmlElement(name = "number", type = Number.class),
			@XmlElement(name = "text", type = Text.class), @XmlElement(name = "textArea", type = TextArea.class),
			@XmlElement(name = "list", type = ListComponent.class), @XmlElement(name = "line", type = Line.class),
			@XmlElement(name = "group", type = Group.class) })
	public List<PrintComponent> getChildren() {
		return children;
	}

	public void setChildren(List<PrintComponent> children) {
		this.children = children;
	}

	@Override
	public PrintedBounds print(PdfHelper helper, float offsetX, float offsetY) {

		PrintedBounds bounds = null;
		try {
			bounds = helper.printGroup(id, offsetX, offsetY, x, y, w, h, borderWidth, borderColor, children);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bounds;
	}

	@XmlAttribute
	public Float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Float borderWidth) {
		this.borderWidth = borderWidth;
	}

	@XmlAttribute
	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

}
