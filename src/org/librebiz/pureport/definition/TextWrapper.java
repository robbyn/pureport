package org.librebiz.pureport.definition;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class TextWrapper extends TextContainer {
    private final Map attributes = new HashMap();

    public TextAttribute[] getKeys() {
        return (TextAttribute[])attributes.keySet().toArray(
                new TextAttribute[attributes.size()]);
    }

    public Object getAttribute(TextAttribute key) {
        return attributes.get(key);
    }

    public void setAttribute(TextAttribute key, Object value) {
        if (value == null) {
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    @Override
    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        TextAttribute keys[] = getKeys();
        Object savedValues[] = new Object[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            savedValues[i] = builder.getAttribute(keys[i]);
            builder.setAttribute(keys[i], attributes.get(keys[i]));
        }
        super.render(context, builder, fwds);
        for (int i = keys.length-1; i >= 0; --i) {
            builder.setAttribute(keys[i], savedValues[i]);
        }
    }
}
