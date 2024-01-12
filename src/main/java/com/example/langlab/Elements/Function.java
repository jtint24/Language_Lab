package com.example.langlab.Elements;

import com.example.langlab.ErrorManager.Error;
import com.example.langlab.ErrorManager.ErrorManager;

abstract public class Function extends Value {
    FunctionType type;
    public Function(FunctionType type) {
        this.type = type;
    }

    public Value apply(Value[] args, ErrorManager errorManager) {
        if (args.length != type.parameterTypes.length) {
            errorManager.logError(new Error(Error.ErrorType.RUNTIME_ERROR, "Expected "+type.parameterTypes.length+" args, got "+args.length, true, 0));
        }
        for (int i = 0; i<args.length; i++) {
            if (!type.parameterTypes[i].matchesValue(args[i])) {
                errorManager.logError(new Error(Error.ErrorType.RUNTIME_ERROR, "Expected "+ type.parameterTypes[i] +", got "+args[i].type, true, 0));
            }
        }
        Value result = prevalidatedApply(args, errorManager);
        if (!type.returnType.matchesValue(result)) {
            errorManager.logError(new Error(Error.ErrorType.RUNTIME_ERROR, "Expected "+type.returnType+" return type, got "+result.getType(), true, 0));
        }
        return result;
    }

    public abstract Value prevalidatedApply(Value[] args, ErrorManager errorManager);

    @Override
    public FunctionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "<Function>";
    }

}
