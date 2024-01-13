package com.example.langlab.Elements;

public class FunctionType extends Type {
    Type returnType;
    Type[] parameterTypes;

    public FunctionType(Type returnType, Type... parameterTypes) {
        super(getTypeName(returnType, parameterTypes));
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    public static String getTypeName(Type returnType, Type... parameterTypes) {
        StringBuilder body = new StringBuilder();
        for (Type paramType : parameterTypes) {
            body.append(", ").append(paramType.name);
        }
        body = new StringBuilder(body.substring(2));
        return "("+body+")->"+returnType.name;
    }

    public Type getReturnType() {
        return returnType;
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
