package com.lauerbach.pdf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

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
	
	public boolean printMultiFile( String baseName, Object... data)  {
		return printMultiFile( baseName, Arrays.asList( data));
	}
	
	public boolean printMultiFile( String baseName, Collection<Object> col)  {
		try {
			Iterator<Object> i = col.iterator();
			int fileNo=0;
			while (i.hasNext()) {
				PdfHelper helper = new PdfHelper( new File( baseName+"-"+fileNo+".pdf"), i.next());
				template.print( helper);
				fileNo++;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
