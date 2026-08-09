import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.step.MergeStep;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;

public class MergeStepTests {

    private static void createPdf(Path path, int pages) throws Exception {
        PdfWriter writer = new PdfWriter(path.toFile());
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        for (int i = 0; i < pages; i++) {
            doc.add(new Paragraph("page" + (i + 1)));
            if (i < pages - 1) {
                doc.add(new AreaBreak());
            }
        }
        doc.close();
    }

    @Test
    public void mergeTwoPdfs() throws Exception {
        Path p1 = Files.createTempFile("merge1", ".pdf");
        Path p2 = Files.createTempFile("merge2", ".pdf");
        createPdf(p1, 1);
        createPdf(p2, 2);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument dest = new PdfDocument(new PdfWriter(out));
        WFContext ctx = new WFContext();
        ctx.pdfDocument = dest;

        MergeStep step = new MergeStep();
        step.action = "merge";
        step.urls = new String[] { p1.toUri().toString(), p2.toUri().toString() };
        step.execute(ctx);
        dest.close();

        PdfDocument result = new PdfDocument(new PdfReader(new ByteArrayInputStream(out.toByteArray())));
        assertEquals(3, result.getNumberOfPages());
        result.close();
    }
}
