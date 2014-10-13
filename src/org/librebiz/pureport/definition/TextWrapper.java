package org.librebiz.pureport.definition;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class TextWrapper extends TextContainer {
    private final Map<TextAttribute,Object> attributes
            = new HashMap<TextAttribute,Object>();

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
        Map<TextAttribute,Object> savedAttrs
                = new HashMap<TextAttribute,Object>();
        for (Map.Entry<TextAttribute,Object> entry: attributes.entrySet()) {
            TextAttribute key = entry.getKey();
            Object value = entry.getValue();
            savedAttrs.put(key, builder.getAttribute(key));
            builder.setAttribute(key, value);
        }
        super.render(context, builder, fwds);
        for (Map.Entry<TextAttribute,Object> entry: savedAttrs.entrySet()) {
            TextAttribute key = entry.getKey();
            Object value = entry.getValue();
            builder.setAttribute(key, value);
        }
    }
}
