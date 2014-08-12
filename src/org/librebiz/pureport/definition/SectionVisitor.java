package org.librebiz.pureport.definition;

public interface SectionVisitor {
    public void processBand(Band band);
    public void processIteration(Iteration iter);
    public void processChoose(Choose choose);
    public void processCall(MacroCall call);
    public void processScript(Script aThis);
}
