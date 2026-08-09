package com.fyhao.springwebapps.business;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

@Service
public class PasswordprotectService {

	public void downloadPDF(String url, String pwd, ServletOutputStream os) {
		downloadPDF(url, pwd, "add", os);
	}

	public void downloadPDF(String url, String pwd, String operation, ServletOutputStream os) {
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> resp = restTemplate.getForEntity(url, byte[].class);
        ByteArrayInputStream bais = new ByteArrayInputStream(resp.getBody());
		transformPDF(bais, os, pwd, operation);
	}
	
	public void uploadpdfpasswordprotect(MultipartFile file, String pwd, ServletOutputStream os) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
		generatePDF(bais, os, pwd);
	}

	public void uploadPdf(MultipartFile file, String pwd, String operation, ServletOutputStream os) throws IOException {
		transformPDF(new ByteArrayInputStream(file.getBytes()), os, pwd, operation);
	}
	
	public void generatePDF(ByteArrayInputStream resourceStream, 
			ServletOutputStream os, String pwd) {
		transformPDF(resourceStream, os, pwd, "add");
	}

	public void transformPDF(ByteArrayInputStream resourceStream, ServletOutputStream os, String pwd, String operation) {
		try  (resourceStream)
        {
			boolean remove = "remove".equalsIgnoreCase(operation);
			WriterProperties properties = new WriterProperties();
			PdfReader reader = remove
					? new PdfReader(resourceStream, new ReaderProperties().setPassword(pwd.getBytes(java.nio.charset.StandardCharsets.UTF_8)))
					: new PdfReader(resourceStream);
			if (!remove) {
				byte[] password = pwd.getBytes(java.nio.charset.StandardCharsets.UTF_8);
				properties.setStandardEncryption(password, password, EncryptionConstants.ALLOW_PRINTING,
						EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA);
			}
            PdfWriter writer = new PdfWriter(os, properties);
            PdfDocument document = new PdfDocument(reader, writer);
            document.close();
        } catch( Exception ex) {
			throw new IllegalArgumentException("Unable to " + operation + " PDF password protection", ex);
        }
	}
}
