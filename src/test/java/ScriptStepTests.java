package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFRequest;
import com.fyhao.springwebapps.wf.WFStep;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class ScriptStepTests {
    @Test
    public void testScriptStepModifiesVars() throws IOException {
        WFContext ctx = new WFContext();
        WFRequest request = new WFRequest();

        WFStep step1 = new WFStep();
        step1.action = "setVar";
        step1.name = "var1";
        step1.value = "1";
        request.steps.add(step1);

        WFStep step2 = new WFStep();
        step2.action = "script";
        step2.value = "ctx.vars.put(\"var1\", (parseInt(var1) + 10) + \"\"); ctx.vars.put(\"var2\", 'scripted');";
        request.steps.add(step2);

        ctx.execute(request);
        assertEquals("11", ctx.vars.get("var1"));
        assertEquals("scripted", ctx.vars.get("var2"));
    }
}
