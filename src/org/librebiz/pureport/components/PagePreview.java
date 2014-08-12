package org.librebiz.pureport.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import javax.swing.JComponent;
import org.librebiz.pureport.definition.PageLayout;
import org.librebiz.pureport.quantity.Unit;
import org.librebiz.pureport.run.Page;

public class PagePreview extends JComponent {
    private PageLayout pageLayout;
    private Page page;
    private PageFormat format;

    @Override
    protected void paintComponent(Graphics g) {
        if (pageLayout != null && page != null) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            Rectangle2D rc = format == null
                    ? pageLayout.getBounds(Unit.PT)
                    : pageLayout.getBounds(format);
            double rx = (getWidth()-8)/rc.getWidth();
            double ry = (getHeight()-8)/rc.getHeight();
            double r = rx > ry ? ry : rx;
            double dx = (getWidth() - r*rc.getWidth())/2;
            double dy = (getHeight() - r*rc.getHeight())/2;
            g2d.translate(dx, dy);
            g2d.scale(r, r);
            g2d.setColor(Color.WHITE);
            g2d.fill(rc);
            g2d.setColor(Color.BLACK);
            g2d.draw(rc);
            rc = format == null
                    ? pageLayout.getDrawableArea(Unit.PT)
                    : pageLayout.getDrawableArea(format);
            page.draw(g2d, rc.getX(), rc.getY(), rc.getHeight());
        }
    }

    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(PageLayout newValue) {
        pageLayout = newValue;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page newValue) {
        page = newValue;
    }

    public PageFormat getPageFormat() {
        return format;
    }

    public void setPageFormat(PageFormat newValue) {
        format = newValue;
    }
}
