package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.Type;
import com.example.langlab.Interpreter.ExpressionResult;
import com.example.langlab.Interpreter.State;
import com.example.langlab.Interpreter.ValidationContext;
import com.example.langlab.Interpreter.ValidationNodeResult;

public abstract class Expression {
    public abstract ExpressionResult evaluate(State s);
    public abstract ValidationContext validate(ValidationContext context);
    public abstract Type getType();
    public abstract ValidationNodeResult getValidationNode();
}
