package org.librebiz.pureport.context;

public class EvaluationException extends RuntimeException {
    public EvaluationException(String message) {
        super(message);
    }

    public EvaluationException(String message, Throwable e) {
        super(message, e);
    }

    public EvaluationException(Throwable e) {
        super(getMessage(e), e);
    }

    private static String getMessage(Throwable e) {
        String message = e.getMessage();
        return message != null ? message : e.toString();
    }
}
