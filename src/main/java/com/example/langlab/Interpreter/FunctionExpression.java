package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Function;
import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;

import java.util.ArrayList;

public class FunctionExpression extends Expression {
    Function wrappedFunction;

    ArrayList<Expression> inputExpressions;

    public FunctionExpression(Function wrappedFunction, ArrayList<Expression> inputExpressions) {
        this.wrappedFunction = wrappedFunction;
        this.inputExpressions = inputExpressions;
    }

    @Override
    public ExpressionResult evaluate(State situatedState) {
        ArrayList<Value> results = new ArrayList<>();
        for (Expression inputExpression : inputExpressions) {
            ExpressionResult result = inputExpression.evaluate(situatedState);
            results.add(result.resultingValue);
            situatedState = result.resultingState;
        }

        Value functionResult = wrappedFunction.apply(situatedState.errorManager, situatedState, results.toArray(new Value[0]));

        return new ExpressionResult(situatedState, functionResult);
    }

    @Override
    public ValidationContext validate(ValidationContext context) {
        for (Expression inputExpression : inputExpressions) {
            context = inputExpression.validate(context);
        }

        // TODO: validate that the types are valid
        return context;
    }

    @Override
    public Type getType(ValidationContext context) {
        return wrappedFunction.getType().getResultType();
    }
}
