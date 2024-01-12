package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;
import com.example.langlab.Interpreter.Expressions.Expression;

public class ExpressionResult {
    State state;
    public static class Success extends ExpressionResult {
        Value returnValue;
    }
    public static class Failure extends ExpressionResult {
        Expression requiredEvaluation;
    }
}
