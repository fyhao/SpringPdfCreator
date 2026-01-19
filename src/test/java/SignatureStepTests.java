import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WFStep;
import com.fyhao.springwebapps.wf.step.SignatureStep;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class SignatureStepTests {

    @Test
    public void testSignatureFieldIsAdded() throws Exception {
        // Create a simple PDF document
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Test document for signature"));
        
        // Create context with the PDF
        WFContext ctx = new WFContext();
        ctx.pdfDocument = pdfDoc;
        ctx.document = doc;
        
        // Create and execute signature step
        SignatureStep step = new SignatureStep();
        step.action = "signature";
        step.name = "TestSignature";
        step.value = "1";
        step.execute(ctx);
        
        // Close document
        doc.close();
        
        // Read the PDF and check if signature field was added
        byte[] pdfBytes = baos.toByteArray();
        PdfDocument resultPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)));
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(resultPdf, false);
        
        assertNotNull(acroForm, "AcroForm should exist");
        PdfFormField signatureField = acroForm.getField("TestSignature");
        assertNotNull(signatureField, "Signature field should be added");
        assertEquals("TestSignature", signatureField.getFieldName().toString());
        
        resultPdf.close();
    }
    
    @Test
    public void testSignatureWithDefaultName() throws Exception {
        // Create a simple PDF document
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Test document"));
        
        // Create context with the PDF
        WFContext ctx = new WFContext();
        ctx.pdfDocument = pdfDoc;
        ctx.document = doc;
        
        // Create and execute signature step without name
        SignatureStep step = new SignatureStep();
        step.action = "signature";
        step.execute(ctx);
        
        // Close document
        doc.close();
        
        // Read the PDF and check if signature field was added with default name
        byte[] pdfBytes = baos.toByteArray();
        PdfDocument resultPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)));
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(resultPdf, false);
        
        assertNotNull(acroForm, "AcroForm should exist");
        PdfFormField signatureField = acroForm.getField("Signature");
        assertNotNull(signatureField, "Signature field with default name should be added");
        
        resultPdf.close();
    }
    
    @Test
    public void testSignatureViaWorkflow() throws Exception {
        // Create a workflow with signature step
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Workflow test"));
        
        WFContext ctx = new WFContext();
        ctx.pdfDocument = pdfDoc;
        ctx.document = doc;
        
        WFRequest request = new WFRequest();
        WFStep sigStep = new WFStep();
        sigStep.action = "signature";
        sigStep.name = "WorkflowSig";
        request.steps.add(sigStep);
        
        ctx.execute(request);
        doc.close();
        
        // Verify
        byte[] pdfBytes = baos.toByteArray();
        PdfDocument resultPdf = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdfBytes)));
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(resultPdf, false);
        
        assertNotNull(acroForm);
        PdfFormField signatureField = acroForm.getField("WorkflowSig");
        assertNotNull(signatureField, "Signature should be added via workflow");
        
        resultPdf.close();
    }
}
