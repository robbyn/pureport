/*
    Pureport, a report generator for Java
    Copyright (C) 2011  Maurice Perry <maurice@perry.ch>

    Project Web Site: http://code.google.com/p/pureport/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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

    public void evaluate(ReportContext context, List<Forward> fwds) {
        tile = context.evaluate(getTileExpr().getExpression(), Tile.class);
    }

    protected double formatContent(FontRenderContext frc,
            double width, double height) {
        if (tile == null) {
            return 0;
        } else {
            return tile.format(width, height).getHeight();
        }
    }

    protected void drawContent(Graphics2D g, double x, double y,
            double w, double h) {
        if (tile != null) {
            tile.draw(g, x, y, w, h);
        }
    }
}
