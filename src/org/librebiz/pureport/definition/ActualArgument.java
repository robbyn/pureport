package org.librebiz.pureport.definition;

public class ActualArgument {
    private final String name;
    private final String value;

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
