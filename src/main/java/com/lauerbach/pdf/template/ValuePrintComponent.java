package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAttribute;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;

public abstract class ValuePrintComponent extends PrintComponent {

	String value;
	String field;

	public abstract PrintedBounds print(PdfHelper helper, float offsetX, float offsetY);

	public Object getPrintValue(PdfHelper helper) {
		if (field != null) {
			Object o = helper.getValue(field);
			if (o == null && value != null) {
				return value;
			}
			return helper.getValue(field);
		} else {
			return value;
		}
	}

	public String getPrintValueAsString(PdfHelper helper) {
		Object o = getPrintValue(helper);
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
