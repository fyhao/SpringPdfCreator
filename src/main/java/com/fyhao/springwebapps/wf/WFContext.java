package com.fyhao.springwebapps.wf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fyhao.springwebapps.wf.step.StepFactory;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;

public class WFContext {

	HttpServletResponse response;
	public Map<String,Object> vars = new HashMap<String,Object>();
	
	public PdfWriter pdfWriter;
	public PdfDocument pdfDocument;
	public Document document;
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public void executeinit(WFRequest request) { 
		for(WFStep step : request.initsteps) {
			WFStep s = StepFactory.createStep(step);
			s.execute(this);
		}
	}

	public void init() throws IOException {
		WriterProperties properties = new WriterProperties();
		if(vars.containsKey("USERPASS")) {
			byte[] USERPASS = ((String)vars.get("USERPASS")).getBytes();
			byte[] OWNERPASS = ((String)vars.get("USERPASS")).getBytes();
			properties.setStandardEncryption(USERPASS, OWNERPASS, EncryptionConstants.ALLOW_PRINTING,
	                EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA);
		}
		pdfWriter = new PdfWriter(response.getOutputStream(), properties);
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
