package com.fyhao.springwebapps.wf.step;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.utils.PdfMerger;

public class MergeStep extends WFStep {

    public String[] urls;

    @Override
    public void execute(WFContext ctx) {
        if (urls == null || ctx.pdfDocument == null) {
            return;
        }
        PdfMerger merger = new PdfMerger(ctx.pdfDocument);
        for (String u : urls) {
            try (InputStream in = new URL(u).openStream();
                 PdfDocument srcDoc = new PdfDocument(new PdfReader(in))) {
                merger.merge(srcDoc, 1, srcDoc.getNumberOfPages());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
