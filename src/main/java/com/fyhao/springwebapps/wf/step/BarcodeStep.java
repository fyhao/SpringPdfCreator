package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

public class BarcodeStep extends WFStep {

	public void execute(WFContext ctx) {
		BarcodeQRCode qrCode = new BarcodeQRCode(text);
	    PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, ctx.pdfDocument);
	    Image barcodeImage = new Image(barcodeObject).setWidth(100f).setHeight(100f);
	    ctx.document.add(new Paragraph().add(barcodeImage));
	}
}
