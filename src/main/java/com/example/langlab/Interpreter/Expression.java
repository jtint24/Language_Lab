package com.example.langlab.Interpreter;

public abstract class Expression {
    public abstract State evaluate(State s);
    public abstract ValidationContext validate(ValidationContext context);
}
