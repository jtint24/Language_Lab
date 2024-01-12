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
            System.out.println(cursorExpression+"  = "+((ExpressionResult.Success) result).returnValue);
            cursorExpression = getTopExpression();
            // System.out.println("reopening "+cursorExpression);
        } else if (result instanceof ExpressionResult.Failure) {
            Expression nextExpression = ((ExpressionResult.Failure) result).requiredEvaluation;
            events.add(new InterpretEvent(nextExpression, true, null));
            cursorExpression = nextExpression;
            // System.out.println("opening "+nextExpression);
        }
        state = result.state;
        // System.out.println("now state is "+state);
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
        if (expressionStack.size() == 0) {
            return null;
        }
        return expressionStack.get(expressionStack.size()-1);
    }

    private boolean isCompleted() {
        InterpretEvent lastEvent = events.get(events.size()-1);
        return !lastEvent.open && lastEvent.expr == headExpression;
    }

    public void run() {
        while (!isCompleted()) {
            step();
        }
    }
}
