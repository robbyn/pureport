package org.librebiz.pureport.definition;

import java.io.Serializable;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public interface TextElement extends Serializable {
    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds);
}
