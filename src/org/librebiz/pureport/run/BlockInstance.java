package org.librebiz.pureport.run;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;
import org.librebiz.pureport.definition.Block;

public class BlockInstance extends BoxInstance {
    private TextBuilder content;

    public BlockInstance(Block block) {
        super(block);
    }

    public Block getBlock() {
        return (Block)box;
    }

    @Override
    public void evaluate(ReportContext context, List<Forward> fwds) {
        content = new TextBuilder();
        getBlock().render(context, content, fwds);
    }

    @Override
    public double formatContent(FontRenderContext frc, double width, double height) {
        content.format(frc, width);
        return content.getHeight();
    }

    @Override
    protected void drawContent(Graphics2D g, double x, double y, double w, double h) {
        content.draw(g, x, y, w, getBlock().getAlignment());
    }
}
