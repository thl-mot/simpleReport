package com.lauerbach.pdf.template;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

public class Group extends PrintComponent {
	Float border = null;
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
	public PrintedBounds print(float offsetX, float offsetY, PrintContext context) {
		PrintedBounds bounds = new PrintedBounds(x, y, 0, 0);
		Iterator<PrintComponent> i = children.iterator();
		while (i.hasNext()) {
			PrintComponent c = i.next();
			PrintedBounds childBound = c.print(offsetX + x, offsetY + y, context);
			if (childBound != null) {
				bounds.merge(childBound);
			}
		}
		if (border != null) {
			try {
				context.getHelper().printRectangle(offsetX + x, offsetY + y, bounds.getLeft(), bounds.getTop(),
						bounds.getWidth(), bounds.getHeight(), border, borderColor);
			} catch (IOException e) {
			}
		}
		return bounds;
	}

	@XmlAttribute
	public Float getBorder() {
		return border;
	}

	public void setBorder(Float border) {
		this.border = border;
	}

	@XmlAttribute
	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

}
