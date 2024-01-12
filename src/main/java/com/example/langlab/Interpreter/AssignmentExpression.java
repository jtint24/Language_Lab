package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.MainApplication;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AssignmentExpression extends Expression {
    String variableName;
    Expression toAssign;
    Type type;
    boolean namePass = false;
    boolean typePass = false;

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

        if (!context.declaredVariables.contains(variableName)) {
            context.addError(new Error(Error.ErrorType.INTERPRETER_ERROR, "Can't find variable "+variableName, true, 0));
        } else {
            namePass = true;
        }

        type = context.declaredVariables.get(variableName);
        if (!toAssign.getType().subTypeOf(type)) {
            context.addError(Error.typeMismatch(type, toAssign.getType()));
        } else {
            typePass = true;
        }
        return context;
    }

    @Override
    public Type getType() {
        return toAssign.getType();
    }

    @Override
    public Node toNode() {
        VBox varName = new VBox(MainApplication.text(variableName), errBadge(namePass));
        VBox expressBox = new VBox(toAssign.toNode(), errBadge(typePass));
        HBox mainBox = new HBox(varName, MainApplication.text(" = "), expressBox);
        return mainBox;
    }

    @Override
    public String toString() {
        return variableName+" = "+toAssign;
    }
}
