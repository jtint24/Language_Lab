package com.example.langlab.Elements;

public class VoidValue extends Value {
    VoidValue() {
        super();
        type = ValueLibrary.voidType;
    }

    public String toString() {
        return "void";
    }
}
