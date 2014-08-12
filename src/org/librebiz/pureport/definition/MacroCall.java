package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.List;

public class MacroCall implements Section {
    private String name;
    private List<ActualArgument> arguments = new ArrayList<ActualArgument>();

    public void accept(SectionVisitor visitor) {
        visitor.processCall(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActualArgument[] getArguments() {
        return arguments.toArray(new ActualArgument[arguments.size()]);
    }

    public void addArgument(String name, String value) {
        arguments.add(new ActualArgument(name, value));
    }
}
