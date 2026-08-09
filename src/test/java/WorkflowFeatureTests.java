import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.fyhao.springwebapps.wf.step.SetWatermarkStep;
import com.fyhao.springwebapps.wf.step.StepFactory;
import com.fyhao.springwebapps.wf.step.TocStep;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class WorkflowFeatureTests {
    @Test
    void factoryCopiesExtendedProtocolFieldsAndRejectsUnknownActions() {
        WFStep source = new WFStep();
        source.action = "toc";
        source.entries = Arrays.asList("One", "Two");
        WFStep result = StepFactory.createStep(source);
        assertTrue(result instanceof TocStep);
        assertEquals(source.entries, result.entries);
        source.action = "not-real";
        assertThrows(IllegalArgumentException.class, () -> StepFactory.createStep(source));
    }

    @Test
    void tocAndWatermarkAreWrittenToPdf() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdf);
        document.add(new Paragraph("Document body"));
        WFContext ctx = new WFContext();
        ctx.pdfDocument = pdf;
        ctx.document = document;

        TocStep toc = new TocStep();
        toc.entries = Arrays.asList("Introduction", "Details");
        toc.execute(ctx);
        SetWatermarkStep watermark = new SetWatermarkStep();
        watermark.text = "CONFIDENTIAL";
        watermark.execute(ctx);
        document.close();

        PdfDocument result = new PdfDocument(new PdfReader(new ByteArrayInputStream(output.toByteArray())));
        String allText = "";
        for (int i = 1; i <= result.getNumberOfPages(); i++) allText += PdfTextExtractor.getTextFromPage(result.getPage(i));
        assertTrue(allText.contains("Table of Contents"));
        assertTrue(allText.contains("Introduction"));
        assertTrue(allText.contains("CONFIDENTIAL"));
        result.close();
    }
}
