package org.librebiz.pureport.context;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TextBuilder implements Serializable {
    private static final Logger LOG
            = Logger.getLogger(TextBuilder.class.getName());

    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_JUSTIFY = 3;

    private StringBuilder charsBuffer = new StringBuilder();
    private List<Map<Attribute,Object>> attrsBuffer
            = new ArrayList<Map<Attribute,Object>>();
    private Map<Attribute,Object> attributes = new HashMap<Attribute,Object>();
    private boolean hasChanged = false;
    private double height;

    public Object getAttribute(TextAttribute key) {
        return attributes.get(key);
    }

    public void setAttribute(TextAttribute key, Object value) {
        if (!hasChanged) {
            attributes = new HashMap(attributes);
            hasChanged = true;
        }
        if (value == null) {
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    public int length() {
        return charsBuffer.length();
    }

    public char getChar(int ix) {
        return charsBuffer.charAt(ix);
    }

    public Map getAttributes(int ix) {
        return (Map)attrsBuffer.get(ix);
    }

    public void append(char c) {
        charsBuffer.append(c);
        attrsBuffer.add(attributes);
        hasChanged = false;
    }

    public void append(char chars[]) {
        append(chars, 0, chars.length);
    }

    public void append(char chars[], int offs, int len) {
        int end = offs+len;
        for (int i = offs; i < end; ++i) {
            append(chars[i]);
        }
    }

    public void append(String s) {
        append(s.toCharArray());
    }

    public void append(boolean value) {
        append(Boolean.toString(value));
    }

    public void append(int value) {
        append(Integer.toString(value));
    }

    public void append(long value) {
        append(Long.toString(value));
    }

    public void append(double value) {
        append(Double.toString(value));
    }

    public void append(Object obj) {
        append("" + obj);
    }

    public void delete(int start, int end) {
        charsBuffer.delete(start, end);
        for (int i = start; i < end; ++i) {
            attrsBuffer.remove(i);
        }
    }

    public void insert(int ix, char c, Map attrs) {
        charsBuffer.insert(ix, c);
        attrsBuffer.add(ix, attrs);
    }

    public void insert(int ix, char chars[], Map attrs) {
        insert(ix, chars, 0, chars.length, attrs);
    }

    public void insert(int ix, char chars[], int offs, int len, Map attrs) {
        int end = offs+len;
        for (int i = offs; i < end; ++i) {
            insert(ix, chars[i], attrs);
            ++ix;
        }
    }

    public void insert(int ix, String s, Map attrs) {
        insert(ix, s.toCharArray(), attrs);
    }

    public void insert(int ix, boolean value, Map attrs) {
        insert(ix, Boolean.toString(value), attrs);
    }

    public void insert(int ix, int value, Map attrs) {
        insert(ix, Integer.toString(value), attrs);
    }

    public void insert(int ix, long value, Map attrs) {
        insert(ix, Long.toString(value), attrs);
    }

    public void insert(int ix, double value, Map attrs) {
        insert(ix, Double.toString(value), attrs);
    }

    public void insert(int ix, Object obj, Map attrs) {
        insert(ix, "" + obj, attrs);
    }

    public AttributedCharacterIterator getIterator() {
        return new TextBuilderIterator();
    }

    public void format(FontRenderContext frc, double width) {
        height = 0;
        if (length() > 0) {
            LineBreakMeasurer lbm = new LineBreakMeasurer(getIterator(), frc);
            if (lbm.getPosition() < length()) {
                while (true) {
                    TextLayout layout = lbm.nextLayout((float)width);
                    height += layout.getAscent() + layout.getDescent();
                    if (lbm.getPosition() >= length()) {
                        break;
                    }
                    height += layout.getLeading();
                }
            }
        }
    }

    public double getHeight() {
        return height;
    }

    public void draw(Graphics2D g, double x, double y, double width, int align) {
        if (length() > 0) {
            LineBreakMeasurer lbm = new LineBreakMeasurer(getIterator(),
                    g.getFontRenderContext());
            if (lbm.getPosition() < length()) {
                while (true) {
                    TextLayout layout = lbm.nextLayout((float)width);
                    double xx = x;
                    if (align == ALIGN_RIGHT) {
                        xx += width-layout.getAdvance();
                    } else if (align == ALIGN_CENTER) {
                        xx += (width-layout.getAdvance())/2;
                    } else if (align == ALIGN_JUSTIFY && lbm.getPosition() < length()) {
                        layout = layout.getJustifiedLayout((float)width);
                    }
                    y += layout.getAscent();
                    layout.draw(g, (float)xx, (float)y);
                    y += layout.getDescent();
                    if (lbm.getPosition() >= length()) {
                        break;
                    }
                    y += layout.getLeading();
                }
            }
        }
    }

    private static boolean sameAttrs(Set keys, Map attrs1, Map attrs2) {
        if (attrs1 != attrs2) {
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                Object key = it.next();
                if (!same(attrs1.get(key), attrs2.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean sameAttr(Object key, Map attrs1, Map attrs2) {
        return attrs1 == attrs2 || same(attrs1.get(key), attrs2.get(key));
    }

    private static boolean same(Object a, Object b) {
        if (a == b) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else {
            return a.equals(b);
        }
    }

    private class TextBuilderIterator implements AttributedCharacterIterator {
        private int index;

        private TextBuilderIterator() {
            this(0);
        }

        private TextBuilderIterator(int index) {
            this.index = index;
        }

        @Override
        public int getRunStart() {
            Map attrs = getAttrs();
            int ix = index;
            do {
                --ix;
            } while (ix > getBeginIndex() && getAttrs(ix) == attrs);
            return ix+1;
        }

        @Override
        public int getRunStart(Set keys) {
            Map attrs = getAttrs();
            int ix = index;
            do {
                --ix;
            } while (ix > getBeginIndex() && sameAttrs(keys, getAttrs(ix), attrs));
            return ix+1;
        }

        @Override
        public int getRunStart(Attribute key) {
            Map attrs = getAttrs();
            int ix = index;
            do {
                --ix;
            } while (ix > getBeginIndex() && sameAttr(key, getAttrs(ix-1), attrs));
            return ix+1;
        }

        @Override
        public int getRunLimit() {
            Map attrs = getAttrs();
            int ix = index;
            do {
                ++ix;
            } while (ix < getEndIndex() && getAttrs(ix) == attrs);
            return ix;
        }

        @Override
        public int getRunLimit(Set keys) {
            Map attrs = getAttrs();
            int ix = index;
            do {
                ++ix;
            } while (ix < getEndIndex() && sameAttrs(keys, getAttrs(ix), attrs));
            return ix;
        }

        @Override
        public int getRunLimit(Attribute key) {
            Map attrs = getAttrs();
            int ix = index;
            do {
                ++ix;
            } while (ix < getEndIndex() && sameAttr(key, getAttrs(ix), attrs));
            return ix;
        }

        @Override
        public Map<Attribute,Object> getAttributes() {
            Map<Attribute,Object> attrs = getAttrs();
            Object attr = attrs.get(TextAttribute.CHAR_REPLACEMENT);
            if (attr instanceof ImageAttribute) {
                ImageAttribute iattr = (ImageAttribute)attr;
                attrs = new HashMap<Attribute,Object>(attrs);
                attrs.put(TextAttribute.CHAR_REPLACEMENT,
                        iattr.toGraphicAttribute());
            }
            return attrs;
        }

        private Map<Attribute,Object> getAttrs() {
            if (index < getBeginIndex() || index >= getEndIndex()) {
                return Collections.EMPTY_MAP;
            }
            return attrsBuffer.get(index);
        }

        private Map<Attribute,Object> getAttrs(int ix) {
            return attrsBuffer.get(ix);
        }

        @Override
        public Set getAllAttributeKeys() {
            return getAttrs().keySet();
        }

        @Override
        public Object getAttribute(Attribute key) {
            return getAttrs().get(key);
        }

        @Override
        public char current() {
            if (index < getBeginIndex() || index >= getEndIndex()) {
                return DONE;
            }
            return charsBuffer.charAt(index);
        }

        @Override
        public char first() {
            index = getBeginIndex();
            return current();
        }

        @Override
        public char last() {
            index = getEndIndex()-1;
            if (index < getBeginIndex()) {
                index = getBeginIndex();
            }
            return current();
        }

        @Override
        public char next() {
            if (index == getEndIndex()) {
                return DONE;
            }
            ++index;
            return current();
        }

        @Override
        public char previous() {
            if (index == getBeginIndex()) {
                return DONE;
            }
            --index;
            return current();
        }

        @Override
        public int getBeginIndex() {
            return 0;
        }

        @Override
        public int getEndIndex() {
            return charsBuffer.length();
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public char setIndex(int newValue) {
            if (newValue < getBeginIndex() || newValue > getEndIndex()) {
                throw new IllegalArgumentException("Invalid index " + newValue);
            }
            index = newValue;
            return index >= charsBuffer.length() ? DONE : charsBuffer.charAt(index);
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException ex) {
                LOG.log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex.getMessage());
            }
        }

    }
}
