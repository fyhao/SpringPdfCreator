package com.fyhao.springwebapps.wf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
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
	
	PdfWriter pdfWriter;
	PdfDocument pdfDocument;
	Document document;
	
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	

	public void init() throws IOException {
		pdfWriter = new PdfWriter(response.getOutputStream());
        pdfDocument = new PdfDocument(pdfWriter, new DocumentProperties());
        
	}
	
	public String replaceVars(String p) {
		for(Map.Entry<String,Object> entry: vars.entrySet()) {
			while(p.contains("{{" + entry.getKey() + "}}")) {
				p = p.replace("{{" + entry.getKey() + "}}", (String)entry.getValue());
			}
		}
		return p;
	}
	
	public void execute(WFRequest request) throws IOException {
		for(WFStep step : request.steps) {
			if(step.action.equals("setVar")) {
				vars.put(step.name, replaceVars(step.value));
			}
			else if(step.action.equals("add")) {
				vars.put(step.name, (String)vars.get(step.name) + replaceVars((String)vars.get(step.value)));
			}
			else if(step.action.equals("httpget")) {
				RestTemplate restTemplate = new RestTemplate();
		        ResponseEntity<String> resp = restTemplate.getForEntity(step.url, String.class);
		        vars.put(step.name, replaceVars(resp.getBody()));
			}
			else if(step.action.equals("generate")) {
				generatePDF();
			}
			else if(step.action.equals("setWatermark")) {
				String value = step.value;
				// Create an ImageData object
		        String imageFile = step.url;
		        ImageData data = ImageDataFactory.create(imageFile);
		 
		        // Creating an Image object
		        Image image = new Image(data);
		        image.scaleToFit(400, 700);
		 
		        // Creating template
		        PdfFormXObject template = new PdfFormXObject(
		            new Rectangle(image.getImageScaledWidth(),
		                          image.getImageScaledHeight()));
		        Canvas canvas
		            = new Canvas(template, pdfDocument).add(image);
		        String watermark = step.text;
		        canvas.setFontColor(DeviceGray.GRAY)
		            .showTextAligned(watermark, 120, 300,
		                             TextAlignment.CENTER);
		 
		        // adding template to document
		        Image imagew = new Image(template);
		        document.add(imagew);
			}
			else if(step.action.equals("barcode")) {
				BarcodeQRCode qrCode = new BarcodeQRCode(step.text);
			    PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
			    Image barcodeImage = new Image(barcodeObject).setWidth(100f).setHeight(100f);
			    document.add(new Paragraph().add(barcodeImage));
			}
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
