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
import java.awt.font.FontRenderContext;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.context.TextBuilder;
import org.librebiz.pureport.definition.Block;

public class BlockInstance extends BoxInstance {
    private TextBuilder content;

    public BlockInstance(Block block) {
        super(block);
    }

    public Block getBlock() {
        return (Block)box;
    }

    public void evaluate(ReportContext context, List<Forward> fwds) {
        content = new TextBuilder();
        getBlock().render(context, content, fwds);
    }

    public double formatContent(FontRenderContext frc, double width, double height) {
        content.format(frc, width);
        return content.getHeight();
    }

    protected void drawContent(Graphics2D g, double x, double y, double w, double h) {
        content.draw(g, x, y, w, getBlock().getAlignment());
    }
}
