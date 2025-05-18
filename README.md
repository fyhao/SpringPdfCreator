# SpringPdfCreator

This project provides endpoints for PDF manipulation using Spring Boot and iText.

## Merge PDF Endpoint

`POST /pdf/mergepdf`

Accepts JSON body:

```json
{
  "urls": ["http://example.com/file1.pdf", "http://example.com/file2.pdf"]
}
```

Returns the merged PDF created via `MergeStep` workflow.

## Table of Contents Step

A `toc` step can be added to a workflow to create a simple table of contents page.
The step reads a list of entries from the `toc_entries` variable and writes them
into the PDF between page breaks.

Example usage in workflow JSON:

```json
{
  "steps": [
    {"action": "generate"},
    {"action": "toc"}
  ],
  "initsteps": [
    {"action": "setVar", "name": "html", "value": "<h1>Document</h1>"},
    {"action": "setVar", "name": "toc_entries", "value": ["Section 1", "Section 2"]}
  ]
}
```

The `toc` step will insert a new page with the title "Table of Contents" and the
specified entries.

## Test Cases

### ExtractImageService Test

A test case has been added to verify the functionality of the `ExtractImageService` class. This test ensures that images are correctly extracted from a PDF and saved into a ZIP file.

### PasswordprotectService Test

A test case has been added to verify the functionality of the `PasswordprotectService` class. This test ensures that a PDF can be password protected and the resulting PDF is correctly encrypted.
