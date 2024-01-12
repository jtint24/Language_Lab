package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;
import com.example.langlab.Interpreter.ExpressionResult;
import com.example.langlab.Interpreter.Expressions.Expression;
import com.example.langlab.Interpreter.State;
import com.example.langlab.Interpreter.ValidationContext;
import com.example.langlab.Interpreter.ValidationNodeResult;
import com.example.langlab.MainApplication;

public class ValueExpression extends Expression {
    Value wrappedValue;

    ValueExpression(Value wrappedType) {
        this.wrappedValue = wrappedType;
    }

    @Override
    public ExpressionResult evaluate(State s) {
        return new ExpressionResult.Success(s, wrappedValue);
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
