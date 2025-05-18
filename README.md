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
