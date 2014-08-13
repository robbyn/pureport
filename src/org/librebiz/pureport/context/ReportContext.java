package org.librebiz.pureport.context;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ReportContext {
    private final Context context;
    private Scriptable scope;
    private int scopeLevel = 0;

    public ReportContext() {
        context = Context.enter();
        scope = context.initStandardObjects();
    }

    public void close() {
        Context.exit();
    }

    public void openScope() {
        Scriptable newScope = context.newObject(scope);
        newScope.setParentScope(scope);
        scope = newScope;
        ++scopeLevel;
    }

    public void closeScope() {
        --scopeLevel;
        scope = scope.getParentScope();
    }

    public void define(String name, Object value) {
        Object jsObject = Context.javaToJS(value, scope);
        scope.put(name, scope, jsObject);
    }

    public boolean evaluateCondition(String expr) {
        Object result = context.evaluateString(scope, expr, "<cmd>", 1, null);
        return Context.toBoolean(result);
    }

    public <T> T evaluate(String expr, Class<T> type) {
        Object result = context.evaluateString(scope, expr, "<cmd>", 1, null);
        result = Context.jsToJava(result, type);
        return type.cast(result);
    }

    public void execute(String statement) {
        context.evaluateString(scope, statement, "<cmd>", 1, null);
    }
}
