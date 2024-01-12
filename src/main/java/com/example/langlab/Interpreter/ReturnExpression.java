package com.example.langlab.Interpreter;

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
        return null;
    }

    @Override
    public String toString() {
        return "return "+returnedExpression;
    }
}
