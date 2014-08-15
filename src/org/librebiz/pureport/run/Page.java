package org.librebiz.pureport.run;

import java.awt.Graphics2D;
import java.io.Serializable;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;

public class Page implements Serializable {
    private final BandInstance header;
    private final BandInstance footer;
    private final BandInstance content[];
    private final Forward forwards[];

    public Page(BandInstance header, BandInstance footer,
            BandInstance content[], Forward forwards[]) {
        this.header = header;
        this.footer = footer;
        this.content = content;
        this.forwards = forwards;
    }

    public void draw(Graphics2D g, double left, double top, double height) {
        double y = top;
        if (header != null) {
            header.draw(g, left, y);
            y += header.getHeight();
        }

        if (content != null && content.length > 0) {
            for (int i = 0; i < content.length; ++i) {
                BandInstance bi = content[i];
                bi.draw(g, left, y);
                y += bi.getHeight();
            }
        }

        if (footer != null) {
            y = top+height-footer.getHeight();
            footer.draw(g, left, y);
        }
    }

    public void resolveForwards(ReportContext context) {
        for (Forward fwd: forwards) {
            fwd.resolve(context);
        }
    }
}
