package com.example.langlab.Elements;

public class ValueLibrary {
    public static Type intType = new Type() {
        @Override
        public boolean matchesValue(Value v) {
            return false;
        }
    };
    public static Type voidType = new Type() {
        @Override
        public boolean matchesValue(Value v) {
            return false;
        }
    };
}
