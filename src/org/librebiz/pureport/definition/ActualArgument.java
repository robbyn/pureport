package org.librebiz.pureport.definition;

public class ActualArgument {
    private String name;
    private String value;

    public ActualArgument(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
