package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.Interpreter.*;
import com.example.langlab.MainApplication;
import javafx.scene.layout.HBox;

public class DeclarationExpression extends Expression {
    String variableName;
    Expression assignTo;

    boolean namePass = false;

    public DeclarationExpression(String variableName, Expression assignTo) {
        this.variableName = variableName;
        this.assignTo = assignTo;
    }

    @Override
    public ExpressionResult evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        context = assignTo.validate(context);
        if (context.declaredVariables.contains(variableName)) {
            context.addError(
                    new Error(
                            Error.ErrorType.INTERPRETER_ERROR,
                            "There already exists a variable named "+variableName,
                            true,
                            0
                    )
            );
        } else {
            namePass = true;
            context.declaredVariables.put(variableName, assignTo.getType());
        }
        return context;
    }

    @Override
    public Type getType() {
        return ValueLibrary.voidType;
    }

    @Override
    public ValidationNodeResult getValidationNode() {
        ValidationNodeResult exprVNR = assignTo.getValidationNode();

        ValidationNodeResult nameVNR = new ValidationNodeResult(
                MainApplication.text(variableName, 24),
                new ErrBadge("The variable name didn't already exist", "The variable name already existed!", namePass)
        );

        return new ValidationNodeResult(new HBox(MainApplication.text("var ", 24), nameVNR.toNode(), MainApplication.text(" = ", 24), exprVNR.toNode()));
    }

    @Override
    public String toString() {
        return "var "+variableName+" = "+assignTo;
    }
}
