package com.example.langlab.Elements;

import java.util.Map;

public class BuiltinValue extends Value {
    Type type;

    public BuiltinValue(Type type) {
        super();
        this.type = type;
    }

    public String toString() {
        for (Map.Entry<String, Value> entry : ValueLibrary.builtinValues.entrySet()) {
            if (this == entry.getValue()) {
                return entry.getKey();
            }
        }
        return super.toString();
    }

    @Override
    public Type getType() {
        return type;
    }
}
