package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.ErrorManager.ErrorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationContext {
    LayeredMap<String, Type> declaredVariables = new LayeredMap<>(false);

    ArrayList<Error> errors = new ArrayList<>();

    public ValidationContext() {
        declaredVariables.addLayer();
        for (Map.Entry<String, Value> builtin : ValueLibrary.builtins.entrySet()) {
            declaredVariables.put(builtin.getKey(), builtin.getValue().getType());
        }
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
