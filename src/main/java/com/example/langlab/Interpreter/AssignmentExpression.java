package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.Error;

public class AssignmentExpression extends Expression {
    String variableName;
    Expression toAssign;
    Type type;

    public AssignmentExpression(String variableName, Expression toAssign) {
        this.variableName = variableName;
        this.toAssign = toAssign;
    }

    @Override
    public State evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        context = toAssign.validate(context);

        type = context.declaredVariables.get(variableName);
        if (!toAssign.getType().subTypeOf(type)) {
            context.addError(Error.typeMismatch(type, toAssign.getType()));
        }
        return context;
    }

    @Override
    public Type getType() {
        return toAssign.getType();
    }

    @Override
    public String toString() {
        return variableName+" = "+toAssign;
    }
}
