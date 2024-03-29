package com.example.langlab.ErrorManager;


import com.example.langlab.IO.OutputBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ErrorManager {
    ArrayList<Error> errors = new ArrayList<>();
    OutputBuffer outputBuffer;

    public ErrorManager(OutputBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
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

        for (Error error : errors) {
            outputBuffer.println(error);
            if (!terse) {
                outputBuffer.printStackTrace();
            }
            // outputBuffer.println();
        }
        System.out.println(outputBuffer);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Error error : errors) {
            ret.append(error.toString());
        }
        return ret.toString();
    }

    public HashMap<Error.ErrorType, Boolean> getPresentErrorTypes() {
        HashMap<Error.ErrorType, Boolean> errorTypes = new HashMap<>();
        for (Error error : errors) {
            if (!errorTypes.getOrDefault(error.type, false)) {
                errorTypes.put(error.type, error.getIsFatal());
            }
        }
        return errorTypes;
    }
}
