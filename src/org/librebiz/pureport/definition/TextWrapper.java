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

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;

public class TextWrapper extends TextContainer {
    private Map attributes = new HashMap();

    public TextAttribute[] getKeys() {
        return (TextAttribute[])attributes.keySet().toArray(
                new TextAttribute[attributes.size()]);
    }

    public Object getAttribute(TextAttribute key) {
        return attributes.get(key);
    }

    public void setAttribute(TextAttribute key, Object value) {
        if (value == null) {
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    @Override
    public void render(ReportContext context, TextBuilder builder,
            List<Forward> fwds) {
        TextAttribute keys[] = getKeys();
        Object savedValues[] = new Object[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            savedValues[i] = builder.getAttribute(keys[i]);
            builder.setAttribute(keys[i], attributes.get(keys[i]));
        }
        super.render(context, builder, fwds);
        for (int i = keys.length-1; i >= 0; --i) {
            builder.setAttribute(keys[i], savedValues[i]);
        }
    }
}
