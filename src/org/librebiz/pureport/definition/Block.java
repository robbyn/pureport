package org.librebiz.pureport.definition;

import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class Block extends Box {
    private final TextWrapper content = new TextWrapper();

    public Block() {
    }

    public TextWrapper getContent() {
        return content;
    }

    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        content.render(context, builder, fwds);
    }
}
