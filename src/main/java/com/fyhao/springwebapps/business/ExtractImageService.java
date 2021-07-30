package com.fyhao.springwebapps.business;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.RENDER_IMAGE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;

@Service
public class ExtractImageService {

	public ZipFile extractImageFromPdfUrl(String url) {
		return null;
	}
	
	public void processPDFImagesIntoZip(InputStream resourceStream, ZipOutputStream zipOS) throws IOException {
		String RESULT_FOLDER = "C:\\temp";
		try  (resourceStream)
        {
            PdfReader reader = new PdfReader(resourceStream);
            PdfDocument document = new PdfDocument(reader);
            PdfDocumentContentParser contentParser = new PdfDocumentContentParser(document);
            for (int page = 1; page <= document.getNumberOfPages(); page++)
            {
                contentParser.processContent(page, new IEventListener()
                {
                    @Override
                    public Set<EventType> getSupportedEvents()
                    {
                        return Collections.singleton(RENDER_IMAGE);
                    }
                    
                    @Override
                    public void eventOccurred(IEventData data, EventType type)
                    {
                        if (data instanceof ImageRenderInfo)
                        {
                            ImageRenderInfo imageRenderInfo = (ImageRenderInfo) data;
                            byte[] bytes = imageRenderInfo.getImage().getImageBytes();
                            ZipEntry zipEntry = new ZipEntry("images-" + index++ + ".jpg");
                            try {
								zipOS.putNextEntry(zipEntry);
								zipOS.write(bytes);
								zipOS.closeEntry();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                    }
                    
                    int index = 0;
                });
            }
        }
		
		zipOS.close();
	}
	
	public void processPDFImagesIntoZip(String srcpdf, String destzip) throws IOException {
		FileOutputStream zipfos = new FileOutputStream(new File(destzip));
		ZipOutputStream zipOS = new ZipOutputStream(zipfos);
		String RESULT_FOLDER = "C:\\temp";
		InputStream resourceStream = new FileInputStream(new File(srcpdf));
		processPDFImagesIntoZip(resourceStream, zipOS);
        zipfos.close();
	}
	
	public void downloadZip(String url, OutputStream os) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> resp = restTemplate.getForEntity(url, byte[].class);
        ByteArrayInputStream bais = new ByteArrayInputStream(resp.getBody());
        ZipOutputStream zipOS = new ZipOutputStream(os);
        processPDFImagesIntoZip(bais, zipOS);
	}
	
	public static void main(String args[]) throws IOException {
		ExtractImageService s = new ExtractImageService();
		
		String t = "<html><body><img src=https://i.stack.imgur.com/ILTQq.png /></body></html>";
		FileOutputStream fos = new FileOutputStream(new File("C:\\temp\\test.pdf"));
		HtmlConverter.convertToPdf(t, fos);
		
		s.processPDFImagesIntoZip("C:\\temp\\test.pdf", "C:\\temp\\out.zip");
		
	}
}
