package com.fyhao.springwebapps.wf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
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
	
	public List<String> extractvarsbracket(String p) {
		List<String> result = new ArrayList<String>();
		while(p.contains("{{") && p.contains("}}")) {
			String t = p.substring(p.indexOf("{{"), p.indexOf("}}")+2);
			result.add(t);
			p = p.substring(p.indexOf("}}") + 2);
		}
		return result;
	}
	
	ScriptEngineManager manager = null;
	ScriptEngine scriptEngine = null;
	String utilsrc = "function int(a) {return java.lang.Integer.parseInt(a);}";
	
	public String replaceVars(String p) {
		if(p == null) return "";

		for(Map.Entry<String,Object> entry: vars.entrySet()) {
			while(p.contains("{{" + entry.getKey() + "}}")) {
				p = p.replace("{{" + entry.getKey() + "}}", (String)entry.getValue());
			}
		}
		try {
			List<String> brackets = extractvarsbracket(p);
			List<String> results = new ArrayList<String>();
			Map<String, Object> parameters = null;
			if(!brackets.isEmpty()) {
				parameters = new HashMap<String,Object>();
				for(Map.Entry<String,Object> entry : vars.entrySet()) {
					parameters.put(entry.getKey(), entry.getValue());
				}
				for(String bracket : brackets) {
					String src = bracket.replace("{{","").replace("}}", "");
					if(scriptEngine == null) {
						manager = new ScriptEngineManager();
						scriptEngine = manager.getEngineByName("nashorn");
					}
					src = utilsrc + "\n" + src;
					Object result = scriptEngine.eval(src, 
	                        new SimpleBindings(parameters));
					results.add("" + result);
				}
				for(int i = 0; i < brackets.size(); i++) {
					p = p.replace(brackets.get(i), results.get(i));
				}
			}
			return p;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
