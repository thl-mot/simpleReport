package com.lauerbach.pdf;

import java.io.File;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfBoxMain {

	public static void main(String[] args) throws Exception {
		PDDocument doc= new PDDocument();
		PDDocumentInformation info = doc.getDocumentInformation();  
		info.setAuthor("Thomas Lauerbach");
		doc.setDocumentInformation(info);
		
		PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
		System.out.println( "Scale-Y: "+font.getFontMatrix().getScaleY());
		
	    PDPage page = new PDPage( PDRectangle.A4);
	    System.out.println("User-Unit: "+page.getUserUnit());
	    
	    float h = page.getMediaBox().getHeight();
	    
	    doc.addPage( page);
	    
	    PDPageContentStream contentStream = new PDPageContentStream(doc, page);  
	    
	    contentStream.beginText();  
	    contentStream.newLineAtOffset(0, h-14); 
	    contentStream.setLeading(14.5f);  
	    contentStream.setFont( font, 14);  
	    contentStream.showText("Hallo 1");
	    
	    System.out.println( font.getStringWidth("Hal"));
	    System.out.println( font.getStringWidth("Hallo"));
	    System.out.println( font.getStringWidth("Hallo 1"));
	    
	    
	    float stringWidth = font.getStringWidth("Hallo 1")/1000*14f;
	    
	    
	    
		contentStream.newLineAtOffset(0, -14.5f); 
	    contentStream.showText("Hallo 2");
	    contentStream.endText();
	    
	    contentStream.setLineWidth(2);
	    contentStream.moveTo( 0, h-100);
	    contentStream.lineTo( stringWidth, h-100);
	    contentStream.stroke();

	    contentStream.moveTo( 0, h-110);
	    contentStream.lineTo( page.getMediaBox().getWidth()-10, h-110);
	    contentStream.stroke();
	    
	    contentStream.moveTo( 0, h-120);
	    contentStream.lineTo( page.getMediaBox().getWidth(), h-120);
	    contentStream.stroke();

	    
	    URL r = PdfBoxMain.class.getResource("/netzebw-logo.jpg");
	    PDImageXObject pdImage = PDImageXObject.createFromFile( r.getFile(),doc);
	    contentStream.drawImage(pdImage, 10, h-50-pdImage.getHeight()*0.2f, pdImage.getWidth()*0.2f, pdImage.getHeight()*0.2f);  
	    
	    contentStream.close();
		doc.save( new File("test.pdf"));
		doc.close();
		Runtime.getRuntime().exec("okular test.pdf");
	}

}
