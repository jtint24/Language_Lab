package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;
import com.example.langlab.MainApplication;

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
        return context;
    }

    @Override
    public Type getType() {
        return wrappedValue.getType();
    }

    @Override
    public ValidationNodeResult getValidationNode() {
        return new ValidationNodeResult(MainApplication.text(wrappedValue.toString(), 24));
    }

    @Override
    public String toString() {
        return wrappedValue.toString();
    }
}
