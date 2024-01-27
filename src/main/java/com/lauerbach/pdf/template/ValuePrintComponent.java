package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

public abstract class ValuePrintComponent extends PrintComponent {

	String value;
	String field;

	public abstract PrintedBounds print(float offsetX, float offsetY, PrintContext context);

	public Object getPrintValue(PrintContext context) {
		if (field != null) {
			Object o = context.getValue(field);
			if (o == null && value != null) {
				return value;
			}
			return context.getValue(field);
		} else {
			return value;
		}
	}

	public String getPrintValueAsString(PrintContext context) {
		Object o = getPrintValue(context);
		return o != null ? o + "" : null;
	}

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
