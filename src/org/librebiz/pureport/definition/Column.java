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
