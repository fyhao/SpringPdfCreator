import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletOutputStream;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fyhao.springwebapps.business.PasswordprotectService;

public class PasswordprotectServiceTests {

    @Test
    public void testDownloadPDF() throws Exception {
        PasswordprotectService service = new PasswordprotectService();
        String url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        String pwd = "password";
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletOutputStream os = response.getOutputStream();

        service.downloadPDF(url, pwd, os);

        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    public void testUploadpdfpasswordprotect() throws Exception {
        PasswordprotectService service = new PasswordprotectService();
        String pwd = "password";
        MockMultipartFile file = new MockMultipartFile("file", "dummy.pdf", "application/pdf", "dummy content".getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletOutputStream os = response.getOutputStream();

        service.uploadpdfpasswordprotect(file, pwd, os);

        assertTrue(response.getContentAsByteArray().length > 0);
    }

    @Test
    public void testGeneratePDF() throws Exception {
        PasswordprotectService service = new PasswordprotectService();
        ByteArrayInputStream bais = new ByteArrayInputStream("dummy content".getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MockServletOutputStream os = new MockServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                baos.write(b);
            }
        };

        service.generatePDF(bais, os, "password");

        assertTrue(baos.toByteArray().length > 0);
    }
}
