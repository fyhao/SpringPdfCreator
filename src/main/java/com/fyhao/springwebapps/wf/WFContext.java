package com.fyhao.springwebapps.wf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fyhao.springwebapps.wf.step.SetVarStep;
import com.fyhao.springwebapps.wf.step.StepFactory;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

public class WFContext {

	HttpServletResponse response;
	public Map<String,Object> vars = new HashMap<String,Object>();
	
	public PdfWriter pdfWriter;
	public PdfDocument pdfDocument;
	public Document document;
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	

	public void init() throws IOException {
		pdfWriter = new PdfWriter(response.getOutputStream());
        pdfDocument = new PdfDocument(pdfWriter, new DocumentProperties());
        
	}
	
	public String replaceVars(String p) {
		if(p == null) return "";
		for(Map.Entry<String,Object> entry: vars.entrySet()) {
			while(p.contains("{{" + entry.getKey() + "}}")) {
				p = p.replace("{{" + entry.getKey() + "}}", (String)entry.getValue());
			}
		}
		return p;
	}
	
	public void execute(WFRequest request) throws IOException {
		for(WFStep step : request.steps) {
			WFStep s = StepFactory.createStep(step);
			s.execute(this);
		}
	}
	
	public void generatePDF() throws IOException {

		String html = (String)vars.get("html");
        document = HtmlConverter.convertToDocument(html, pdfDocument, null);
        
	}
	

	public void close() {

        document.close();
	}
	
}
