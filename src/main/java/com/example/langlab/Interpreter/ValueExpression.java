package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;

public class ValueExpression extends Expression {
    Value wrappedValue;

    ValueExpression(Value wrappedType) {
        this.wrappedValue = wrappedType;
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
        return wrappedValue.toString();
    }
}
