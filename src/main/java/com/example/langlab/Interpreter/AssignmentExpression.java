package com.example.langlab.Interpreter;

public class AssignmentExpression extends Expression {
    String variableName;
    Expression toAssign;

    public AssignmentExpression(String variableName, Expression toAssign) {
        this.variableName = variableName;
        this.toAssign = toAssign;
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
        return variableName+" = "+toAssign;
    }
}
