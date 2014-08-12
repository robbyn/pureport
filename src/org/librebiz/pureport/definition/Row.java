package org.librebiz.pureport.definition;

import java.io.Serializable;
import org.librebiz.pureport.quantity.Quantity;

public class Row implements Serializable {
    private Quantity height;
    private Quantity topMargin;
    private Quantity bottomMargin;

    public Quantity getHeight() {
        return height;
    }

    public void setHeight(Quantity newValue) {
        height = newValue;
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
