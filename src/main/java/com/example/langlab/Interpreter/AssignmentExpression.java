package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;

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
    public Type getType() {
        return toAssign.getType();
    }

    @Override
    public String toString() {
        return variableName+" = "+toAssign;
    }
}
