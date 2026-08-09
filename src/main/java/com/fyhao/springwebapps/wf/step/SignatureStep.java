package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

/** Adds a visible text or image signature to the generated document. */
public class SignatureStep extends WFStep {
    @Override
    public void execute(WFContext ctx) {
        if (ctx.document == null) throw new IllegalStateException("signature requires generate to run first");
        try {
            if (url != null && !url.trim().isEmpty()) {
                ctx.document.add(new Image(ImageDataFactory.create(ctx.replaceVars(url))).scaleToFit(220, 100));
            }
            if (text != null && !text.trim().isEmpty()) {
                ctx.document.add(new Paragraph(ctx.replaceVars(text)).setItalic());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to add signature", e);
        }
    }
}
