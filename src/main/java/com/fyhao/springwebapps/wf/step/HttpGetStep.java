package com.fyhao.springwebapps.wf.step;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

public class HttpGetStep extends WFStep {

	public void execute(WFContext ctx) {
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
        ctx.vars.put(name, ctx.replaceVars(resp.getBody()));
	}
}
