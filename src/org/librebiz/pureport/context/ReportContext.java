package org.librebiz.pureport.context;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

public class ReportContext {
    private static final Logger LOG
            = Logger.getLogger(ReportContext.class.getName());
    private final ScriptContext scontext = new MyScriptContext();
    private final ScriptEngine engine;
    private int scopeLevel = ScriptContext.ENGINE_SCOPE;

    public ReportContext(String type) {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName(type);
        engine.setContext(scontext);
    }

    public void close() {
        // nothing
    }

    public void openScope() {
        scontext.setBindings(new SimpleBindings(), --scopeLevel);
    }

    public void closeScope() {
        scontext.setBindings(new SimpleBindings(), scopeLevel++);
    }

    public void define(String name, Object value) {
        scontext.setAttribute(name, value, scopeLevel);
    }

    public boolean evaluateCondition(String expr) {
        try {
            return (Boolean)engine.eval("!!(" + expr + ")");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public <T> T evaluate(String expr, Class<T> type) {
        try {
            Object result = engine.eval(expr);
            if (type == String.class) {
                if (result == null) {
                    result = "";
                } else {
                    result = result.toString();
                }
            }
            return type.cast(result);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void execute(String statement) {
        try {
            engine.eval(statement);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
