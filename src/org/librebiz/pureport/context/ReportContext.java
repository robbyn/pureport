/*
    Pureport, a report generator for Java
    Copyright (C) 2011  Maurice Perry <maurice@perry.ch>

    Project Web Site: http://code.google.com/p/pureport/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.librebiz.pureport.context;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ReportContext {
    private Context context;
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
