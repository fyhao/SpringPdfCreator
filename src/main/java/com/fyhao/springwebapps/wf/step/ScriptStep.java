package com.fyhao.springwebapps.wf.step;

import com.fyhao.springwebapps.wf.WFContext;
import com.fyhao.springwebapps.wf.WFStep;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;

public class ScriptStep extends WFStep {
    @Override
    public void execute(WFContext ctx) {
        if (value == null || value.trim().isEmpty()) return;
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("nashorn");
            SimpleBindings bindings = new SimpleBindings(ctx.vars);
            bindings.put("parseInt", (java.util.function.Function<String, Integer>) Integer::parseInt);
            bindings.put("ctx", ctx);
            engine.eval(value, bindings);
            // Copy all variables from engine bindings to ctx.vars
            for (Map.Entry<String, Object> entry : engine.getBindings(javax.script.ScriptContext.ENGINE_SCOPE).entrySet()) {
                ctx.vars.put(entry.getKey(), entry.getValue());
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
