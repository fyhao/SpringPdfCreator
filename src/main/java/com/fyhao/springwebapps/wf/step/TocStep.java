package com.fyhao.springwebapps.wf.step;

import java.util.List;
import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;

public class TocStep extends WFStep {
    @Override
    public void execute(WFContext ctx) {
        if (ctx.document == null) throw new IllegalStateException("toc requires generate to run first");
        List<?> tocEntries = entries != null ? entries : (List<?>) ctx.vars.get("toc_entries");
        ctx.document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        ctx.document.add(new Paragraph(text == null ? "Table of Contents" : ctx.replaceVars(text))
                .setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(16));
        if (tocEntries != null) {
            int index = 1;
            for (Object entry : tocEntries) {
                ctx.document.add(new Paragraph(index++ + ". " + ctx.replaceVars(String.valueOf(entry))));
            }
        }
    }
}
