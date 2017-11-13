package org.librebiz.pureport.context;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.UtilEvalError;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeanShellReportContext implements ReportContext {
    private final Interpreter interpreter = new Interpreter();
    private int scopeLevel = 0;

    public BeanShellReportContext(String type) {
    }

    @Override
    public void close() {
    }

    @Override
    public int openScope() {
        interpreter.setNameSpace(new NameSpace(
                interpreter.getNameSpace(), "level" + scopeLevel));
        return scopeLevel++;
    }

    @Override
    public void closeScope() {
        --scopeLevel;
        interpreter.setNameSpace(interpreter.getNameSpace().getParent());
    }

    @Override
    public void define(String name, Object value) {
        try {
            interpreter.set(name, value);
        } catch (EvalError e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public void define(String name, Object value, int level) {
        try {
            NameSpace ns = interpreter.getNameSpace();
            for (int i = scopeLevel-1; i > level; --i) {
                ns = ns.getParent();
            }
            ns.setVariable(name, value, false);
        } catch (UtilEvalError e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public <T> T evaluate(String expr, Class<T> type) {
        try {
            return type.cast(interpreter.eval(expr));
        } catch (EvalError e) {
            throw new EvaluationException(e);
        }
    }

    @Override
    public void execute(String statement) {
        try {
            interpreter.eval(statement);
        } catch (EvalError e) {
            throw new EvaluationException(e);
        }
    }
}
