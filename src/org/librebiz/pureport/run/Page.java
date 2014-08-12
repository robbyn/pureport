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
package org.librebiz.pureport.run;

import java.awt.Graphics2D;
import java.io.Serializable;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;

public class Page implements Serializable {
    private BandInstance header;
    private BandInstance footer;
    private BandInstance content[];
    private Forward forwards[];

    public Page(BandInstance header, BandInstance footer,
            BandInstance content[], Forward forwards[]) {
        this.header = header;
        this.footer = footer;
        this.content = content;
        this.forwards = forwards;
    }

    public void draw(Graphics2D g, double left, double top, double height) {
        double y = top;
        if (header != null) {
            header.draw(g, left, y);
            y += header.getHeight();
        }

        if (content != null && content.length > 0) {
            for (int i = 0; i < content.length; ++i) {
                BandInstance bi = content[i];
                bi.draw(g, left, y);
                y += bi.getHeight();
            }
        }

        if (footer != null) {
            y = top+height-footer.getHeight();
            footer.draw(g, left, y);
        }
    }

    public void resolveForwards(ReportContext context) {
        for (Forward fwd: forwards) {
            fwd.resolve(context);
        }
    }
}
