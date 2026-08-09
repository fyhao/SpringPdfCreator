package com.fyhao.springwebapps.wf.step;

import java.io.IOException;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

public class GenerateStep extends WFStep {

	public void execute(WFContext ctx) {
		try {
			ctx.generatePDF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
