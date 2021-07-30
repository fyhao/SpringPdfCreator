package com.fyhao.springwebapps;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WFStep;
import com.fyhao.springwebapps.wf.WorkflowExecutor;
import com.fyhao.springwebapps.wf.step.SetVarStep;
import com.fyhao.springwebapps.wf.step.StepFactory;
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
        
        // add metadata
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_title";
        step.value = "my title";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_author";
        step.value = "my author";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_subject";
        step.value = "my subject";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_keywords";
        step.value = "my title";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_creator";
        step.value = "my creator";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "metadata";
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
        
        // add metadata
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_title";
        step.value = "my title";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_author";
        step.value = "my author";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_subject";
        step.value = "my subject";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_keywords";
        step.value = "my keywords";
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "metadata_creator";
        step.value = "my creator";
        request.steps.add(step);
        
        step = new WFStep();
        step.action = "metadata";
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
    
    @RequestMapping("/unittest")
	public String unittest() throws Exception {
    	TestMgm mgm = new TestMgm();
    	runTestsuite(mgm);
    	return mgm.htmlreport();
	}
    
    public static void main(String args[]) {
    	TestMgm mgm = new TestMgm();
    	runTestsuite(mgm);
    	String res = mgm.report(); System.out.println(res);
    }


	public static class TestMgm {
		int noOfTest = 0;
		int noOfPass = 0;
		StringBuffer sb = new StringBuffer();
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		void assertTest(Object expected, Object actual, String msg) {
	    	noOfTest++;
	    	if(expected.equals(actual)) {
	    		noOfPass++;
	    		sb.append("[PASS] - Expected [" + expected + "] Actual [" + actual + "] " + msg + "\n");
	    	}
	    	else {
	    		sb.append("[FAIL] - Expected [" + expected + "] Actual [" + actual + "] " + msg + "\n");
	    	}
	    }
		
		String report() {
			endTime = System.currentTimeMillis();
			String percent = 100 * ((double)noOfPass / noOfTest) + "%";
			sb.append("Result: " + noOfPass + "/" + noOfTest + " - " + percent);
			sb.append("\nTaken: " + (endTime - startTime) + "ms");
			return sb.toString();
		}
		
		String htmlreport() {
			String s = report();
			s = s.replaceAll("\n","<br>");
			return s;
		}
	}
	
    static void runTestsuite(TestMgm mgm) {
    	testsuite1(mgm); // workflow engine valid test
    	testsuite2(mgm); // workflow engine invalid test
    	testsuite3(mgm); // workflow engine stepfactory
    }
    
	static void testsuite1(TestMgm mgm) {
		PdfController pdf = new PdfController();
		mgm.assertTest(true, pdf.home().contains("PDF Generation Form"), "Check PDF Generation Form");
		
		WFContext ctx = new WFContext();
		mgm.assertTest(0, ctx.vars.size(), "WFContext size initialize should be 0");
		WFRequest request = new WFRequest();
		WFStep step = new WFStep();
        step.action = "setVar";
        step.name = "var1";
        step.value = "value1";
        request.steps.add(step);
        try { ctx.execute(request); } catch (IOException e) {}
        mgm.assertTest(1, ctx.vars.size()," Should have 1 vars");
        step = new WFStep();
        step.action = "setVar";
        step.name = "var2";
        step.value = "value2";
        request.steps.add(step);
        try { ctx.execute(request); } catch (IOException e) {}
        mgm.assertTest(2, ctx.vars.size()," Should have 2 vars");
        mgm.assertTest("value1", ctx.vars.get("var1"), "var1 = value1");
        mgm.assertTest("value2", ctx.vars.get("var2"), "var2 = value2");
        step = new WFStep();
        step.action = "setVar";
        step.name = "var3";
        step.value = "value3 with {{var1}} {{var2}}";
        request.steps.add(step);
        try { ctx.execute(request); } catch (IOException e) {}
        mgm.assertTest(3, ctx.vars.size()," Should have 3 vars");
        mgm.assertTest("value1", ctx.vars.get("var1"), "var1 = value1");
        mgm.assertTest("value2", ctx.vars.get("var2"), "var2 = value2");
        mgm.assertTest("value3 with value1 value2", ctx.vars.get("var3"), "var3");
        
        ctx = new WFContext();
        mgm.assertTest(0, ctx.vars.size()," Should have 0 vars");
        request = new WFRequest();
		step = new WFStep();
        step.action = "setVar";
        step.name = "var1";
        step.value = "value1";
        request.steps.add(step);
		step = new WFStep();
        step.action = "setVar";
        step.name = "html";
        step.value = "<html><body>Test {{var1}}</body></html>";
        request.steps.add(step);
        try { ctx.execute(request); } catch (IOException e) {}
        mgm.assertTest(2, ctx.vars.size()," Should have 2 vars");
        mgm.assertTest("<html><body>Test value1</body></html>", ctx.vars.get("html"), "html value");
        
	}
	
	static void testsuite2(TestMgm mgm) {
		// Doing some invalid test case
		WFContext ctx = new WFContext();
		WFRequest request = null;
		WFStep step = null;
		request = new WFRequest();
		step = new WFStep();
        step.action = "setVar";
        step.name = "var1";
        step.value = null;
        request.steps.add(step);
        step = new WFStep();
        step.action = "setVar";
        step.name = "html";
        step.value = "<html><body>Test {{var1}}</body></html>";
        request.steps.add(step);
        try { ctx.execute(request); } catch (IOException e) {}
        mgm.assertTest(2, ctx.vars.size()," Should have 2 vars");
        mgm.assertTest("<html><body>Test </body></html>", ctx.vars.get("html"), "html value");
        
	}
	
	static void testsuite3(TestMgm mgm) {
		WFStep s = new WFStep();
		s.action = "setVar";
		s.name = "apple";
		s.value = "dog";
		WFStep r = StepFactory.createStep(s);
		boolean t = r instanceof SetVarStep;
		mgm.assertTest(true, t, "is SetVarStep");
		if(t) {
			SetVarStep svs = (SetVarStep)r;
			mgm.assertTest(s.action, svs.action, "check SetVarStep action");
			mgm.assertTest(s.name, svs.name, "check SetVarStep name");
			mgm.assertTest(s.value, svs.value, "check SetVarStep value");
		}
	}
}
