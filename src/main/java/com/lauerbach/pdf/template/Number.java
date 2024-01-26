package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

@XmlRootElement( name="number")
@XmlAccessorType( XmlAccessType.PROPERTY)
public class Number extends PrintComponent {

	private String format="0.00";
	
	private Double value;
	
	private String field;

	@XmlAttribute
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlAttribute
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@XmlAttribute
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public PrintedBounds print( float offsetX, float offsetY, PrintContext context) {
		return null;
	}

}
