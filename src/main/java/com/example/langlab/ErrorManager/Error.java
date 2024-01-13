package com.example.langlab.ErrorManager;

import com.example.langlab.Elements.Type;

public class Error {
    private final int errorLevel;
    private final String annotation;
    final ErrorType type;
    private final boolean isFatal;

    public Error(ErrorType type, String annotation, boolean isFatal) {
        this.annotation = annotation;
        this.type = type;
        this.errorLevel = 0;
        this.isFatal = isFatal;
    }

    public Error(ErrorType type, String annotation, boolean isFatal, int errorLevel) {
        this.annotation = annotation;
        this.type = type;
        this.errorLevel = errorLevel;
        this.isFatal = isFatal;
    }

    public static Error typeMismatch(Type type, Type type1) {
        return new Error(
                ErrorType.INTERPRETER_ERROR,
                "Expected expression of type "+type+", received type "+type1,
                true,
                0
        );
    }

    public boolean getIsFatal() {
        return isFatal;
    }

    public enum ErrorType {
        INPUT_ERROR,
        LEXER_ERROR,
        PARSER_ERROR,
        INTERPRETER_ERROR,
        RUNTIME_ERROR
    }

    @Override
    public String toString() {
        return type.toString() + "\n" + annotation;
    }
}
