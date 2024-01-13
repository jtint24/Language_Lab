package com.example.langlab.Interpreter;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Interpreter.Expressions.Expression;
import com.example.langlab.MainApplication;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Interpreter {
    Expression headExpression;
    Expression cursorExpression;
    State state;
    ArrayList<InterpretEvent> events;
    OutputBuffer outputBuffer;

    public Interpreter(Expression headExpression, ErrorManager errorManager, OutputBuffer outputBuffer) {
        this.headExpression = headExpression;
        this.state = new State(errorManager, outputBuffer);
        this.cursorExpression = headExpression;
        this.events = new ArrayList<>() {{
            add(new InterpretEvent(headExpression, true, null));
        }};
        this.outputBuffer = outputBuffer;
    }
    public void step() {
        ExpressionResult result = cursorExpression.evaluate(state);
        if (result instanceof ExpressionResult.Success) {
            ExpressionResult.Success success = (ExpressionResult.Success) result;
            events.add(new InterpretEvent(cursorExpression, false, success.returnValue));
            state.callResults.put(cursorExpression, success.returnValue);

            if (success.addNewScope) {
                state.variables.addLayer();
            }
            if (success.closeOldScope) {
                state.variables.removeLayer();
            }

            System.out.println(cursorExpression+"  = "+success.returnValue);
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

    public boolean isCompleted() {
        InterpretEvent lastEvent = events.get(events.size()-1);
        return !lastEvent.open && lastEvent.expr == headExpression;
    }

    public void run() {
        while (!isCompleted()) {
            step();
        }
    }

    public Node toNode() {
        VBox lines = new VBox();
        VBox results = new VBox();
        int indent = 1;
        for (int i = 0; i<events.size(); i++) {
            InterpretEvent interpretEvent = events.get(i);
            if (i+1<events.size() && events.get(i+1).open != interpretEvent.open && events.get(i+1).expr == interpretEvent.expr) {
                if (interpretEvent.open) {
                    indent++;
                } else {
                    indent--;
                }
                continue;
            }

            System.out.println(interpretEvent);
            Text label;
            Text result = MainApplication.text("");

            if (interpretEvent.open) {
                label = MainApplication.text(" ".repeat(indent)+interpretEvent.expr.toStringLine(), 18);
            } else {
                indent--;
                label = MainApplication.text(" ".repeat(indent)+interpretEvent.expr.toStringLine(), 18);
                result = MainApplication.text("= "+interpretEvent.returnValue);
            }

            result.setFont(Font.font("Courier new", FontPosture.ITALIC, 18));

            lines.getChildren().add(label);
            results.getChildren().add(result);

            if (interpretEvent.open) {
                indent++;
            }
        }

        HBox mainBox = new HBox(lines, results);

        return mainBox;
    }

    public State getState() {
        return state;
    }
}
