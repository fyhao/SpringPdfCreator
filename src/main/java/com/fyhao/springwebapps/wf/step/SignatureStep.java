package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfSignatureFormField;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SignatureStep adds a signature field to the PDF document.
 * This creates a visible signature field that can be signed later.
 * 
 * Properties:
 * - name: Name of the signature field (default: "Signature")
 * - value: Page number (1-based) where signature should appear (default: 1)
 * - text: Optional comma-separated position/size "x,y,width,height" in points (default: "36,36,200,100")
 *         Example: "100,150,250,80" places signature at x=100, y=150 with width=250 and height=80
 */
public class SignatureStep extends WFStep {
    
    private static final Logger logger = LoggerFactory.getLogger(SignatureStep.class);
    
    // Default signature field dimensions
    private static final float DEFAULT_X = 36f;
    private static final float DEFAULT_Y = 36f;
    private static final float DEFAULT_WIDTH = 200f;
    private static final float DEFAULT_HEIGHT = 100f;

    @Override
    public void execute(WFContext ctx) {
        if (ctx.pdfDocument == null) {
            logger.warn("Cannot add signature field: PDF document is null");
            return;
        }
        
        try {
            // Get page number from value, default to 1
            int pageNum = 1;
            if (value != null && !value.trim().isEmpty()) {
                try {
                    pageNum = Integer.parseInt(ctx.replaceVars(value));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid page number '{}', using default page 1", value);
                }
            }
            
            // Ensure page exists
            if (pageNum < 1 || pageNum > ctx.pdfDocument.getNumberOfPages()) {
                logger.warn("Page number {} is out of range (1-{}), using page 1", 
                           pageNum, ctx.pdfDocument.getNumberOfPages());
                pageNum = 1;
            }
            
            // Get the PDF page
            PdfPage page = ctx.pdfDocument.getPage(pageNum);
            
            // Parse signature field position and size from text property
            Rectangle rect = parseRectangle();
            
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
            
            logger.info("Added signature field '{}' to page {}", fieldName, pageNum);
            
        } catch (Exception e) {
            logger.error("Failed to add signature field: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add signature field", e);
        }
    }
    
    /**
     * Parse rectangle from text property in format "x,y,width,height"
     * Returns default rectangle if text is null or invalid
     */
    private Rectangle parseRectangle() {
        if (text == null || text.trim().isEmpty()) {
            return new Rectangle(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
        
        try {
            String[] parts = text.split(",");
            if (parts.length == 4) {
                float x = Float.parseFloat(parts[0].trim());
                float y = Float.parseFloat(parts[1].trim());
                float width = Float.parseFloat(parts[2].trim());
                float height = Float.parseFloat(parts[3].trim());
                return new Rectangle(x, y, width, height);
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid rectangle format '{}', using default position", text);
        }
        
        return new Rectangle(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
