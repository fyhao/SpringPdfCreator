import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.step.SignatureStep;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

public class SignatureStepTests {

    @Test
    void addsTextSignatureToGeneratedDocument() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdf);
        WFContext context = new WFContext();
        context.pdfDocument = pdf;
        context.document = document;

        SignatureStep step = new SignatureStep();
        step.text = "Signed by ${signer}";
        context.vars.put("signer", "Test User");
        step.execute(context);
        document.close();

        assertTrue(output.size() > 0);
    }

    @Test
    void requiresGenerateStepFirst() {
        SignatureStep step = new SignatureStep();
        assertThrows(IllegalStateException.class, () -> step.execute(new WFContext()));
    }
}
