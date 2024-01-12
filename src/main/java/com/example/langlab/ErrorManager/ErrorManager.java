package com.example.langlab.ErrorManager;


import com.example.langlab.IO.OutputBuffer;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
    ArrayList<Error> errors = new ArrayList<>();
    boolean suppress = false;
    OutputBuffer outputBuffer;

    public ErrorManager(OutputBuffer outputBuffer) {
        this(outputBuffer, false);
    }
    public ErrorManager(OutputBuffer outputBuffer, boolean suppress) {
        this.outputBuffer = outputBuffer;
        this.suppress = suppress;
    }

    public void logError(Error e) {
        errors.add(e);
        if (e.getIsFatal()) {
            killSession();
        }
    }

    public void logErrors(List<Error> newErrors) {

        errors.addAll(newErrors);

        for (Error error : newErrors) {
            if (error.getIsFatal()) {
                killSession();
            }
        }
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }
    public boolean hasErrors(Error.ErrorType type) {
        for (Error error : errors) {
            if (error.type == type) {
                return true;
            }
        }
        return false;
    }

    public void killSession() {
        printErrors();
        // System.exit(0);
        throw new RuntimeException();
    }

    public void printErrors() {
        printErrors(false);
    }

    public void printErrors(boolean terse) {
        // If terse is set to true, the stack traces won't be printed

        if (suppress) {
            return;
        }
        for (Error error : errors) {
            outputBuffer.println(error);
            if (!terse) {
                outputBuffer.printStackTrace();
            }
            // outputBuffer.println();
        }
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Error error : errors) {
            ret.append(error.toString());
        }
        return ret.toString();
    }
}
