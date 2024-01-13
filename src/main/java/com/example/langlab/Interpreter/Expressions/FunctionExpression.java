package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.*;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.Interpreter.*;
import com.example.langlab.Interpreter.Expressions.Expression;
import com.example.langlab.MainApplication;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

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
    public ExpressionResult evaluate(State s) {
        if (!s.callResults.containsKey(appliedFunctionExpression)) {
            return new ExpressionResult.Failure(s, appliedFunctionExpression);
        }

        for (Expression inputExpression : inputExpressions) {
            if (!s.callResults.containsKey(inputExpression)) {
                return new ExpressionResult.Failure(s, inputExpression);
            }
        }
        Function function = (Function) s.callResults.get(appliedFunctionExpression);

        Value[] inputValues = new Value[inputExpressions.size()];
        for (int i = 0; i < inputExpressions.size(); i++) {
            inputValues[i] = s.callResults.get(inputExpressions.get(i));
        }

        Value returnValue = function.apply(inputValues, s.errorManager, s.outputBuffer);

        for (Expression inputExpression : inputExpressions) {
            s.callResults.remove(inputExpression);
        }
        s.callResults.remove(appliedFunctionExpression);

        return new ExpressionResult.Success(s, returnValue);
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
        HBox box;

        if (type instanceof BinaryOpFunctionType) {
            box = new HBox(argNodes.get(0), funcVNR.displayNode, argNodes.get(1));
        } else {
            box = new HBox(funcVNR.displayNode, MainApplication.text("(", 24));

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
        }

        return new ValidationNodeResult(box, totalErrBadges);
    }

    @Override
    public String toString() {
        if (type instanceof BinaryOpFunctionType) {
            return inputExpressions.get(0).toString()+appliedFunctionExpression+inputExpressions.get(1);
        } else {
            StringBuilder args = new StringBuilder();
            for (Expression inputExpression : inputExpressions) {
                args.append(", ").append(inputExpression);
            }
            args = new StringBuilder(args.substring(2));
            return appliedFunctionExpression.toString() + "(" + args + ")";
        }
    }
}
