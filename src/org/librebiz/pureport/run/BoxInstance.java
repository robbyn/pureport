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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.Block;
import org.librebiz.pureport.definition.Box;
import org.librebiz.pureport.definition.Cell;
import org.librebiz.pureport.definition.StrokeInfo;
import org.librebiz.pureport.definition.TileExpression;
import org.librebiz.pureport.quantity.Unit;

public abstract class BoxInstance implements Serializable {
    protected Box box;
    private double height;

    public static BoxInstance getInstance(Box box) {
        if (box instanceof Block) {
            return new BlockInstance((Block)box);
        } else if (box instanceof Cell) {
            return new CellInstance((Cell)box);
        } else if (box instanceof TileExpression) {
            return new TileInstance((TileExpression)box);
        } else {
            throw new IllegalArgumentException("Unknown box class "
                    + box.getClass().getName());
        }
    }

    public BoxInstance(Box box) {
        this.box = box;
    }

    public double getHeight() {
        return height;
    }

    public abstract void evaluate(ReportContext context, List<Forward> fwds);

    public void format(FontRenderContext frc, double width, double maxHeight) {
        height = formatContent(frc,
                width-box.getHorizontalGap(Unit.PT),
                maxHeight-box.getVerticalGap(Unit.PT));
        height += box.getVerticalGap(Unit.PT);
    }

    public void draw(Graphics2D g, double x, double y, double w, double h) {
        double l = box.getLeftGap(Unit.PT);
        double r = box.getRightGap(Unit.PT);
        double t = box.getTopGap(Unit.PT);
        double b = box.getBottomGap(Unit.PT);
        drawBackground(g, x, y, w, h);
        drawContent(g, x+l, y+t, w-l-r, h-t-b);
        drawBorder(g, x, y, w, h);
    }

    protected abstract double formatContent(FontRenderContext frc, double w,
            double h);
    protected abstract void drawContent(Graphics2D g, double x, double y,
            double w, double h);

    protected void drawBackground(Graphics2D g, double x, double y, double w,
            double h) {
        double l = box.getLeftMargin() != null
                ? box.getLeftMargin().getValue(Unit.PT) : 0;
        double r = box.getRightMargin() != null
                ? box.getRightMargin().getValue(Unit.PT) : 0;
        double t = box.getTopMargin() != null
                ? box.getTopMargin().getValue(Unit.PT) : 0;
        double b = box.getBottomMargin() != null
                ? box.getBottomMargin().getValue(Unit.PT) : 0;
        x += l;
        y += t;
        w -= l+r;
        h -= t+b;
        if (box.getBackground() != null) {
            Color savedColor = g.getColor();
            g.setColor(box.getBackground());
            g.fill(new Rectangle2D.Double(x, y, w, h));
            g.setColor(savedColor);
        }
    }

    protected void drawBorder(Graphics2D g, double x, double y, double w,
            double h) {
        double l = box.getLeftMargin() != null
                ? box.getLeftMargin().getValue(Unit.PT) : 0;
        double r = box.getRightMargin() != null
                ? box.getRightMargin().getValue(Unit.PT) : 0;
        double t = box.getTopMargin() != null
                ? box.getTopMargin().getValue(Unit.PT) : 0;
        double b = box.getBottomMargin() != null
                ? box.getBottomMargin().getValue(Unit.PT) : 0;
        if (box.getLeftBorder() != null
                && box.getLeftBorder().getWidth() != null) {
            l += box.getLeftBorder().getWidth().getValue(Unit.PT)/2;
        }
        if (box.getRightBorder() != null
                && box.getRightBorder().getWidth() != null) {
            r += box.getRightBorder().getWidth().getValue(Unit.PT)/2;
        }
        if (box.getTopBorder() != null
                && box.getTopBorder().getWidth() != null) {
            t += box.getTopBorder().getWidth().getValue(Unit.PT)/2;
        }
        if (box.getBottomBorder() != null
                && box.getBottomBorder().getWidth() != null) {
            b += box.getBottomBorder().getWidth().getValue(Unit.PT)/2;
        }
        x += l;
        y += t;
        w -= l+r;
        h -= t+b;
        drawLine(g, x, y, x, y+h, box.getLeftBorder());
        drawLine(g, x, y, x+w, y, box.getTopBorder());
        drawLine(g, x+w, y, x+w, y+h, box.getRightBorder());
        drawLine(g, x, y+h, x+w, y+h, box.getBottomBorder());
    }

    private void drawLine(Graphics2D g, double x1, double y1, double x2,
            double y2, StrokeInfo si) {
        if (si != null) {
            Color savedColor = g.getColor();
            if (si.getColor() != null) {
                g.setColor(si.getColor());
            }
            Stroke savedStroke = g.getStroke();
            g.setStroke(createStroke(si));
            g.draw(new Line2D.Double(x1, y1, x2, y2));
            g.setStroke(savedStroke);
            g.setColor(savedColor);
        }
    }

    private Stroke createStroke(StrokeInfo si) {
        double w = si.getWidth() == null ? 0 : si.getWidth().getValue(Unit.PT);
        return new BasicStroke((float)w, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER);
    }
}
