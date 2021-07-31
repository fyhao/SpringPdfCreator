package com.fyhao.springwebapps.business;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

@Service
public class PasswordprotectService {

	public void downloadPDF(String url, String pwd, ServletOutputStream os) {
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> resp = restTemplate.getForEntity(url, byte[].class);
        ByteArrayInputStream bais = new ByteArrayInputStream(resp.getBody());
        generatePDF(bais, os, pwd);
	}
	
	public void uploadpdfpasswordprotect(MultipartFile file, String pwd, ServletOutputStream os) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(file.getBytes());
		generatePDF(bais, os, pwd);
	}
	
	public void generatePDF(ByteArrayInputStream resourceStream, 
			ServletOutputStream os, String pwd) {
		try  (resourceStream)
        {
			WriterProperties properties = new WriterProperties();
			byte[] USERPASS = pwd.getBytes();
			byte[] OWNERPASS = pwd.getBytes();
			properties.setStandardEncryption(USERPASS, OWNERPASS, EncryptionConstants.ALLOW_PRINTING,
	                EncryptionConstants.ENCRYPTION_AES_128 | EncryptionConstants.DO_NOT_ENCRYPT_METADATA);
			
            PdfReader reader = new PdfReader(resourceStream);
            PdfWriter writer = new PdfWriter(os, properties);
            PdfDocument document = new PdfDocument(reader, writer);
            document.close();
        } catch( Exception ex) {
        	
        }
	}
}
