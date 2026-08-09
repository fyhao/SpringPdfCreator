package com.fyhao.springwebapps.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

public class TestToc {

	public static void main(String args[]) throws FileNotFoundException {
		// test for table of contents
		File file = new File("C:\\Temp\\toc.pdf");
		FileOutputStream fos = new FileOutputStream(file);
		String html = "<html><body>test " + new java.util.Date().toString() + "</body></html>";
		PdfWriter pdfWriter = new PdfWriter(fos);
		PdfDocument pdfDocument = new PdfDocument(pdfWriter, new DocumentProperties());
		PdfPage page = pdfDocument.addNewPage();
		
		HtmlConverter.convertToPdf(html, pdfDocument, null);
		// 20211028
	}
}
