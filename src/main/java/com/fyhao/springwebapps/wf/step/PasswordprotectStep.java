package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

public class PasswordprotectStep  extends WFStep {

	public void execute(WFContext ctx) {
		String password = value;
		ctx.vars.put("USERPASS", password);
	}
}
