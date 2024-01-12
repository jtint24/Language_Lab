package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.Interpreter.*;
import com.example.langlab.Interpreter.Expressions.Expression;
import com.example.langlab.MainApplication;

public class VariableExpression extends Expression {
    String variableName;
    Type type;
    boolean namePass;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ExpressionResult evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        if (!context.declaredVariables.contains(variableName)) {
            context.addError(
                    new Error(Error.ErrorType.INTERPRETER_ERROR, "Can't find variable "+variableName, true, 0)
            );
            type = ValueLibrary.voidType;
        } else {
            namePass = true;
            type = context.declaredVariables.get(variableName);

        }
        return context;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public ValidationNodeResult getValidationNode() {
        return new ValidationNodeResult(
                MainApplication.text(variableName,24),
                new ErrBadge("The variable name could be found", "The variable name couldn't be found", namePass)
        );
    }

    @Override
    public String toString() {
        return variableName;
    }
}
