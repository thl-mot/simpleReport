package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

@XmlRootElement( name="image")
@XmlAccessorType( XmlAccessType.PROPERTY)
public class Image extends PrintComponent {

	private String src;

	@XmlAttribute
	public String getImageName() {
		return src;
	}

	public void setImageName(String src) {
		this.src = src;
	}

	@Override
	public PrintedBounds print( float offsetX, float offsetY, PrintContext context) {
		return null;
	}

}
