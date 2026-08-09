package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

public class SetVarStep extends WFStep {

	public void execute(WFContext ctx) {
		ctx.vars.put(name, ctx.replaceVars(value));
	}
}
