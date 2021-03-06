package com.upgrad.proman.service.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class ForbiddenException extends Exception {
    private final String code;
    private final String errorMessage;

    public ForbiddenException(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    public void printStackTrace() {
        super.printStackTrace();
    }
}
