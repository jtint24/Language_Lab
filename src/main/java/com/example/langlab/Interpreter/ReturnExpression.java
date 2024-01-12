package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;

public class ReturnExpression extends Expression {
    Expression returnedExpression;
    public ReturnExpression(Expression convert) {
        super();
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
        return ValueLibrary.voidType;
    }

    @Override
    public String toString() {
        return "return "+returnedExpression;
    }
}
