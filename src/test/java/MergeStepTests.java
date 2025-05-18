import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.fyhao.springwebapps.business.ExtractImageService;
import com.fyhao.springwebapps.business.PasswordprotectService;
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

    @Test
    public void testExtractImageService() throws Exception {
        ExtractImageService service = new ExtractImageService();
        Path pdfPath = Files.createTempFile("extractImage", ".pdf");
        createPdf(pdfPath, 1);

        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
        service.processPDFImagesIntoZip(Files.newInputStream(pdfPath), zipOut);

        // Verify that the ZIP file contains the extracted image
        ByteArrayInputStream zipIn = new ByteArrayInputStream(zipOut.toByteArray());
        java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(zipIn);
        java.util.zip.ZipEntry entry = zis.getNextEntry();
        assertEquals("images-0.jpg", entry.getName());
        zis.close();
    }

    @Test
    public void testPasswordprotectService() throws Exception {
        PasswordprotectService service = new PasswordprotectService();
        Path pdfPath = Files.createTempFile("passwordProtect", ".pdf");
        createPdf(pdfPath, 1);

        ByteArrayOutputStream protectedOut = new ByteArrayOutputStream();
        service.generatePDF(Files.newInputStream(pdfPath), protectedOut, "password");

        // Verify that the protected PDF can be opened with the password
        ByteArrayInputStream protectedIn = new ByteArrayInputStream(protectedOut.toByteArray());
        PdfReader reader = new PdfReader(protectedIn, new com.itextpdf.kernel.pdf.ReaderProperties().setPassword("password".getBytes()));
        PdfDocument protectedPdf = new PdfDocument(reader);
        assertEquals(1, protectedPdf.getNumberOfPages());
        protectedPdf.close();
    }
}
