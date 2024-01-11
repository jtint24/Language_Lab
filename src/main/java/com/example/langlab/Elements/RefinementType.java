package com.example.langlab.Elements;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.Interpreter.State;

public class RefinementType extends Type {
    Type superType;

    Function condition;

    public RefinementType(Type superType, Function condition) {
        this.superType = superType;
        this.condition = condition;
    }

    @Override
    public boolean subtypeOf(Type superType) {
        if (superType instanceof UniverseType || superType==this) {
            return true;
        } else {
            return this.superType.subtypeOf(superType);
        }
    }

    @Override
    public boolean matchesValue(Value v, ErrorManager errorManager) {
        Value result = condition.apply(errorManager, new State(errorManager), v);
        return result==ValueLibrary.trueValue && superType.matchesValue(v, errorManager);
    }
}
