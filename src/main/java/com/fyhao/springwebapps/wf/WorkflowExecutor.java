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
		ctx.init();
		ctx.execute(request);
		ctx.close();
	}
}
