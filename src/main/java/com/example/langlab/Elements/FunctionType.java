package com.example.langlab.Elements;

public class FunctionType extends Type {
    Type returnType;
    Type[] parameterTypes;

    public FunctionType(String name) {
        super(name);
    }

    public Type getReturnType() {
        return getReturnType();
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }


    @Override
    public boolean matchesValue(Value v) {
        if (v instanceof Function) {
            Function fv = (Function) v;
            // TODO

            return true;


        } else {
            return false;
        }
    }

    @Override
    public boolean subTypeOf(Type type) {
        return false;
    }
}
