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
        generatePdf(context, report, file, null);
    }

    public static void generatePdf(ReportContext context, Report report,
            File file, FormatterListener listener) throws IOException {
        try (OutputStream out = new FileOutputStream(file)) {
            generatePdf(context, report, out, listener);
        }
    }

    public static void generatePdf(ReportContext context, Report report,
            OutputStream out) throws IOException {
        generatePdf(context, report, out, null);
    }

    public static void generatePdf(ReportContext context, Report report,
            OutputStream out, FormatterListener listener) throws IOException {
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
            if (listener != null) {
                fmt.addListener(listener);
            }
            fmt.format(pf);
            if (listener != null) {
                fmt.removeListener(listener);
            }
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
