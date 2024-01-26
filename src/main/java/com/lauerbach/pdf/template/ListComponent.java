package com.lauerbach.pdf.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PrintContext;
import com.lauerbach.pdf.PrintedBounds;

@XmlRootElement(name = "list")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ListComponent extends PrintComponent {

	String field;

	Group firstHeader, otherHeader;
	Group subtotalFooter, totalFooter;
	Group repeatBlock;

	@XmlElement
	public Group getFirstHeader() {
		return firstHeader;
	}

	public void setFirstHeader(Group firstHeader) {
		this.firstHeader = firstHeader;
	}

	@XmlElement
	public Group getOtherHeader() {
		return otherHeader;
	}

	public void setOtherHeader(Group otherHeader) {
		this.otherHeader = otherHeader;
	}

	@XmlElement
	public Group getSubtotalFooter() {
		return subtotalFooter;
	}

	public void setSubtotalFooter(Group subtotalFooter) {
		this.subtotalFooter = subtotalFooter;
	}

	@XmlElement
	public Group getTotalFooter() {
		return totalFooter;
	}

	public void setTotalFooter(Group totalFooter) {
		this.totalFooter = totalFooter;
	}

	@XmlElement
	public Group getRepeatBlock() {
		return repeatBlock;
	}

	public void setRepeatBlock(Group repeatBlock) {
		this.repeatBlock = repeatBlock;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public PrintedBounds print(float offsetX, float offsetY, PrintContext context) {
		return null;
	}

}
