package org.librebiz.pureport.definition;

public class Iteration extends SectionContainer implements Section {
    private String id;
    private String indexId;
    private String collection;

    @Override
    public void accept(SectionVisitor visitor) {
        visitor.processIteration(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String newValue) {
        id = newValue;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String newValue) {
        collection = newValue;
    }
}
