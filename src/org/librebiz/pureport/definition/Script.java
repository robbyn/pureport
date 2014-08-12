package org.librebiz.pureport.definition;

public class Script implements Section {
    private String code;

    public Script(String code) {
        this.code = code;
    }

    public void accept(SectionVisitor visitor) {
        visitor.processScript(this);
    }

    public String getCode() {
        return code;
    }
}
