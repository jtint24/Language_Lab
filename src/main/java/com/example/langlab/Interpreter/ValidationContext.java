package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.ErrorManager.ErrorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidationContext {
    LayeredMap<String, Type> declaredVariables = new LayeredMap<>(false);

    ArrayList<Error> errors = new ArrayList<>();

    public ValidationContext() {
        declaredVariables.addLayer();
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
