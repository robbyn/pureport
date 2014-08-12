package org.librebiz.pureport.definition;

public interface Section {
    public void accept(SectionVisitor visitor);
}
