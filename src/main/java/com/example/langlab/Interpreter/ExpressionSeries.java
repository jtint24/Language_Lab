package com.example.langlab.Interpreter;

import java.util.ArrayList;

public class ExpressionSeries extends Expression {
    ArrayList<Expression> subExpressions = new ArrayList<>();

    ExpressionSeries(ArrayList<Expression> subExpressions) {
        this.subExpressions = subExpressions;
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
        StringBuilder ret = new StringBuilder();
        for (Expression subExpression : subExpressions) {
            ret.append(subExpression).append("\n");
        }
        return ret.toString();
    }
}
