package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;

public class MetadataStep extends WFStep {

	public void execute(WFContext ctx) {
		PdfDocumentInfo info = ctx.pdfDocument.getDocumentInfo();
		if(ctx.vars.containsKey("metadata_title"))
			info.setTitle((String)ctx.vars.get("metadata_title"));
		if(ctx.vars.containsKey("metadata_author"))
			info.setAuthor((String)ctx.vars.get("metadata_author"));
		if(ctx.vars.containsKey("metadata_subject"))
			info.setSubject((String)ctx.vars.get("metadata_subject"));
		if(ctx.vars.containsKey("metadata_keywords"))
			info.setKeywords((String)ctx.vars.get("metadata_keywords"));
		if(ctx.vars.containsKey("metadata_creator"))
			info.setCreator((String)ctx.vars.get("metadata_creator"));
        info.addCreationDate();
        info.addModDate();
	}
}
