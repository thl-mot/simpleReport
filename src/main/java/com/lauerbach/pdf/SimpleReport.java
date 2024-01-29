package com.lauerbach.pdf;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.lauerbach.pdf.template.Template;

public class SimpleReport {
	
	Template template;
	
	public SimpleReport( File templateFile) throws JAXBException {
		this.template= Template.parse( templateFile);
	}
	
	public boolean print( File file, Object data)  {
		try {
			PdfHelper helper = new PdfHelper( file, data);
			template.print( helper);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
