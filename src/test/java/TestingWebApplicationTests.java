

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.web.server.LocalServerPort;

import com.fyhao.springwebapps.SpringWebMain;
import com.fyhao.springwebapps.TestController;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SpringWebMain.class)
public class TestingWebApplicationTests {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestController controller;
	
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Autowired
	private TestRestTemplate restTemplate;

    
    @Test
	public void pdfHomeShouldReturnPDFGenerationFormTitle() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/pdf/",
				String.class)).contains("PDF Generation Form");
	}

	@Test
	public void workflowPdfShouldReturnPdf() {
		String json = "{\"steps\":[{\"action\":\"setVar\",\"name\":\"html\",\"value\":\"<h1>Workflow test</h1>\"},{\"action\":\"generate\"}]}";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<byte[]> result = restTemplate.postForEntity("http://localhost:" + port + "/pdf/workflowpdf",
				new HttpEntity<>(json, headers), byte[].class);
		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(result.getBody()).startsWith(new byte[] {'%', 'P', 'D', 'F'});
	}
}
