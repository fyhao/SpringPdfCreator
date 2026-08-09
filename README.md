# SpringPdfCreator

Spring Boot and React application for generating and manipulating PDFs with iText.

## Build and test

```bash
./gradlew test
npm ci
npm test -- --runInBand
npm run build
```

GitHub Actions runs the Java and React tests and creates the production React bundle on every push and pull request.

## Generate a PDF

`POST /pdf/workflowpdf` accepts a JSON workflow. Variables use `{{name}}` placeholders.

```json
{
  "metadata": {"title": "Example", "author": "SpringPdfCreator"},
  "steps": [
    {"action": "setVar", "name": "customer", "value": "Yong Hao"},
    {"action": "setVar", "name": "html", "value": "<h1>Hello {{customer}}</h1>"},
    {"action": "generate"},
    {"action": "toc", "entries": ["Introduction", "Details"]},
    {"action": "setWatermark", "text": "CONFIDENTIAL", "opacity": 0.25, "rotation": 45},
    {"action": "signature", "text": "Approved by Example"}
  ]
}
```

Supported actions include `setVar`, `add`, `generate`, `metadata`, `barcode`, `passwordprotect`, `setWatermark`, `toc`, `signature`, `merge`, `httpget`, `httpWorkflow`, and `script`. `httpWorkflow` downloads a JSON workflow and runs its steps as a subflow.

## PDF utilities

- `POST /pdf/mergepdf`: merge the PDF URLs in `{"urls": [...]}`.
- `POST /pdf/extractimagefrompdf`: extract images from a PDF URL into ZIP.
- `POST /pdf/uploadpdfextractimage`: extract images from an uploaded PDF.
- `POST /pdf/passwordprotectfrompdf`: use `operation: "add"` to encrypt or `operation: "remove"` to decrypt a URL-hosted PDF.
- `POST /pdf/uploadpdfpasswordprotect`: multipart upload with `file`, `pwd`, and `operation` (`add` or `remove`).

## Optional Firebase audit logging

Successful PDF operations are delivered asynchronously to Firebase Realtime Database when configured. Keep these values in Heroku/host environment configuration, never in source control:

- `FIREBASE_DATABASE_URL` — for example `https://project-id-default-rtdb.firebaseio.com`
- `FIREBASE_DATABASE_AUTH` — optional database auth token

When the URL is absent, auditing is disabled without affecting PDF generation.

## HTTP DB client protocol

Set `HTTP_DB_URL` to a JSON endpoint. `HttpDbService` supports `insert`, `queryRowByField`, `queryList`, `update`, `delete`, `insertMany`, and `updateMany`. Requests contain `action`, `entity`, and action-specific `data`/criteria fields; responses return a `data` field.

## Web pages

- Markdown editor: live preview, local autosave, snippets, metadata, and watermark.
- Password protection: upload or URL input with add/remove modes.
- Image extraction: upload or URL input with ZIP output.
