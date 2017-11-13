package org.librebiz.pureport.context;

import java.io.Closeable;

public interface ReportContext extends Closeable {

    void closeScope();

    void define(String name, Object value);

    void define(String name, Object value, int level);

    <T> T evaluate(String expr, Class<T> type);

    void execute(String statement);

    int openScope();
    
}
