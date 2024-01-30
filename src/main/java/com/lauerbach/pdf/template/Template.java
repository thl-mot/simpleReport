package com.lauerbach.pdf.template;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PdfHelper;

@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Template extends Group {

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
		helper.printPage(id, this.children);
		// super.print(helper, 0, 0);
		// helper.endDoc();
	}

}
