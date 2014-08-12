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

import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.librebiz.pureport.context.Forward;
import org.librebiz.pureport.context.ReportContext;
import org.librebiz.pureport.definition.ActualArgument;
import org.librebiz.pureport.definition.Band;
import org.librebiz.pureport.definition.Choose;
import org.librebiz.pureport.definition.Iteration;
import org.librebiz.pureport.definition.MacroCall;
import org.librebiz.pureport.definition.MacroDefinition;
import org.librebiz.pureport.definition.PageLayout;
import org.librebiz.pureport.definition.Report;
import org.librebiz.pureport.definition.Script;
import org.librebiz.pureport.definition.Section;
import org.librebiz.pureport.definition.SectionContainer;
import org.librebiz.pureport.definition.SectionVisitor;
import org.librebiz.pureport.definition.When;
import org.librebiz.pureport.quantity.Unit;

public class Formatter {
    private Report report;
    private ReportContext context;
    private List<Forward> fwds;
    private FontRenderContext frc;
    private double top;
    private double height;
    private double width;
    private double y;
    private BandInstance header;
    private BandInstance footer;
    private List bands = new ArrayList();
    private PageFormat format;
    private PageStore pageStore;

    public Formatter(Report report, ReportContext context,
            FontRenderContext frc, PageStore pageStore) {
        this.report = report;
        this.context = context;
        this.frc = frc;
        this.pageStore = pageStore;
    }

    public void format(PageFormat fmt) {
        format = fmt;
        pageStore.clear();
        context.openScope();
        try {
            computeMargins();
            if (report.getBefore() != null) {
                context.execute(report.getBefore());
            }
            startPage();
            addBand(report.getReportHeader());
            processContent(report);
            addBand(report.getReportFooter());
            endPage();
            if (report.getAfter() != null) {
                context.execute(report.getAfter());
            }
        } finally {
            context.closeScope();
        }
        context.define("pageCount", pageStore.getPageCount());
    }

    public PageLayout getPageLayout() {
        return report.getPageLayout();
    }

    public PageFormat getPageFormat() {
        return format;
    }

    public ReportContext getContext() {
        return context;
    }

    private void processContent(SectionContainer container) {
        for (Section section: container.getContent()) {
            section.accept(new SectionVisitor() {
                public void processBand(Band band) {
                    addBand(band);
                }

                public void processIteration(Iteration iter) {
                    String id = getIterationId(iter);
                    Iterable col = getIterationCollection(iter);
                    int index = 0;
                    String indexId = iter.getIndexId();
                    for (Object elem: col) {
                        context.openScope();
                        context.define(id, elem);
                        if (indexId != null) {
                            context.define(indexId, index);
                            ++index;
                        }
                        processContent(iter);
                        context.closeScope();
                    }
                }

                public void processChoose(Choose choose) {
                    for (When branch: choose.getBranches()) {
                        String cond = branch.getCondition();
                        if (cond == null
                                || context.evaluateCondition(cond)) {
                            processContent(branch);
                            break;
                        }
                    }
                }

                public void processCall(MacroCall call) {
                    MacroDefinition def = report.getMacro(call.getName());
                    if (def == null) {
                        throw new RuntimeException("Macro not found "
                                + call.getName());
                    }
                    context.openScope();
                    for (ActualArgument arg: call.getArguments()) {
                        context.define(arg.getName(),
                                context.evaluate(arg.getValue(), Object.class));
                    }
                    processContent(def);
                    context.closeScope();
                }

                public void processScript(Script script) {
                    context.execute(script.getCode());
                }
            });
        }
    }

    private void computeMargins() {
        PageLayout layout = report.getPageLayout();
        Rectangle2D rc = format == null 
                ? layout.getDrawableArea(Unit.PT) : layout.getDrawableArea(format);
        top = rc.getY();
        width = rc.getWidth();
        height = rc.getHeight();
    }

    private String getIterationId(Iteration iter) {
        String id = iter.getId();
        return id == null ? "current" : id;
    }

    private Iterable getIterationCollection(Iteration iter) {
        Object obj = context.evaluate(iter.getCollection(), Object.class);
        if (obj instanceof Iterable) {
            return (Iterable)obj;
        } else if (obj instanceof Object[]) {
            return Arrays.asList((Object[])obj);
        } else {
            throw new FormatterException("Collection required for iteration");
        }
    }

    private void startPage() {
        fwds = new ArrayList<Forward>();
        context.define("pageNumber", pageStore.getPageCount()+1);
        header = null;
        y = top;
        if (report.getPageHeader() != null) {
            header = new BandInstance(report.getPageHeader());
            header.evaluate(context, fwds);
            header.format(frc, width, height);
            y += header.getHeight();
        }
        footer = null;
        if (report.getPageFooter() != null) {
            footer = new BandInstance(report.getPageFooter());
        }
        bands.clear();
    }

    private void endPage() {
        if (footer != null) {
            footer.evaluate(context, fwds);
            footer.format(frc, width, height+top-y);
        }
        BandInstance content[] = (BandInstance[])bands.toArray(
                new BandInstance[bands.size()]);
        Page page = new Page(header, footer, content, fwds.toArray(new Forward[fwds.size()]));
        pageStore.addPage(page);
    }

    private void addBand(Band band) {
        if (band != null) {
            boolean printBand = true;
            if (band.getCondition() != null) {
                printBand = context.evaluateCondition(band.getCondition());
            }
            if (printBand) {
                BandInstance bi = new BandInstance(band);
                bi.evaluate(context, fwds);
                double mh = height+top-y;
                double footerHeight = 0;
                if (footer != null) {
                    footer.evaluate(context, fwds);
                    footer.format(frc, width, mh);
                    footerHeight = footer.getHeight();
                    mh -= footerHeight;
                }
                bi.format(frc, width, mh);
                double end = top + height - footerHeight;
                double next = y + bi.getHeight();
                if (next > end) {
                    endPage();
                    startPage();
                }
                bands.add(bi);
                y += bi.getHeight();
            }
        }
    }
}
