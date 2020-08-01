package com.fyhao.springwebapps;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.html2pdf.HtmlConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="pdf")
public class PdfController {

    static Logger logger = LoggerFactory.getLogger(PdfController.class);
    
    static final String HTMLFormConstants = "htmlform" + System.currentTimeMillis();
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
        HtmlConverter.convertToPdf(html, response.getOutputStream());
	}
}