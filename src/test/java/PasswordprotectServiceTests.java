import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import com.fyhao.springwebapps.business.PasswordprotectService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class PasswordprotectServiceTests {
    private byte[] samplePdf() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document(new PdfDocument(new PdfWriter(output)));
        document.add(new Paragraph("password test"));
        document.close();
        return output.toByteArray();
    }

    @Test
    void addsAndRemovesPassword() throws Exception {
        PasswordprotectService service = new PasswordprotectService();
        MockHttpServletResponse encryptedResponse = new MockHttpServletResponse();
        service.transformPDF(new ByteArrayInputStream(samplePdf()), encryptedResponse.getOutputStream(), "secret", "add");
        byte[] encrypted = encryptedResponse.getContentAsByteArray();
        assertThrows(Exception.class, () -> new PdfDocument(new PdfReader(new ByteArrayInputStream(encrypted))));

        MockHttpServletResponse plainResponse = new MockHttpServletResponse();
        service.transformPDF(new ByteArrayInputStream(encrypted), plainResponse.getOutputStream(), "secret", "remove");
        PdfDocument plain = new PdfDocument(new PdfReader(new ByteArrayInputStream(plainResponse.getContentAsByteArray())));
        assertEquals(1, plain.getNumberOfPages());
        plain.close();
    }
}
