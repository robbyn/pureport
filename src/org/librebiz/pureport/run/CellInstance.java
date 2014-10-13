package org.librebiz.pureport.run;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.Box;
import org.librebiz.pureport.definition.Cell;

public class CellInstance extends BoxInstance {
    private BoxInstance boxes[];

    public CellInstance(Cell cell) {
        super(cell);
    }

    public Cell getCell() {
        return (Cell)box;
    }

    public int getRow() {
        return getCell().getRow();
    }

    public int getRowSpan() {
        return getCell().getRowSpan();
    }

    public int getColumn() {
        return getCell().getColumn();
    }

    public int getColumnSpan() {
        return getCell().getColumnSpan();
    }

    @Override
    public void evaluate(ReportContext context, List<Forward> fwds) {
        List list = new ArrayList();
        for (int i = 0; i < getCell().getBoxCount(); ++i) {
            Box bx = getCell().getBox(i);
            boolean printBox = true;
            if (bx.getCondition() != null) {
                printBox = context.evaluateCondition(bx.getCondition());
            }
            if (printBox) {
                BoxInstance bi = BoxInstance.getInstance(bx);
                bi.evaluate(context, fwds);
                list.add(bi);
            }
        }
        boxes = (BoxInstance[])list.toArray(new BoxInstance[list.size()]);
    }

    @Override
    public double formatContent(FontRenderContext frc, double width,
            double maxHeight) {
        double height = 0;
        for (int i = 0; i < boxes.length; ++i) {
            BoxInstance block = boxes[i];
            block.format(frc, width, maxHeight-height);
            height += block.getHeight();
        }
        return height;
    }

    @Override
    public void drawContent(Graphics2D g, double x, double y,
            double w, double h) {
        if (getCell().getVerticalAlignment() == Cell.ALIGN_BOTTOM) {
            y += h-getHeight();
        } else if (getCell().getVerticalAlignment() == Cell.ALIGN_CENTER) {
            y += (h-getHeight())/2;
        }
        for (int i = 0; i < boxes.length; ++i) {
            BoxInstance block = boxes[i];
            block.draw(g, x, y, w, block.getHeight());
            y += block.getHeight();
        }
    }
}
