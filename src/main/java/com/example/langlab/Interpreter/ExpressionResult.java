package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;
import com.example.langlab.Interpreter.Expressions.Expression;

public class ExpressionResult {
    State state;

    public static class Success extends ExpressionResult {
        Value returnValue;
        boolean addNewScope = false;
        boolean closeOldScope = false;
        public Success(State state, Value returnValue) {
            this.state = state;
            this.returnValue = returnValue;
        }
        public Success(State state, Value returnValue, boolean addNewScope, boolean closeOldScope) {
            this.state = state;
            this.returnValue = returnValue;
            this.addNewScope = addNewScope;
            this.closeOldScope = closeOldScope;
        }
    }
    public static class Failure extends ExpressionResult {
        Expression requiredEvaluation;

        public Failure(State state, Expression requiredEvaluation) {
            this.requiredEvaluation = requiredEvaluation;
            this.state = state;
        }
    }
}
