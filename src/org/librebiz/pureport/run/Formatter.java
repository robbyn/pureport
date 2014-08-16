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
    private final Report report;
    private final ReportContext context;
    private List<Forward> fwds;
    private final FontRenderContext frc;
    private double top;
    private double height;
    private double width;
    private double y;
    private BandInstance header;
    private BandInstance footer;
    private final List bands = new ArrayList();
    private PageFormat format;
    private final PageStore pageStore;
    private int topLevel;

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
        topLevel = context.openScope();
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
                @Override
                public void processBand(Band band) {
                    addBand(band);
                }

                @Override
                public void processIteration(Iteration iter) {
                    String id = getIterationId(iter);
                    Iterable col = getIterationCollection(iter);
                    int index = 0;
                    String indexId = iter.getIndexId();
                    for (Object elem: col) {
                        context.openScope();
                        try {
                            context.define(id, elem);
                            if (indexId != null) {
                                context.define(indexId, index);
                                ++index;
                            }
                            processContent(iter);
                        } finally {
                            context.closeScope();
                        }
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
                    try {
                        for (ActualArgument arg: call.getArguments()) {
                            context.define(arg.getName(),
                                    context.evaluate(arg.getValue(), Object.class));
                        }
                        processContent(def);
                    } finally {
                        context.closeScope();
                    }
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
        context.define("pageNumber", pageStore.getPageCount()+1, topLevel);
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
