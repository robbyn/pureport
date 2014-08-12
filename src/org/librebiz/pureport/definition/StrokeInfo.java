package org.librebiz.pureport.definition;

import java.awt.Color;
import java.io.Serializable;
import org.librebiz.pureport.quantity.Quantity;

public class StrokeInfo implements Serializable {
    private Quantity width;
    private int style;
    private Color color;

    public Quantity getWidth() {
        return width;
    }

    public void setWidth(Quantity width) {
        this.width = width;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
