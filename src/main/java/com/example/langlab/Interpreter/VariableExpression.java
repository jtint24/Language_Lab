package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;

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
        return null;
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
