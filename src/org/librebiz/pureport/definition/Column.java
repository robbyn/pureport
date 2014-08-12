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

import java.io.Serializable;
import org.librebiz.pureport.quantity.Quantity;

public class Column implements Serializable {
    private Quantity width;
    private Quantity leftMargin;
    private Quantity rightMargin;

    public Column() {
    }

    public Quantity getWidth() {
        return width;
    }

    public void setWidth(Quantity newValue) {
        width = newValue;
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
}
