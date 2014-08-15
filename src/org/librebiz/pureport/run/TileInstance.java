package org.librebiz.pureport.run;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.List;
import org.librebiz.pureport.Tile;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.TileExpression;

public class TileInstance extends BoxInstance {
    private Tile tile;

    public TileInstance(TileExpression tileExpr) {
        super(tileExpr);
    }

    public TileExpression getTileExpr() {
        return (TileExpression)box;
    }

    @Override
    public void evaluate(ReportContext context, List<Forward> fwds) {
        tile = context.evaluate(getTileExpr().getExpression(), Tile.class);
    }

    @Override
    protected double formatContent(FontRenderContext frc,
            double width, double height) {
        if (tile == null) {
            return 0;
        } else {
            return tile.format(width, height).getHeight();
        }
    }

    @Override
    protected void drawContent(Graphics2D g, double x, double y,
            double w, double h) {
        if (tile != null) {
            tile.draw(g, x, y, w, h);
        }
    }
}
