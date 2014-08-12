package org.librebiz.pureport.context;

import java.io.Serializable;
import java.util.Map;

public class Forward implements Serializable {
    private TextBuilder builder;
    private String expr;
    private int index;

    public Forward(TextBuilder builder, int index, String expr) {
        this.builder = builder;
        this.index = index;
        this.expr = expr;
    }

    public void resolve(ReportContext context) {
        String obj = context.evaluate(expr, String.class);
        Map attrs = builder.getAttributes(index);
        builder.delete(index, index + 1);
        if (obj != null) {
            builder.insert(index, obj, attrs);
        }
    }
}
