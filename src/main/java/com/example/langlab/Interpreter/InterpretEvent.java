package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;
import com.example.langlab.Interpreter.Expressions.Expression;

public class InterpretEvent {
    boolean open;
    final Expression expr;
    Value returnValue;

    InterpretEvent(Expression expr, boolean open, Value returnValue) {
        this.expr = expr;
        this.open = open;
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return open ? "Open("+expr.toStringLine()+")" : "Close("+expr.toStringLine()+")";
    }
}
