package com.lauerbach.pdf.template;

import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;

@XmlRootElement(name = "list")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ListComponent extends ValuePrintComponent {

	Group firstHeader, otherHeader;
	Group subtotalFooter, totalFooter;
	Group repeatBlock;

	Integer maxRows;
	Float   breakAt;
	
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

	@Override
	public PrintedBounds print(PdfHelper helper, float offsetX, float offsetY) {

		PrintedBounds bounds = null;
		try {
			Collection<Object> collection = null;
			Object obj = this.getPrintValue(helper);
			if (obj instanceof Collection) {
				collection = (Collection<Object>) obj;
			}
			bounds = helper.printList(id, offsetX, offsetY, this, collection);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bounds;
	}

	@XmlAttribute
	public Integer getMaxRows() {
		return maxRows;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	@XmlAttribute
	public Float getBreakAt() {
		return breakAt;
	}

	public void setBreakAt(Float breakAt) {
		this.breakAt = breakAt;
	}

}
