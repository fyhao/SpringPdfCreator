import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.step.TocStep;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public class TocStepTests {

    @Test
    void addsConfiguredEntries() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdf);
        WFContext context = new WFContext();
        context.pdfDocument = pdf;
        context.document = document;

        TocStep step = new TocStep();
        step.text = "Contents";
        step.entries = List.of("Introduction", "Summary");
        step.execute(context);
        document.close();

        assertTrue(output.size() > 0);
    }

    @Test
    void requiresGenerateStepFirst() {
        TocStep step = new TocStep();
        assertThrows(IllegalStateException.class, () -> step.execute(new WFContext()));
    }
}
