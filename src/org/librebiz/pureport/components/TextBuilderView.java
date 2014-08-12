package org.librebiz.pureport.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import javax.swing.JComponent;
import org.librebiz.pureport.context.TextBuilder;

public class TextBuilderView extends JComponent {
    private TextBuilder textBuilder = new TextBuilder();

    @Override
    protected void paintComponent(Graphics g) {
        if (textBuilder != null && textBuilder.length() > 0) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            FontRenderContext frc = g2d.getFontRenderContext();
            LineBreakMeasurer lbm = new LineBreakMeasurer(
                    textBuilder.getIterator(), frc);
            float width = getWidth()-15;
            float x = 8;
            float y = 0;
            while (lbm.getPosition() < textBuilder.length()) {
                TextLayout layout = lbm.nextLayout(width);
                if (lbm.getPosition() < textBuilder.length()) {
                    layout = layout.getJustifiedLayout(width);
                }
                y += layout.getAscent();
                layout.draw(g2d, x, y);
                y += layout.getDescent() + layout.getLeading();
            }
        }
    }

    public TextBuilder getTextBuilder() {
        return textBuilder;
    }

    public void setTextBuilder(TextBuilder newValue) {
        textBuilder = newValue;
        repaint();
    }
}
