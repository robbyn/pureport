package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.List;

public class Band extends Box implements Section {
    private List<Row> rows = new ArrayList<Row>();
    private List<Column> columns = new ArrayList<Column>();
    private List<Cell> cells = new ArrayList<Cell>();
    private String before;
    private String after;

    public Band() {
    }

    public void accept(SectionVisitor visitor) {
        visitor.processBand(this);
    }

    public Row[] getRows() {
        return rows.toArray(new Row[rows.size()]);
    }

    public int getRowCount() {
        return rows.size();
    }

    public Row getRow(int ix) {
        return rows.get(ix);
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public void removeRow(int ix) {
        rows.remove(ix);
    }

    public void removeAllRows() {
        rows.clear();
    }

    public Column[] getColumns() {
        return columns.toArray(new Column[columns.size()]);
    }

    public int getColumnCount() {
        return columns.size();
    }

    public Column getColumn(int ix) {
        return columns.get(ix);
    }

    public void addColumn(Column col) {
        columns.add(col);
    }

    public void removeColumn(int ix) {
        columns.remove(ix);
    }

    public void removeAllColumns() {
        columns.clear();
    }

    public int getCellCount() {
        return cells.size();
    }

    public Cell getCell(int i) {
        return cells.get(i);
    }

    public Cell[] getCells() {
        return cells.toArray(new Cell[cells.size()]);
    }

    public Cell getCellAt(int row, int col) {
        for (Cell cell: cells) {
            if (cell.contains(row, col)) {
                return cell;
            }
        }
        return null;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String newValue) {
        before = newValue;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String newValue) {
        after = newValue;
    }
}
