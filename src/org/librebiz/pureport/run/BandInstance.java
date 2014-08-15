package org.librebiz.pureport.run;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.io.Serializable;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.Band;
import org.librebiz.pureport.definition.Column;
import org.librebiz.pureport.definition.Row;
import org.librebiz.pureport.quantity.Unit;

public class BandInstance implements Serializable {
    private final Band band;
    private final CellInstance cells[];
    private final double rowStart[];
    private double height;
    private final double columnStart[];

    public BandInstance(Band band) {
        this.band = band;
        cells = new CellInstance[band.getCellCount()];
        for (int i = 0; i < cells.length; ++i) {
            cells[i] = new CellInstance(band.getCell(i));
        }
        rowStart = new double[band.getRowCount()+1];
        columnStart = new double[band.getColumnCount()+1];
    }

    public double getHeight() {
        return height;
    }

    public void evaluate(ReportContext context, List<Forward> fwds) {
        if (band.getBefore() != null) {
            context.define("band", band);
            context.execute(band.getBefore());
        }
        for (int i = 0; i < cells.length; ++i) {
            CellInstance cell = cells[i];
            cell.evaluate(context, fwds);
        }
        if (band.getAfter() != null) {
            context.define("band", band);
            context.execute(band.getAfter());
        }
    }

    public void format(FontRenderContext frc, double width, double maxHeight) {
        computeColumns(width);
        double contentHeight = maxHeight;
        if (band.getBottomMargin() != null) {
            contentHeight -= band.getBottomMargin().getValue(Unit.PT);
        }
        double top = 0;
        if (band.getTopMargin() != null) {
            top = band.getTopMargin().getValue(Unit.PT);
        }
        for (int i = 0; i < rowStart.length; ++i) {
            rowStart[i] = top;
        }
        for (int i = 0; i < cells.length; ++i) {
            CellInstance cell = cells[i];
            int cs = cell.getColumn();
            int ce = cs+cell.getColumnSpan();
            int rs = cell.getRow();
            int re = rs+cell.getRowSpan();
            double start = columnStart[cs];
            double end = columnStart[ce];
            cell.format(frc, end-start, contentHeight-rowStart[rs]);
            double y = rowStart[rs] + cell.getHeight();
            if (y > rowStart[re]) {
                rowStart[re] = y;
            }
        }
        for (int i = 0; i < band.getRowCount(); ++i) {
            Row row = band.getRow(i);
            if (row.getHeight() != null) {
                double h = row.getHeight().getValue(Unit.PT);
                double delta = h - (rowStart[i+1]-rowStart[i]);
                if (delta > 0) {
                    for (int j = i+1; j < rowStart.length; ++j) {
                        rowStart[j] += delta;
                    }
                }
            }
        }
        height = rowStart[band.getRowCount()];
        if (band.getBottomMargin() != null) {
            height += band.getBottomMargin().getValue(Unit.PT);
        }
    }

    private void computeColumns(double width) {
        if (band.getLeftMargin() != null) {
            double left = band.getLeftMargin().getValue(Unit.PT);
            columnStart[0] = left;
            width -= left;
        } else {
            columnStart[0] = 0;
        }
        if (band.getRightMargin() != null) {
            width -= band.getRightMargin().getValue(Unit.PT);
        }
        int unsizedCols = 0;
        for (int i = 0; i < band.getColumnCount(); ++i) {
            Column col = band.getColumn(i);
            if (col.getWidth() != null) {
                width -= col.getWidth().getValue(Unit.PT);
            } else {
                ++unsizedCols;
            }
        }
        double unsizedWidth = 0;
        if (unsizedCols > 0) {
            unsizedWidth = width/unsizedCols;
        }
        for (int i = 0; i < band.getColumnCount(); ++i) {
            Column col = band.getColumn(i);
            double w = col.getWidth() == null 
                    ? unsizedWidth
                    : col.getWidth().getValue(Unit.PT);
            columnStart[i+1] = columnStart[i] + w;
        }
    }

    public void draw(Graphics2D g, double x, double y) {
        for (int i = 0; i < cells.length; ++i) {
            CellInstance cell = cells[i];
            int rs = cell.getRow();
            int re = rs+cell.getRowSpan();
            double dy = rowStart[rs];
            double h = rowStart[re]-dy;
            int cs = cell.getColumn();
            int ce = cs+cell.getColumnSpan();
            double dx = columnStart[cs];
            double w = columnStart[ce]-dx;
            cell.draw(g, x+dx, y+dy, w, h);
        }
    }
}
