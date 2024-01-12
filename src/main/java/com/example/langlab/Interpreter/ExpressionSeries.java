package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;

import java.util.ArrayList;

public class ExpressionSeries extends Expression {
    ArrayList<Expression> subExpressions;

    ExpressionSeries(ArrayList<Expression> subExpressions) {
        this.subExpressions = subExpressions;
    }

    @Override
    public State evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        for (Expression subExpression : subExpressions) {
            context = subExpression.validate(context);
        }

        return context;
    }

    @Override
    public Type getType() {
        return ValueLibrary.voidType;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Expression subExpression : subExpressions) {
            ret.append(subExpression).append("\n");
        }
        return ret.toString();
    }
}
