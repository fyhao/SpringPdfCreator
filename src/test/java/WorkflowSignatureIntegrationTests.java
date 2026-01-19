import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WFStep;
import com.fyhao.springwebapps.wf.WorkflowExecutor;

/**
 * End-to-end integration test for the workflow engine with signature support
 */
public class WorkflowSignatureIntegrationTests {

    @Test
    public void testCompleteWorkflowWithSignature() throws Exception {
        // Create a complete workflow: generate HTML, add content, add signature
        WFRequest request = new WFRequest();
        
        // Step 1: Set HTML content variable
        WFStep setHtmlStep = new WFStep();
        setHtmlStep.action = "setVar";
        setHtmlStep.name = "html";
        setHtmlStep.value = "<html><body><h1>Document with Signature</h1><p>This document will have a signature field.</p></body></html>";
        request.initsteps.add(setHtmlStep);
        
        // Step 2: Generate PDF from HTML
        WFStep generateStep = new WFStep();
        generateStep.action = "generate";
        request.steps.add(generateStep);
        
        // Step 3: Add barcode
        WFStep barcodeStep = new WFStep();
        barcodeStep.action = "barcode";
        barcodeStep.text = "https://github.com/fyhao/SpringPdfCreator";
        request.steps.add(barcodeStep);
        
        // Step 4: Add signature field
        WFStep signatureStep = new WFStep();
        signatureStep.action = "signature";
        signatureStep.name = "AuthorSignature";
        signatureStep.value = "1"; // Add to first page
        request.steps.add(signatureStep);
        
        // Mock response
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) {
                baos.write(b);
            }
            
            @Override
            public boolean isReady() {
                return true;
            }
            
            @Override
            public void setWriteListener(javax.servlet.WriteListener listener) {
            }
        };
        Mockito.when(response.getOutputStream()).thenReturn(sos);
        
        // Execute workflow
        WorkflowExecutor.generatePdf(request, response);
        
        // Verify PDF was generated
        byte[] pdfBytes = baos.toByteArray();
        assertTrue(pdfBytes.length > 0, "PDF should be generated");
        assertTrue(pdfBytes[0] == 0x25 && pdfBytes[1] == 0x50 && pdfBytes[2] == 0x44 && pdfBytes[3] == 0x46, 
                   "Should be a valid PDF file");
    }
    
    @Test
    public void testMultipleSignatureFields() throws Exception {
        // Create a workflow with multiple signature fields
        WFRequest request = new WFRequest();
        
        // Set HTML content
        WFStep setHtmlStep = new WFStep();
        setHtmlStep.action = "setVar";
        setHtmlStep.name = "html";
        setHtmlStep.value = "<html><body><h1>Contract</h1><p>This contract requires multiple signatures.</p></body></html>";
        request.initsteps.add(setHtmlStep);
        
        // Generate PDF
        WFStep generateStep = new WFStep();
        generateStep.action = "generate";
        request.steps.add(generateStep);
        
        // Add first signature field
        WFStep sig1Step = new WFStep();
        sig1Step.action = "signature";
        sig1Step.name = "ClientSignature";
        sig1Step.value = "1";
        request.steps.add(sig1Step);
        
        // Add second signature field
        WFStep sig2Step = new WFStep();
        sig2Step.action = "signature";
        sig2Step.name = "VendorSignature";
        sig2Step.value = "1";
        request.steps.add(sig2Step);
        
        // Mock response
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream sos = new ServletOutputStream() {
            @Override
            public void write(int b) {
                baos.write(b);
            }
            
            @Override
            public boolean isReady() {
                return true;
            }
            
            @Override
            public void setWriteListener(javax.servlet.WriteListener listener) {
            }
        };
        Mockito.when(response.getOutputStream()).thenReturn(sos);
        
        // Execute workflow
        WorkflowExecutor.generatePdf(request, response);
        
        // Verify PDF was generated
        byte[] pdfBytes = baos.toByteArray();
        assertTrue(pdfBytes.length > 0, "PDF with multiple signatures should be generated");
    }
}
