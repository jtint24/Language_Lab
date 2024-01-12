package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class ExpressionSeries extends Expression {
    ArrayList<Expression> subExpressions;

    ExpressionSeries(ArrayList<Expression> subExpressions) {
        this.subExpressions = subExpressions;
    }

    @Override
    public State evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        for (Expression subExpression : subExpressions) {
            context = subExpression.validate(context);
        }

        return context;
    }

    @Override
    public Type getType() {
        return ValueLibrary.voidType;
    }

    @Override
    public ValidationNodeResult getValidationNode() {
        VBox stack = new VBox();
        for (Expression subExpression : subExpressions) {
            stack.getChildren().add(subExpression.getValidationNode().toNode());
        }
        return new ValidationNodeResult(new HBox(new VBox(), stack));
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Expression subExpression : subExpressions) {
            ret.append(subExpression).append("\n");
        }
        return ret.toString();
    }
}
