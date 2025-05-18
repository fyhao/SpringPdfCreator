package com.fyhao.springwebapps.wf;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

public class WorkflowExecutor {

	public static void generatePdf(WFRequest request, HttpServletResponse response) throws IOException {
		
		WFContext ctx = new WFContext();
		ctx.setResponse(response);
		ctx.executeinit(request);
		ctx.init();
		// Set PDF metadata if present
		if (request.metadata != null && ctx.pdfDocument != null) {
			com.itextpdf.kernel.pdf.PdfDocumentInfo info = ctx.pdfDocument.getDocumentInfo();
			String author = request.metadata.getOrDefault("author", "");
			String title = request.metadata.getOrDefault("title", "");
			String subject = request.metadata.getOrDefault("subject", "");
			if (!author.isEmpty()) info.setAuthor(author);
			if (!title.isEmpty()) info.setTitle(title);
			if (!subject.isEmpty()) info.setSubject(subject);
		}
		ctx.execute(request);
		ctx.close();
	}
}
