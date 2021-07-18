package com.fyhao.springwebapps;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WFStep;
import com.fyhao.springwebapps.wf.WorkflowExecutor;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

@RestController
@RequestMapping(value="test")
public class TestController {

	static Logger logger = LoggerFactory.getLogger(TestController.class);
	@RequestMapping("/")
	public @ResponseBody String greeting() {
        logger.info("Greeting");
		return "Hello, World";
    }
    
    @RequestMapping("/testpdf")
	public void testpdf(HttpServletResponse response) throws Exception {
        String html = "<html><body>test " + new java.util.Date().toString() + "</body></html>";
        
       HtmlConverter.convertToPdf(html, response.getOutputStream());
       
	}
    
    @RequestMapping("/testdoc")
	public void testdoc(HttpServletResponse response) throws Exception {
       String html = "<html><body>test " + new java.util.Date().toString() + "</body></html>";
       PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
       PdfDocument pdfDocument = new PdfDocument(pdfWriter, new DocumentProperties());
       PdfPage page = pdfDocument.addNewPage();
       
       HtmlConverter.convertToPdf(html, pdfDocument, null);
	}
    
    @RequestMapping("/testwf")
	public void testwf(HttpServletResponse response) throws Exception {
       WFRequest request = new WFRequest();
       WFStep step = new WFStep();
       step.action = "setVar";
       step.name = "html";
       String html = "<html><body>test " + new java.util.Date().toString() + "</body></html>";
       step.value = html;
       request.steps.add(step);
       

       step = new WFStep();
       step.action = "generate";
       request.steps.add(step);
       
       step = new WFStep();
       step.action = "setWatermark";
       step.text = "sample watermark";
       step.url = "https://i.stack.imgur.com/ILTQq.png";
       request.steps.add(step);
       
       WorkflowExecutor.generatePdf(request, response);
	}
    
    @RequestMapping("/testrequest")
	public WFRequest testrequest() throws Exception {
    	WFRequest request = new WFRequest();
        WFStep step = new WFStep();
        step.action = "setVar";
        step.name = "html";
        String html = "<html><body>test " + new java.util.Date().toString() + "</body></html>";
        step.value = html;
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "setVar";
        step.name = "Break";
        step.value = "<hr />";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "httpget";
        step.name = "httpresult";
        step.url = "http://localhost:8080/test/mockhttpget";
        request.steps.add(step);

        step = new WFStep();
        step.action = "add";
        step.name = "html";
        step.value = "Break";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "add";
        step.name = "html";
        step.value = "httpresult";
        request.steps.add(step);
        
        
        step = new WFStep();
        step.action = "generate";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "setWatermark";
        step.text = "sample watermark";
        step.url = "https://i.stack.imgur.com/ILTQq.png";
        request.steps.add(step);
        
        return request;
	}
    
    @RequestMapping("/testrequest2")
	public WFRequest testrequest2() throws Exception {
    	WFRequest request = new WFRequest();
        WFStep step = new WFStep();
        step.action = "setVar";
        step.name = "html";
        String html = "<html><body>test " + new java.util.Date().toString() + "</body></html>";
        step.value = html;
        request.steps.add(step);
        
        
        
        step = new WFStep();
        step.action = "generate";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "setWatermark";
        step.text = "sample watermark";
        step.url = "https://i.stack.imgur.com/ILTQq.png";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "barcode";
        step.text = "the test barcode";
        request.steps.add(step);
        
        return request;
	}
    
    @RequestMapping("/mockhttpget")
	public String mockhttpget() throws Exception {
    	return "mock http result";
	}
}