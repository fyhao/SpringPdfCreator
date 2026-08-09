package com.fyhao.springwebapps.wf.step;

import org.springframework.web.client.RestTemplate;
import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WFStep;

/** Downloads and executes another workflow's steps in the current context. */
public class HttpWorkflowStep extends WFStep {
    @Override
    public void execute(WFContext ctx) {
        WFRequest subflow = new RestTemplate().getForObject(ctx.replaceVars(url), WFRequest.class);
        if (subflow == null) throw new IllegalArgumentException("HTTP workflow response was empty");
        try {
            ctx.execute(subflow);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Unable to execute HTTP workflow", e);
        }
    }
}
