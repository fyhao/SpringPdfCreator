package com.fyhao.springwebapps.wf.step;

import java.util.List;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;

public class TocStep extends WFStep {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(WFContext ctx) {
        if (ctx.document == null) {
            return;
        }

        ctx.document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        ctx.document.add(new Paragraph("Table of Contents")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16));

        Object obj = ctx.vars.get("toc_entries");
        if (obj instanceof List<?>) {
            List<String> entries = (List<String>) obj;
            int i = 1;
            for (String entry : entries) {
                ctx.document.add(new Paragraph(String.format("%d. %s", i++, ctx.replaceVars(entry))));
            }
        }
        ctx.document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
    }
}
