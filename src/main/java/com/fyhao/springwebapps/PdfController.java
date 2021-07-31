package com.fyhao.springwebapps;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fyhao.springwebapps.business.ExtractImageService;
import com.fyhao.springwebapps.business.PasswordprotectService;
import com.fyhao.springwebapps.dto.ExtractImageRequest;
import com.fyhao.springwebapps.dto.PasswordprotectRequest;
import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WorkflowExecutor;
import com.itextpdf.html2pdf.HtmlConverter;

@RestController
@RequestMapping(value="pdf")
public class PdfController {

    static Logger logger = LoggerFactory.getLogger(PdfController.class);
    
    static final String HTMLFormConstants = "htmlform" + System.currentTimeMillis();
    
    @Autowired
    ExtractImageService extractImageService;
    
    @Autowired
    PasswordprotectService passwordprotectService;
    
    @RequestMapping("/")
	public @ResponseBody String home() {
        String html = "<html><head><title>PDF Generation Form</title>";
        html += "<body>";
        html += "<form method=post action=/pdf/generatepdf method=post>";
        html += "<textarea name='" + HTMLFormConstants + "'></textarea>";
        html += "<input type=submit value=submit />";
        html += "</form>";
        html += "</body></html>";
		return html;
    }
    
    @RequestMapping(value="/generatepdf", method = RequestMethod.POST)
	public void generatepdf(@RequestBody String html, HttpServletResponse response) throws Exception {
        html = html.replace(HTMLFormConstants + "=", "");
        html = html.trim();
        html = URLDecoder.decode(html, "UTF-8");
        response.setContentType("application/pdf");
        HtmlConverter.convertToPdf(html, response.getOutputStream());
	}
    
    @RequestMapping(value="/workflowpdf", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public void workflowpdf(@RequestBody WFRequest request, HttpServletResponse response) throws Exception {
    	response.setContentType("application/pdf");
    	WorkflowExecutor.generatePdf(request, response);
	}
    
    @RequestMapping(value="/getpdf", method = RequestMethod.GET)
	public void workflowpdf(@RequestParam String url, HttpServletResponse response) throws Exception {
    	response.setContentType("application/pdf");
    	RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WFRequest> resp = restTemplate.getForEntity(url, WFRequest.class);
        WFRequest request = resp.getBody();
    	WorkflowExecutor.generatePdf(request, response);
	}
    //extractimagefrompdf
    @RequestMapping(value="/extractimagefrompdf", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public void extractimagefrompdf(@RequestBody ExtractImageRequest request, HttpServletResponse response) throws Exception {
    	response.setContentType("application/zip");
    	String url = request.url;
    	extractImageService.downloadZip(url, response.getOutputStream());
	}
    //uploadpdfextractimage
    @RequestMapping(value="/uploadpdfextractimage", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void uploadpdfextractimage(@RequestParam MultipartFile file, HttpServletResponse response) throws Exception {
    	response.setContentType("application/zip");
    	extractImageService.uploadpdfextractimage(file, response.getOutputStream());
	}
    
  //extractimagefrompdf
    @RequestMapping(value="/passwordprotectfrompdf", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public void passwordprotectfrompdf(@RequestBody PasswordprotectRequest request, HttpServletResponse response) throws Exception {
    	response.setContentType("application/pdf");
    	String url = request.url;
    	String pwd = request.pwd;
    	passwordprotectService.downloadPDF(url, pwd, response.getOutputStream());
	}
    //uploadpdfextractimage
    @RequestMapping(value="/uploadpdfpasswordprotect", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void uploadpdfpasswordprotect(@RequestParam MultipartFile file, @RequestParam String pwd, HttpServletResponse response) throws Exception {
    	response.setContentType("application/pdf");
    	passwordprotectService.uploadpdfpasswordprotect(file, pwd, response.getOutputStream());
	}
    
}