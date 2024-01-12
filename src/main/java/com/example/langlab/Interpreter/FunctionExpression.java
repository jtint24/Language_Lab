package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Function;
import com.example.langlab.Elements.FunctionType;
import com.example.langlab.Elements.Type;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.MainApplication;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.langlab.Elements.ValueLibrary.voidType;

public class FunctionExpression extends Expression {
    Expression appliedFunctionExpression;
    ArrayList<Expression> inputExpressions;
    FunctionType type;
    ArrayList<ErrBadge> errBadges = new ArrayList<>();

    public FunctionExpression(Expression appliedFunctionExpression, ArrayList<Expression> inputExpressions) {
        this.appliedFunctionExpression = appliedFunctionExpression;
        this.inputExpressions = inputExpressions;
    }

    @Override
    public State evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        context = appliedFunctionExpression.validate(context);

        for (Expression inputExpression : inputExpressions) {
            context = inputExpression.validate(context);
        }

        Type funcType = appliedFunctionExpression.getType();
        errBadges.add(new ErrBadge("The expression is callable", "The expression isn't callable", funcType instanceof FunctionType));

        if (funcType instanceof FunctionType) {
            type = (FunctionType) funcType;
            FunctionType appliedFunctionType = (FunctionType) funcType;

            errBadges.add(new ErrBadge("Proper number of arguments", "Improper number of arguments", inputExpressions.size() == appliedFunctionType.getParameterTypes().length));
            if (inputExpressions.size() != appliedFunctionType.getParameterTypes().length) {
                context.addError(
                        new Error(
                                Error.ErrorType.INTERPRETER_ERROR,
                                "Expected " + appliedFunctionType.getParameterTypes().length + " arguments, got " + inputExpressions.size(),
                                true,
                                0
                        )
                );
            }

            for (int i = 0; i < inputExpressions.size(); i++) {

                boolean success = inputExpressions.get(i).getType().subTypeOf(appliedFunctionType.getParameterTypes()[i]);
                errBadges.add(new ErrBadge("Types correctly matched in argument","Type mismatch in argument", success));
                if (!success) {
                    context.addError(Error.typeMismatch(appliedFunctionType.getParameterTypes()[i], inputExpressions.get(i).getType()));
                }
            }
        } else {
            context.addError(
                    new Error(
                            Error.ErrorType.INTERPRETER_ERROR,
                            "Expression of type "+funcType+" isn't callable",
                            true,
                            0
                    )
            );
        }

        return context;
    }

    @Override
    public Type getType() {
        return type == null ? voidType : type.getReturnType();
    }

    @Override
    public ValidationNodeResult getValidationNode() {
        ArrayList<Node> argNodes = new ArrayList<>();
        ArrayList<ErrBadge> totalErrBadges = new ArrayList<>(errBadges);

        for (Expression arg : inputExpressions) {
            ValidationNodeResult argVNR = arg.getValidationNode();
            argNodes.add(argVNR.displayNode);
            totalErrBadges.addAll(argVNR.badges);
        }

        ValidationNodeResult funcVNR = appliedFunctionExpression.getValidationNode();
        totalErrBadges.addAll(funcVNR.badges);

        HBox box = new HBox(funcVNR.displayNode, MainApplication.text("(",24));

        for (Node argNode : argNodes) {
            box.getChildren().add(
                    MainApplication.text(", ", 24)
            );
            box.getChildren().add(
                    argNode
            );
        }

        box.getChildren().remove(2);

        box.getChildren().add(MainApplication.text(")", 24));

        return new ValidationNodeResult(box, totalErrBadges);
    }
}
