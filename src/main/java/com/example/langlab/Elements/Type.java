package com.example.langlab.Elements;

public abstract class Type {
    String name;

    public Type(String name) {
        this.name = name;
    }
    public abstract boolean matchesValue(Value v);
    public abstract boolean subTypeOf(Type type);

    public String toString() {
        return name;
    }
}
