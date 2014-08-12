package org.librebiz.pureport.run;

public class FormatterException extends RuntimeException {
    public FormatterException(String message) {
        super(message);
    }

    public FormatterException(String message, Throwable e) {
        super(message, e);
    }

    public FormatterException(Throwable e) {
        super(getMessage(e), e);
    }

    private static String getMessage(Throwable e) {
        String message = e.getMessage();
        return message != null ? message : e.toString();
    }
}
