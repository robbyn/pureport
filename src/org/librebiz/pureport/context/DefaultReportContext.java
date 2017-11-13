package org.librebiz.pureport.context;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class DefaultReportContext implements ReportContext {
    private static final Logger LOG
            = Logger.getLogger(DefaultReportContext.class.getName());
    private final ScriptContext scontext = new MyScriptContext();
    private final ScriptEngine engine;
    private int scopeLevel = ScriptContext.ENGINE_SCOPE;

    public DefaultReportContext(String type) {
        engine = getEngine(type);
    }

    @Override
    public void close() {
        // nothing
    }

    public int getLevel() {
        return scopeLevel;
    }

    @Override
    public int openScope() {
        scontext.setBindings(new SimpleBindings(), --scopeLevel);
        LOG.log(Level.FINE, "openScope: {0}", scopeLevel);
        return scopeLevel;
    }

    @Override
    public void closeScope() {
        LOG.log(Level.FINE, "closeScope: {0}", scopeLevel);
        scontext.setBindings(null, scopeLevel++);
    }

    @Override
    public void define(String name, Object value) {
        define(name, value, scopeLevel);
    }

    @Override
    public void define(String name, Object value, int level) {
        LOG.log(Level.FINE, "define({0}:{2}): {1}",
                new Object[] {name, convert(value, String.class), level});
        scontext.setAttribute(name, value, level);
    }

    @Override
    public <T> T evaluate(String expr, Class<T> type) {
        try {
            Object result = engine.eval(expr, scontext);
            LOG.log(Level.FINE, "evaluate({0}:{2}): {1}",
                    new Object[] {expr, convert(result, String.class), scopeLevel});
            return convert(result, type);
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, null, e);
            throw e;
        } catch (ScriptException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void execute(String statement) {
        try {
            engine.eval(statement, scontext);
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, null, e);
            throw e;
        } catch (ScriptException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public <T> T convert(Object result, Class<T> type) {
        if (type == String.class) {
            if (result == null) {
                result = "";
            } else {
                result = result.toString();
            }
        } else if (type == Boolean.class || type == boolean.class) {
            if (result == null) {
                result = Boolean.FALSE;
            } else if (result instanceof Boolean) {
                // nothing
            } else if (result instanceof Number) {
                result = ((Number)result).doubleValue() != 0.0;
            } else {
                result = Boolean.TRUE;
            }
        }
        return type.cast(result);
    }

    private static ScriptEngine getEngine(String type) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine eng = manager.getEngineByName(type);
        if (eng == null) {
            eng = manager.getEngineByMimeType(type);
            if (eng == null) {
                throw new IllegalArgumentException(
                        "Script engine not found " + type);
            }
        }
        return eng;
    }
}
