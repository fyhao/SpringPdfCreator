package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

public class AddStep extends WFStep {

	public void execute(WFContext ctx) {
		ctx.vars.put(name, (String)ctx.vars.get(name) + ctx.replaceVars((String)ctx.vars.get(value)));
	}
}
