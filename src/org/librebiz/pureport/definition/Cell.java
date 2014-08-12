package org.librebiz.pureport.definition;

public class Cell extends BoxContainer {
    private int row;
    private int column;
    private int rowSpan = 1;
    private int columnSpan = 1;

    public int getRow() {
        return row;
    }

    public void setRow(int newValue) {
        row = newValue;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int newValue) {
        column = newValue;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int newValue) {
        rowSpan = newValue;
    }

    public int getColumnSpan() {
        return columnSpan;
    }

    public void setColumnSpan(int newValue) {
        columnSpan = newValue;
    }

    public boolean contains(int r, int c) {
        return r >= row && c >= column
                && r < row+rowSpan && c < column+columnSpan;
    }
}
