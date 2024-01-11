package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;

import java.util.ArrayList;


public class ConditionalExpression extends ExpressionContainer {
    ExpressionSeries expressionSeries;
    Expression condition;

    public ConditionalExpression(ExpressionSeries expressionSeries, Expression condition) {
        this.expressionSeries = expressionSeries;
        this.condition = condition;
    }

    @Override
    public ExpressionResult evaluate(State situatedState) {
        ExpressionResult conditionResult = condition.evaluate(situatedState);
        situatedState = conditionResult.resultingState;

        if (conditionResult.resultingValue == ValueLibrary.trueValue) {
            return expressionSeries.evaluate(situatedState);
        }

        return conditionResult;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        // TODO
        return context;
    }

    @Override
    public Type getType(ValidationContext context) {
        return ValueLibrary.boolType;
    }

    @Override
    public ArrayList<Expression> getContainedExpressions() {
        return new ArrayList<>() {{
            add(expressionSeries);
        }};
    }
}
