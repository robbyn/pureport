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
