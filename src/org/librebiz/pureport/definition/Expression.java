package org.librebiz.pureport.definition;

import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class Expression implements TextElement {
    private final String expression;
    private final boolean pass2;

    public Expression(String expression, boolean pass2) {
        this.expression = expression;
        this.pass2 = pass2;
    }

    public String getExpression() {
        return expression;
    }

    @Override
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
