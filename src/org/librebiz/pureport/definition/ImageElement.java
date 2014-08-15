package org.librebiz.pureport.definition;

import java.awt.font.GraphicAttribute;
import java.awt.font.TextAttribute;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ImageAttribute;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;
import org.librebiz.pureport.quantity.Quantity;
import org.librebiz.pureport.quantity.Unit;

public class ImageElement implements TextElement {
    private static final Logger LOG
            = Logger.getLogger(ImageElement.class.getName());

    public static final int TOP_ALIGNMENT    = GraphicAttribute.TOP_ALIGNMENT;
    public static final int BOTTOM_ALIGNMENT = GraphicAttribute.BOTTOM_ALIGNMENT;
    public static final int HANGING_BASELINE = GraphicAttribute.HANGING_BASELINE;
    public static final int CENTER_BASELINE  = GraphicAttribute.CENTER_BASELINE;
    public static final int ROMAN_BASELINE   = GraphicAttribute.ROMAN_BASELINE;

    private final String expression;
    private final int alignment;
    private final Quantity width;
    private final Quantity height;
    private final Quantity originX;
    private final Quantity originY;

    public ImageElement(String expression, int alignment,
            Quantity width, Quantity height, 
            Quantity originX, Quantity originY) {
        this.expression = expression;
        this.alignment = alignment;
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
    }

    public String getExpression() {
        return expression;
    }

    public int getAlignment() {
        return alignment;
    }

    @Override
    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        Object value = context.evaluate(expression, Object.class);
        Object attr = null;
        if (value instanceof GraphicAttribute) {
            attr = value;
        } else {
            URL url = toURL(value);
            if (url != null) {
                float w = width == null ? 0 : (float)width.getValue(Unit.PT);
                float h = height == null ? 0 : (float)height.getValue(Unit.PT);
                float orgx = (float)originX.getValue(Unit.PT);
                float orgy = (float)originY.getValue(Unit.PT);
                attr = new ImageAttribute(url, alignment, 
                        w, h, orgx, orgy);
            }
        }
        if (attr != null) {
            builder.setAttribute(TextAttribute.CHAR_REPLACEMENT, attr);
            builder.append('?');
            builder.setAttribute(TextAttribute.CHAR_REPLACEMENT, null);
        }
    }

    private static URL toURL(Object value) {
        try {
            if (value instanceof String) {
                value = new URL((String)value);
            }
            if (value instanceof URL) {
                return (URL)value;
            }
            if (value instanceof File) {
                return ((File)value).toURI().toURL();
            }
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } 
        return null;
    }
}
