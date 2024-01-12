package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Function;
import com.example.langlab.Elements.Type;

import java.util.ArrayList;

public class FunctionExpression extends Expression {
    Function appliedFunction;
    ArrayList<Expression> inputExpressions;

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
        return appliedFunction.getType().getReturnType();
    }
}
