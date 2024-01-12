package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.MainApplication;
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
    public ValidationNodeResult getValidationNode() {
        ValidationNodeResult exprVNR = toAssign.getValidationNode();
        if (namePass) {
            exprVNR.addBadge(new ErrBadge("The expression's type matched the variable's", "The expression's type did not match the variable's", typePass));
        }

        ValidationNodeResult nameVNR = new ValidationNodeResult(MainApplication.text(variableName, 24), new ErrBadge("The variable's name exists", "The variable's name does not exist", namePass));

        HBox mainBox = new HBox(nameVNR.toNode(), MainApplication.text(" = ", 24), exprVNR.toNode());
        return new ValidationNodeResult(mainBox);
    }

    @Override
    public String toString() {
        return variableName+" = "+toAssign;
    }
}
