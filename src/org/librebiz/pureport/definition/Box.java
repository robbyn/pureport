package org.librebiz.pureport.definition;

import java.awt.Color;
import java.io.Serializable;
import org.librebiz.pureport.context.TextBuilder;
import org.librebiz.pureport.quantity.Quantity;
import org.librebiz.pureport.quantity.Unit;

public class Box implements Serializable {
    public static final int ALIGN_CENTER = TextBuilder.ALIGN_CENTER;
    public static final int ALIGN_LEFT = TextBuilder.ALIGN_LEFT;
    public static final int ALIGN_RIGHT = TextBuilder.ALIGN_RIGHT;
    public static final int ALIGN_JUSTIFY = TextBuilder.ALIGN_JUSTIFY;
    public static final int ALIGN_TOP = 1;
    public static final int ALIGN_BOTTOM = 2;

    private Quantity leftMargin;
    private Quantity rightMargin;
    private Quantity topMargin;
    private Quantity bottomMargin;
    private StrokeInfo leftBorder;
    private StrokeInfo rightBorder;
    private StrokeInfo topBorder;
    private StrokeInfo bottomBorder;
    private Quantity leftPadding;
    private Quantity rightPadding;
    private Quantity topPadding;
    private Quantity bottomPadding;
    private int alignment = ALIGN_LEFT;
    private int verticalAlignment = ALIGN_TOP;
    private Color background;
    private String condition;

    public Quantity getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Quantity leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Quantity getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Quantity rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Quantity getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Quantity topMargin) {
        this.topMargin = topMargin;
    }

    public Quantity getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Quantity bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public StrokeInfo getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(StrokeInfo leftBorder) {
        this.leftBorder = leftBorder;
    }

    public StrokeInfo getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(StrokeInfo rightBorder) {
        this.rightBorder = rightBorder;
    }

    public StrokeInfo getTopBorder() {
        return topBorder;
    }

    public void setTopBorder(StrokeInfo topBorder) {
        this.topBorder = topBorder;
    }

    public StrokeInfo getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(StrokeInfo bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    public Quantity getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(Quantity leftPadding) {
        this.leftPadding = leftPadding;
    }

    public Quantity getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(Quantity rightPadding) {
        this.rightPadding = rightPadding;
    }

    public Quantity getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(Quantity topPadding) {
        this.topPadding = topPadding;
    }

    public Quantity getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(Quantity bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public double getLeftGap(Unit unit) {
        return calculateGap(leftMargin, leftBorder, leftPadding, unit);
    }

    public double getRightGap(Unit unit) {
        return calculateGap(rightMargin, rightBorder, rightPadding, unit);
    }

    public double getHorizontalGap(Unit unit) {
        return getLeftGap(unit) + getRightGap(unit);
    }

    public double getTopGap(Unit unit) {
        return calculateGap(topMargin, topBorder, topPadding, unit);
    }

    public double getBottomGap(Unit unit) {
        return calculateGap(bottomMargin, bottomBorder, bottomPadding, unit);
    }

    public double getVerticalGap(Unit unit) {
        return getTopGap(unit) + getBottomGap(unit);
    }

    private double calculateGap(Quantity margin, StrokeInfo border,
            Quantity padding, Unit unit) {
        double gap = 0;
        if (margin != null) {
            gap += margin.getValue(unit);
        }
        if (border != null && border.getWidth() != null) {
            gap += border.getWidth().getValue(unit);
        }
        if (padding != null) {
            gap += padding.getValue(unit);
        }
        return gap;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color newValue) {
        background = newValue;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String newValue) {
        condition = newValue;
    }
}
