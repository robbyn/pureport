package org.librebiz.pureport.reportfile;

public class ReportReaderException extends RuntimeException {
    public ReportReaderException(String message) {
        super(message);
    }

    public ReportReaderException(String message, Throwable e) {
        super(message, e);
    }

    public ReportReaderException(Throwable e) {
        super(getMessage(e), e);
    }

    private static String getMessage(Throwable e) {
        String message = e.getMessage();
        return message != null ? message : e.toString();
    }
}
