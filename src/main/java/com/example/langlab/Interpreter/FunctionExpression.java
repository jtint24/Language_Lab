package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Function;
import com.example.langlab.Elements.Type;
import com.example.langlab.ErrorManager.Error;

import java.util.ArrayList;

public class FunctionExpression extends Expression {
    Function appliedFunction;
    ArrayList<Expression> inputExpressions;

    @Override
    public State evaluate(State s) {
        return null;
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        for (Expression inputExpression : inputExpressions) {
            context = inputExpression.validate(context);
        }

        if (inputExpressions.size() != appliedFunction.getType().getParameterTypes().length) {
            context.addError(
                    new Error(
                            Error.ErrorType.INTERPRETER_ERROR,
                            "Expected "+appliedFunction.getType().getParameterTypes().length+" arguments, got "+inputExpressions.size(),
                            true,
                            0
                    )
            );
        }

        for (int i = 0; i<inputExpressions.size(); i++) {
            if (inputExpressions.get(i).getType().subTypeOf(appliedFunction.getType().getParameterTypes()[i])) {
                context.addError(Error.typeMismatch(appliedFunction.getType().getParameterTypes()[i], inputExpressions.get(i).getType()));
            }
        }

        return context;
    }

    @Override
    public Type getType() {
        return appliedFunction.getType().getReturnType();
    }

    @Override
    public ValidationNodeResult getValidationNode() {
        // TODO
        return null;
    }
}
