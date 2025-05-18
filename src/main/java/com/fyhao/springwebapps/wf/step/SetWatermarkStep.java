package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

public class SetWatermarkStep extends WFStep {

       public void execute(WFContext ctx) {
               PdfDocument pdfDoc = ctx.pdfDocument;
               if (pdfDoc == null) {
                       return;
               }

               for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                       PdfPage page = pdfDoc.getPage(i);
                       PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
                       Canvas canvas = new Canvas(pdfCanvas, pdfDoc, new Rectangle(page.getPageSize()));

                       if (url != null && !url.isEmpty()) {
                               try {
                                       ImageData data = ImageDataFactory.create(url);
                                       Image img = new Image(data).setOpacity(0.3f);
                                       float x = (page.getPageSize().getWidth() - img.getImageScaledWidth()) / 2;
                                       float y = (page.getPageSize().getHeight() - img.getImageScaledHeight()) / 2;
                                       img.setFixedPosition(i, x, y);
                                       canvas.add(img);
                               } catch (Exception e) {
                                       e.printStackTrace();
                               }
                       }

                       if (text != null && !text.isEmpty()) {
                               canvas.setFontColor(DeviceGray.GRAY);
                               canvas.showTextAligned(text,
                                       page.getPageSize().getWidth() / 2,
                                       page.getPageSize().getHeight() / 2,
                                       i,
                                       TextAlignment.CENTER,
                                       VerticalAlignment.MIDDLE,
                                       45);
                       }

                       canvas.close();
               }
       }
}
