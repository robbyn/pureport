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
package org.librebiz.pureport.definition;

import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class Expression implements TextElement {
    private String expression;
    private boolean pass2;

    public Expression(String expression, boolean pass2) {
        this.expression = expression;
        this.pass2 = pass2;
    }

    public String getExpression() {
        return expression;
    }

    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        if (pass2) {
            if (fwds == null) {
                builder.append('?');
            } else {
                int index = builder.length();
                builder.append('?');
                fwds.add(new Forward(builder, index, expression));
            }
        } else {
            String value = context.evaluate(expression, String.class);
            if (value != null) {
                builder.append(value);
            }
        }
    }
}
