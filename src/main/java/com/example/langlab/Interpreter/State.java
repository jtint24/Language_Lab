package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.Interpreter.Expressions.Expression;

import java.util.HashMap;
import java.util.Map;

public class State {
    public HashMap<Expression, Value> callResults = new HashMap<>();
    public LayeredMap<String, Value> variables = new LayeredMap<>(true);

    public State() {
        variables.addLayer();
        for (Map.Entry<String, Value> builtin : ValueLibrary.builtins.entrySet()) {
            variables.put(builtin.getKey(), builtin.getValue());
        }
    }
}
