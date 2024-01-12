package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.ErrorManager.Error;

public class VariableExpression extends Expression {
    String variableName;
    Type type;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public State evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        if (context.declaredVariables.contains(variableName)) {
            context.addError(
                    new Error(Error.ErrorType.INTERPRETER_ERROR, "Can't find variable "+variableName, true, 0)
            );
        }
        type = context.declaredVariables.get(variableName);
        return context;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return variableName;
    }
}
