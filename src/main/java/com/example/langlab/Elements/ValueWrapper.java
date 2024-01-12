package com.example.langlab.Elements;

public class ValueWrapper<T> extends Value {
    T wrappedValue;
    public ValueWrapper(Type type, T wrappedValue) {
        this.wrappedValue = wrappedValue;
        this.type = type;
    }

    public String toString() {
        return wrappedValue.toString();
    }
}
