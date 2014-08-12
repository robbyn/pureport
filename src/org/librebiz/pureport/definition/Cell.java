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
