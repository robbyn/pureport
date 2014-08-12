package org.librebiz.pureport.definition;

import java.util.ArrayList;
import java.util.List;

public class Choose implements Section {
    private List<When> branches = new ArrayList<When>();

    public When[] getBranches() {
        return branches.toArray(new When[branches.size()]);
    }

    public void addBranch(When branch) {
        branches.add(branch);
    }

    public void accept(SectionVisitor visitor) {
        visitor.processChoose(this);
    }
}
