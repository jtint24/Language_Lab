package com.example.langlab.Interpreter;

public class DeclarationExpression extends Expression {
    String variableName;
    Expression assignTo;

    public DeclarationExpression(String variableName, Expression assignTo) {
        this.variableName = variableName;
        this.assignTo = assignTo;
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
        return "var "+variableName+" = "+assignTo;
    }
}
