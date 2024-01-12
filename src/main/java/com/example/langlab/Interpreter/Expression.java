package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;

public abstract class Expression {
    public abstract State evaluate(State s);
    public abstract ValidationContext validate(ValidationContext context);
    public abstract Type getType();
}
