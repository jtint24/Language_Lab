package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.Error;

public class DeclarationExpression extends Expression {
    String variableName;
    Expression assignTo;

    public DeclarationExpression(String variableName, Expression assignTo) {
        this.variableName = variableName;
        this.assignTo = assignTo;
    }

    @Override
    public State evaluate(State s) {
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
            context.declaredVariables.put(variableName, assignTo.getType());
        }
        return context;
    }

    @Override
    public Type getType() {
        return ValueLibrary.voidType;
    }

    @Override
    public String toString() {
        return "var "+variableName+" = "+assignTo;
    }
}
