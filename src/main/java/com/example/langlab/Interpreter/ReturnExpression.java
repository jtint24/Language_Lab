package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import javafx.scene.Node;

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
        // TODO: Validate type from context
        return null;
    }

    @Override
    public Type getType() {
        return ValueLibrary.voidType;
    }

    @Override
    public Node toNode() {
        // TODO
        return null;
    }

    @Override
    public String toString() {
        return "return "+returnedExpression;
    }
}
