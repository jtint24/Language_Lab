package com.example.langlab.Interpreter;

import com.example.langlab.Interpreter.Expressions.Expression;

import java.util.ArrayList;

public class Interpreter {
    Expression headExpression;
    Expression cursorExpression;
    State state;
    ArrayList<InterpretEvent> events;

    public Interpreter(Expression headExpression) {
        this.headExpression = headExpression;
        this.state = new State();
        this.cursorExpression = headExpression;
        this.events = new ArrayList<>() {{
            add(new InterpretEvent(headExpression, true, null));
        }};
    }
    public void step() {
        ExpressionResult result = cursorExpression.evaluate(state);
        if (result instanceof ExpressionResult.Success) {
            events.add(new InterpretEvent(cursorExpression, false, ((ExpressionResult.Success) result).returnValue));
            state.callResults.put(cursorExpression, ((ExpressionResult.Success) result).returnValue);
            cursorExpression = getTopExpression();
        } else if (result instanceof ExpressionResult.Failure) {
            Expression nextExpression = ((ExpressionResult.Failure) result).requiredEvaluation;
            events.add(new InterpretEvent(nextExpression, true, null));
            cursorExpression = nextExpression;
        }
        state = result.state;
    }

    private Expression getTopExpression() {
        ArrayList<Expression> expressionStack = new ArrayList<>();
        for (InterpretEvent event : events) {
            if (event.open) {
                expressionStack.add(event.expr);
            } else {
                expressionStack.remove(expressionStack.size()-1);
            }
        }
        return expressionStack.get(expressionStack.size()-1);
    }

    public void run() {

    }
}
