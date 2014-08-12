package org.librebiz.pureport.definition;

import java.util.HashMap;
import java.util.Map;

public class Report extends SectionContainer {
    private PageLayout pageLayout = new PageLayout();
    private Band reportHeader;
    private Band reportFooter;
    private Band pageHeader;
    private Band pageFooter;
    private String before;
    private String after;
    private Map<String,MacroDefinition> macros
            = new HashMap<String,MacroDefinition>();

    public Report() {
    }

    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(PageLayout newValue) {
        pageLayout = newValue;
    }

    public Band getReportHeader() {
        return reportHeader;
    }

    public void setReportHeader(Band newValue) {
        reportHeader = newValue;
    }

    public Band getReportFooter() {
        return reportFooter;
    }

    public void setReportFooter(Band newValue) {
        reportFooter = newValue;
    }

    public Band getPageHeader() {
        return pageHeader;
    }

    public void setPageHeader(Band newValue) {
        pageHeader = newValue;
    }

    public Band getPageFooter() {
        return pageFooter;
    }

    public void setPageFooter(Band newValue) {
        pageFooter = newValue;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String newValue) {
        before = newValue;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String newValue) {
        after = newValue;
    }

    public void addMacro(String name, MacroDefinition macro) {
        macros.put(name, macro);
    }

    public MacroDefinition getMacro(String name) {
        return macros.get(name);
    }
}
