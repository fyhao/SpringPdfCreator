package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfSignatureFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;

/**
 * SignatureStep adds a signature field to the PDF document.
 * This creates a visible signature field that can be signed later.
 * 
 * Properties:
 * - name: Name of the signature field
 * - text: Optional text to display (defaults to "Signature")
 * - value: Page number (1-based) where signature should appear. Defaults to first page.
 */
public class SignatureStep extends WFStep {

    @Override
    public void execute(WFContext ctx) {
        if (ctx.pdfDocument == null) {
            return;
        }
        
        try {
            // Get page number from value, default to 1
            int pageNum = 1;
            if (value != null && !value.trim().isEmpty()) {
                try {
                    pageNum = Integer.parseInt(ctx.replaceVars(value));
                } catch (NumberFormatException e) {
                    // Use default page 1
                }
            }
            
            // Ensure page exists
            if (pageNum < 1 || pageNum > ctx.pdfDocument.getNumberOfPages()) {
                pageNum = 1;
            }
            
            // Get the PDF page
            PdfPage page = ctx.pdfDocument.getPage(pageNum);
            
            // Define signature field position (bottom left corner with some margin)
            Rectangle rect = new Rectangle(36, 36, 200, 100);
            
            // Get or create the AcroForm
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(ctx.pdfDocument, true);
            
            // Create signature field
            String fieldName = name != null ? ctx.replaceVars(name) : "Signature";
            PdfSignatureFormField signatureField = PdfFormField.createSignature(
                ctx.pdfDocument, rect);
            signatureField.setFieldName(fieldName);
            signatureField.getWidgets().get(0).setPage(page);
            
            // Add the field to the form
            acroForm.addField(signatureField);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
