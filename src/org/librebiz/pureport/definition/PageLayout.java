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
package org.librebiz.pureport.definition;

import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import org.librebiz.pureport.quantity.Quantity;
import org.librebiz.pureport.quantity.Unit;

public class PageLayout {
    public static final int LANDSCAPE = 0;
    public static final int PORTRAIT = 1;
    public static final int REVERSE_LANDSCAPE = 2;

    private int orientation = PageLayout.PORTRAIT;
    private Quantity paperWidth = new Quantity(21, Unit.CM);
    private Quantity paperHeight = new Quantity(29.7, Unit.CM);
    private Quantity leftMargin = new Quantity(1.5, Unit.CM);
    private Quantity rightMargin = new Quantity(1.5, Unit.CM);
    private Quantity topMargin = new Quantity(1.5, Unit.CM);
    private Quantity bottomMargin = new Quantity(1.5, Unit.CM);

    public PageLayout() {
    }

    public Rectangle2D getBounds(PageFormat format) {
        double w = format.getWidth();
        double h = format.getHeight();
        return new Rectangle2D.Double(0, 0, w, h);
    }

    public Rectangle2D getDrawableArea(PageFormat format) {
        double x = format.getImageableX();
        double y = format.getImageableY();
        double w = format.getImageableWidth();
        double h = format.getImageableHeight();
        return new Rectangle2D.Double(x, y, w, h);
    }

    public Rectangle2D getBounds(Unit unit) {
        double w = paperWidth.getValue(unit);
        double h = paperHeight.getValue(unit);
        return new Rectangle2D.Double(0, 0, w, h);
    }

    public Rectangle2D getDrawableArea(Unit unit) {
        double w = paperWidth.getValue(unit);
        double x = 0;
        if (leftMargin != null) {
            x = leftMargin.getValue(unit);
            w -= x;
        }
        if (rightMargin != null) {
            w -= rightMargin.getValue(unit);
        }
        double h = paperHeight.getValue(unit);
        double y = 0;
        if (topMargin != null) {
            y = topMargin.getValue(unit);
            h -= y;
        }
        if (bottomMargin != null) {
            h -= bottomMargin.getValue(unit);
        }
        return new Rectangle2D.Double(x, y, w, h);
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int newValue) {
        orientation = newValue;
    }

    public Quantity getPaperWidth() {
        return paperWidth;
    }

    public void setPaperWidth(Quantity newValue) {
        paperWidth = newValue;
    }

    public Quantity getPaperHeight() {
        return paperHeight;
    }

    public void setPaperHeight(Quantity newValue) {
        paperHeight = newValue;
    }

    public Quantity getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Quantity newValue) {
        leftMargin = newValue;
    }

    public Quantity getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Quantity newValue) {
        rightMargin = newValue;
    }

    public Quantity getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Quantity newValue) {
        topMargin = newValue;
    }

    public Quantity getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Quantity newValue) {
        bottomMargin = newValue;
    }
}
