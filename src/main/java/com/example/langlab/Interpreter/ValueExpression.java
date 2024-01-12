package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;
import com.example.langlab.MainApplication;
import javafx.scene.Node;

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
    public Node toNode() {
        return MainApplication.text(wrappedValue.toString());
    }

    @Override
    public String toString() {
        return wrappedValue.toString();
    }
}
