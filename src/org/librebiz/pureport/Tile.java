package org.librebiz.pureport;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

public interface Tile {
    public Dimension2D format(double width, double height);
    public void draw(Graphics2D g, double x, double y, double w, double h);
}
