package org.librebiz.pureport.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportContextFactory {
    private static final Logger LOG = Logger.getLogger(ReportContextFactory.class.getName());

    public static ReportContext create(String type) {
        if (type == null || type.equalsIgnoreCase("bsh")
                || type.equalsIgnoreCase("beanshell")) {
            try {
                Class<?> thisClass = ReportContextFactory.class;
                String name = thisClass.getPackage().getName()
                        + ".BeanShellReportContext";
                Class<? extends ReportContext> clazz
                        = (Class<? extends ReportContext>)Class.forName(name);
                Constructor<? extends ReportContext> cons =
                        clazz.getConstructor(String.class);
                return cons.newInstance(type);
            } catch (ClassNotFoundException | NoSuchMethodException
                    | SecurityException | InstantiationException
                    | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return new DefaultReportContext(type);
    }
}
