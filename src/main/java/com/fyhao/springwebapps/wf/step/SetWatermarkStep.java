package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

public class SetWatermarkStep extends WFStep {

	public void execute(WFContext ctx) {
		/*
		// Create an ImageData object
        String imageFile = url;
        ImageData data = null;
		try {
			data = ImageDataFactory.create(imageFile);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        // Creating an Image object
        Image image = new Image(data);
        image.scaleToFit(400, 700);
 
        // Creating template
        PdfFormXObject template = new PdfFormXObject(
            new Rectangle(image.getImageScaledWidth(),
                          image.getImageScaledHeight()));
        Canvas canvas
            = new Canvas(template, ctx.pdfDocument).add(image);
        String watermark = text;
        canvas.setFontColor(DeviceGray.GRAY)
            .showTextAligned(watermark, 120, 300,
                             TextAlignment.CENTER);
 
        // adding template to document
        Image imagew = new Image(template);
        ctx.document.add(imagew);
        */
	}
}
