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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.PageLayout;
import org.librebiz.pureport.definition.Report;
import org.librebiz.pureport.quantity.Unit;

public class PdfGenerator {
    public static void generatePdf(ReportContext context, Report report,
            File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            generatePdf(context, report, out);
        } finally {
            out.close();
        }
    }

    public static void generatePdf(ReportContext context, Report report,
            OutputStream out) throws IOException {
        try {
            PageStore pageStore = new PageStore();
            FontRenderContext frc = new FontRenderContext(null, true, true);
            Formatter fmt = new Formatter(report, context, frc, pageStore);
            PageLayout layout = report.getPageLayout();
            Rectangle2D bounds = layout.getBounds(Unit.PT);
            Rectangle2D drawable = layout.getDrawableArea(Unit.PT);
            Paper paper = new Paper();
            paper.setSize(bounds.getWidth(), bounds.getHeight());
            paper.setImageableArea(drawable.getX(), drawable.getY(),
                    drawable.getWidth(), drawable.getHeight());
            PageFormat pf = new PageFormat();
            pf.setOrientation(PageFormat.PORTRAIT);
            pf.setPaper(paper);
            fmt.format(pf);
            Document document = new Document(new Rectangle(
                    (float) bounds.getWidth(), (float) bounds.getHeight()));
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            for (int i = 0; i < pageStore.getPageCount(); ++i) {
                document.newPage();
                PdfContentByte pcb = writer.getDirectContent();
                Graphics2D g = pcb.createGraphics(
                        (float) pf.getWidth(), (float) pf.getHeight());
                Page page = pageStore.getPage(i);
                page.resolveForwards(context);
                page.draw(g, drawable.getX(), drawable.getY(),
                        drawable.getHeight());
                g.dispose();
            }
            document.close();
            writer.close();
            pageStore.close();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }
}
