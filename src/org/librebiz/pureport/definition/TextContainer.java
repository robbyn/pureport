package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class TextContainer implements TextElement {
    private final List elements = new ArrayList();

    public int getCount() {
        return elements.size();
    }

    public TextElement get(int index) {
        return (TextElement)elements.get(index);
    }

    public void add(TextElement elm) {
        elements.add(elm);
    }

    public void remove(int index) {
        elements.remove(index);
    }

    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        for (Iterator it = elements.iterator(); it.hasNext(); ) {
            TextElement elm = (TextElement)it.next();
            elm.render(context, builder, fwds);
        }
    }
}
