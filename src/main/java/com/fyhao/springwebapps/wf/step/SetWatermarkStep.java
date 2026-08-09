package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class SetWatermarkStep extends WFStep {
	@Override
	public void execute(WFContext ctx) {
		if (ctx.pdfDocument == null) return;
		float alpha = opacity == null ? 0.3f : Math.max(0f, Math.min(1f, opacity));
		float angle = rotation == null ? 45f : rotation;
		for (int pageNumber = 1; pageNumber <= ctx.pdfDocument.getNumberOfPages(); pageNumber++) {
			PdfPage page = ctx.pdfDocument.getPage(pageNumber);
			Rectangle size = page.getPageSize();
			PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), ctx.pdfDocument);
			Canvas canvas = new Canvas(pdfCanvas, ctx.pdfDocument, size);
			try {
				if (url != null && !url.trim().isEmpty()) {
					Image image = new Image(ImageDataFactory.create(ctx.replaceVars(url))).setOpacity(alpha);
					image.scaleToFit(size.getWidth() * 0.7f, size.getHeight() * 0.7f);
					image.setFixedPosition(pageNumber, (size.getWidth() - image.getImageScaledWidth()) / 2,
							(size.getHeight() - image.getImageScaledHeight()) / 2);
					canvas.add(image);
				}
				if (text != null && !text.trim().isEmpty()) {
					canvas.setFontColor(DeviceGray.GRAY, alpha).showTextAligned(ctx.replaceVars(text),
							size.getWidth() / 2, size.getHeight() / 2, pageNumber,
							TextAlignment.CENTER, VerticalAlignment.MIDDLE, angle);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("Unable to add watermark", e);
			} finally {
				canvas.close();
			}
		}
	}
}
