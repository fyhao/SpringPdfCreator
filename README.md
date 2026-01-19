# SpringPdfCreator

This project provides endpoints for PDF manipulation using Spring Boot and iText.

## Workflow Engine

The workflow engine supports customization of PDF generation through JSON-based workflows. It can generate PDFs from HTML text and add various customizations like signatures, watermarks, barcodes, and more.

### Workflow Endpoints

#### POST Workflow
`POST /pdf/workflowpdf`

Accepts JSON workflow definition in the request body.

#### GET Workflow from External JSON
`GET /pdf/getpdf?url={workflow_json_url}`

Loads and executes workflow from an external JSON file.

### Available Workflow Steps

- **generate**: Generate PDF from HTML text
- **setVar**: Set variables for use in the workflow
- **signature**: Add signature field to PDF
- **barcode**: Add QR code barcode
- **setWatermark**: Add watermark (stub)
- **merge**: Merge multiple PDFs
- **metadata**: Set PDF metadata (title, author, subject, etc.)
- **passwordprotect**: Password protect the PDF
- **httpget**: Download content via HTTP
- **script**: Execute JavaScript code
- **add**: Concatenate variables

### Example Workflow with Signature

```json
{
  "initsteps": [
    {
      "action": "setVar",
      "name": "html",
      "value": "<html><body><h1>Contract Document</h1><p>This is a sample contract.</p></body></html>"
    }
  ],
  "steps": [
    {
      "action": "generate"
    },
    {
      "action": "barcode",
      "text": "https://example.com/document/12345"
    },
    {
      "action": "signature",
      "name": "ClientSignature",
      "value": "1"
    }
  ],
  "metadata": {
    "title": "Contract Document",
    "author": "System",
    "subject": "Legal Contract"
  }
}
```

### Signature Step

The `signature` step adds a signature field to the PDF that can be digitally signed later.

**Properties:**
- `name`: Name of the signature field (default: "Signature")
- `value`: Page number where signature should appear (default: 1)
- `text`: Optional position and size in format "x,y,width,height" in points (default: "36,36,200,100")

**Example with default position:**
```json
{
  "action": "signature",
  "name": "AuthorSignature",
  "value": "1"
}
```

**Example with custom position:**
```json
{
  "action": "signature",
  "name": "ClientSignature",
  "value": "1",
  "text": "100,150,250,80"
}
```

## Merge PDF Endpoint

`POST /pdf/mergepdf`

Accepts JSON body:

```json
{
  "urls": ["http://example.com/file1.pdf", "http://example.com/file2.pdf"]
}
```

Returns the merged PDF created via `MergeStep` workflow.
