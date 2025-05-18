import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.step.SetWatermarkStep;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfStream;

public class SetWatermarkStepTests {

    private static PdfDocument createSamplePdf(ByteArrayOutputStream out) throws Exception {
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);
        doc.add(new Paragraph("test"));
        return pdf;
    }

    @Test
    public void addTextWatermark() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdf = createSamplePdf(out);
        Document doc = new Document(pdf);

        WFContext ctx = new WFContext();
        ctx.pdfDocument = pdf;
        ctx.document = doc;

        SetWatermarkStep step = new SetWatermarkStep();
        step.text = "WATERMARK";
        step.execute(ctx);
        doc.close();

        PdfDocument result = new PdfDocument(new PdfReader(new ByteArrayInputStream(out.toByteArray())));
        String text = PdfTextExtractor.getTextFromPage(result.getPage(1));
        assertTrue(text.contains("WATERMARK"));
        result.close();
    }

    @Test
    public void addImageWatermark() throws Exception {
        Path img = Files.createTempFile("wm", ".png");
        BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 10, 10);
        g.dispose();
        ImageIO.write(bi, "png", img.toFile());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdf = createSamplePdf(out);
        Document doc = new Document(pdf);

        WFContext ctx = new WFContext();
        ctx.pdfDocument = pdf;
        ctx.document = doc;

        SetWatermarkStep step = new SetWatermarkStep();
        step.url = img.toUri().toString();
        step.execute(ctx);
        doc.close();

        PdfDocument result = new PdfDocument(new PdfReader(new ByteArrayInputStream(out.toByteArray())));
        PdfDictionary resources = result.getPage(1).getPdfObject().getAsDictionary(PdfName.Resources);
        PdfDictionary xobjects = resources.getAsDictionary(PdfName.XObject);
        boolean foundImage = false;
        if (xobjects != null) {
            for (PdfName name : xobjects.keySet()) {
                PdfObject obj = xobjects.get(name);
                if (obj instanceof PdfStream) {
                    PdfStream st = (PdfStream) obj;
                    if (PdfName.Image.equals(st.getAsName(PdfName.Subtype))) {
                        foundImage = true;
                        break;
                    }
                }
            }
        }
        assertTrue(foundImage);
        result.close();
    }
}
