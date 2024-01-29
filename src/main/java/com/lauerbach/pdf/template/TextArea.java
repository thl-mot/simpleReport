package com.lauerbach.pdf.template;

import java.io.IOException;

import com.lauerbach.pdf.PdfHelper;
import com.lauerbach.pdf.PrintedBounds;
import com.lauerbach.pdf.TextFormat;

public class TextArea extends Text {

	@Override
	public PrintedBounds print(PdfHelper helper, float offsetX, float offsetY) {
		PrintedBounds b = null;
		try {
			b = helper.printTextArea(offsetX, offsetY, x, y, w, h, this.fontSize, this.color,
					TextFormat.valueOf(textFormat), getPrintValueAsString(helper));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

}
