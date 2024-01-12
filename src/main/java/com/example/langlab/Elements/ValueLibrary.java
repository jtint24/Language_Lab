package com.example.langlab.Elements;

public class ValueLibrary {
    public static Type intType = new Type("int") {
        @Override
        public boolean matchesValue(Value v) {
            return false;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this;
        }
    };
    public static Type voidType = new Type("void") {
        @Override
        public boolean matchesValue(Value v) {
            return false;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this;
        }
    };
}
