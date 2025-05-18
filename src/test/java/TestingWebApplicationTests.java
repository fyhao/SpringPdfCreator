import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.fyhao.springwebapps.SpringWebMain;
import com.fyhao.springwebapps.TestController;
import com.fyhao.springwebapps.PdfController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringWebMain.class)
public class TestingWebApplicationTests {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestController controller;
	
	@Autowired
	private PdfController pdfController;
	
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(pdfController).isNotNull();
	}

	@Autowired
	private TestRestTemplate restTemplate;

    
    @Test
	public void pdfHomeShouldReturnPDFGenerationFormTitle() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/pdf/",
				String.class)).contains("PDF Generation Form");
	}
	
	@Test
	public void passwordProtectFromPdfShouldReturnProtectedPdf() throws Exception {
		String url = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
		String pwd = "password";
		PasswordprotectRequest request = new PasswordprotectRequest();
		request.url = url;
		request.pwd = pwd;
		
		byte[] response = this.restTemplate.postForObject("http://localhost:" + port + "/pdf/passwordprotectfrompdf", request, byte[].class);
		assertThat(response).isNotNull();
		assertThat(response.length).isGreaterThan(0);
	}
	
	@Test
	public void uploadPdfPasswordProtectShouldReturnProtectedPdf() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "dummy.pdf", "application/pdf", "dummy content".getBytes());
		String pwd = "password";
		
		byte[] response = this.restTemplate.postForObject("http://localhost:" + port + "/pdf/uploadpdfpasswordprotect?pwd=" + pwd, file.getBytes(), byte[].class);
		assertThat(response).isNotNull();
		assertThat(response.length).isGreaterThan(0);
	}
}
