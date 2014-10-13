package org.librebiz.pureport.definition;

import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class TextString implements TextElement {
    private final String content;

    public TextString(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        builder.append(content);
    }
}
