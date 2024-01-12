package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.Interpreter.ExpressionResult;
import com.example.langlab.Interpreter.Expressions.Expression;
import com.example.langlab.Interpreter.State;
import com.example.langlab.Interpreter.ValidationContext;
import com.example.langlab.Interpreter.ValidationNodeResult;
import com.example.langlab.MainApplication;
import javafx.scene.layout.HBox;

public class ReturnExpression extends Expression {
    Expression returnedExpression;
    public ReturnExpression(Expression convert) {
        super();
    }

    @Override
    public ExpressionResult evaluate(State s) {
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
    public ValidationNodeResult getValidationNode() {
        HBox coreBox = new HBox(MainApplication.text("return ", 24), returnedExpression.getValidationNode().toNode());
        // TODO
        return new ValidationNodeResult(coreBox);
    }

    @Override
    public String toString() {
        return "return "+returnedExpression;
    }
}
