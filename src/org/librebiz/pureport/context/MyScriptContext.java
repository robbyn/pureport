package org.librebiz.pureport.context;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;

public class MyScriptContext implements ScriptContext {
    private static final List<Integer> SCOPES;
    private final Bindings[] stack = new Bindings[201];
    private Reader reader;
    private Writer writer;
    private Writer errorWriter;
    private int minScope = stack.length;

    public MyScriptContext() {
        stack[ENGINE_SCOPE] = new SimpleBindings();
        minScope = ENGINE_SCOPE;
        reader = new InputStreamReader(System.in);
        writer = new PrintWriter(System.out , true);
        errorWriter = new PrintWriter(System.err, true);
    }

    @Override
    public void setBindings(Bindings bindings, int scope) {
        stack[scope] = bindings;
        if (scope <= minScope) {
            while (scope < stack.length && stack[scope] == null) {
                ++scope;
            }
            minScope = scope;
        }
    }

    @Override
    public Bindings getBindings(int scope) {
        return stack[scope];
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        stack[scope].put(name, value);
    }

    @Override
    public Object getAttribute(String name, int scope) {
        Bindings bindings = stack[scope];
        return bindings == null ? null : bindings.get(name);
    }

    @Override
    public Object removeAttribute(String name, int scope) {
        Bindings bindings = stack[scope];
        if (bindings != null) {
            return bindings.remove(name);
        }
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        for (int i = minScope; i < stack.length; ++i) {
            Bindings bindings = stack[i];
            if (bindings != null && bindings.containsKey(name)) {
                return bindings.get(name);
            }
        }
        return null;
    }

    @Override
    public int getAttributesScope(String name) {
        for (int i = minScope; i < stack.length; ++i) {
            Bindings bindings = stack[i];
            if (bindings != null && bindings.containsKey(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Writer getWriter() {
        return writer;
    }

    @Override
    public Writer getErrorWriter() {
        return errorWriter;
    }

    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void setErrorWriter(Writer writer) {
        this.errorWriter = writer;
    }

    @Override
    public Reader getReader() {
        return reader;
    }

    @Override
    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public List<Integer> getScopes() {
        return SCOPES;
    }

    static {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= 200; ++i) {
            list.add(i);
        }
        SCOPES = Collections.unmodifiableList(list);
    }
}
