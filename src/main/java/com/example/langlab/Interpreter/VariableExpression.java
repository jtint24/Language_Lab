package com.example.langlab.Interpreter;

public class VariableExpression extends Expression {
    String variableName;

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
    public String toString() {
        return variableName;
    }
}
