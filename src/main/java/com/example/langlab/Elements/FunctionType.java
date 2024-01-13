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
            return subTypeOf(v.type);
        } else {
            return false;
        }
    }

    @Override
    public boolean subTypeOf(Type type) {
        if (type == ValueLibrary.anyType) {
            return true;
        }
        if (type instanceof FunctionType) {
            FunctionType ft = (FunctionType) type;
            for (int i = 0; i<parameterTypes.length; i++) {
                if (!parameterTypes[i].subTypeOf(ft.parameterTypes[i])) {
                    return false;
                }
            }
            return returnType.subTypeOf(ft.returnType);
        } else {
            return false;
        }
    }
}
