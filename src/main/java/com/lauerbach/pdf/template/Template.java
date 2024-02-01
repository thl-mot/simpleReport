package com.lauerbach.pdf.template;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.lauerbach.pdf.PdfHelper;

@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Template extends Group {
	public enum Orientation {
		portrait, landscape;

		public static Orientation fromName(String text) {
			return Arrays.stream(values()).filter(bl -> bl.name().equalsIgnoreCase(text)).findFirst().orElse( portrait);
		}
	}

	public enum PaperSize {
		A4(PDRectangle.A4), A5(PDRectangle.A5), custom( null);

		PDRectangle pdfBoxSize;

		private PaperSize(PDRectangle rectangle) {
			this.pdfBoxSize = rectangle;
		}

		public static PaperSize fromName(String text) {
			return Arrays.stream(values()).filter(bl -> bl.name().equalsIgnoreCase(text)).findFirst().orElse(null);
		}
	}

	String paperSize;
	Orientation orientation = Orientation.portrait;

	static class InitParentVisitor {
		void visit(Group group) {
			if (group.children != null) {
				Iterator<PrintComponent> i = group.children.iterator();
				while (i.hasNext()) {
					PrintComponent child = i.next();
					child.setParent(group);
					if (child instanceof Group) {
						visit(((Group) child));
					}
				}
			}
		}
	}

	public Template() {
		x = 0f;
		y = 0f;
	}

	public static Template parse(File f) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Template.class);
		Unmarshaller u = jc.createUnmarshaller();

		Template t = (Template) u.unmarshal(f);

		new InitParentVisitor().visit(t);

		return (Template) u.unmarshal(f);
	}

	public void print(PdfHelper helper) throws IOException {
		PaperSize ps = PaperSize.fromName(paperSize);
		PDRectangle pdfBoxSize;
		if (ps == null || ps==PaperSize.custom) {
			if (w!=null && h!=null) {
				float POINTS_PER_INCH = 72;
				float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
				pdfBoxSize = new PDRectangle(w * POINTS_PER_MM, h * POINTS_PER_MM);
			} else {
				pdfBoxSize= PaperSize.A4.pdfBoxSize;
			}
		} else {
			pdfBoxSize= ps.pdfBoxSize;
		}
		
		helper.printPage(id, this.children, pdfBoxSize, (orientation == Orientation.landscape));
		// super.print(helper, 0, 0);
		// helper.endDoc();
	}

	@XmlAttribute
	public String getPaperSize() {
		return paperSize;
	}

	public void setPaperSize(String paperSize) {
		this.paperSize = paperSize;
	}

	@XmlAttribute
	public String getOrientation() {
		return orientation.name();
	}

	public void setOrientation(String orientation) {
		this.orientation = Orientation.fromName(orientation);
		if (this.orientation == null) {
			this.orientation = Orientation.portrait;
		}
	}

}
