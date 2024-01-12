package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.ErrorManager.ErrorManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidationContext {
    LayeredMap<String, Type> declaredVariables;

    ArrayList<Error> errors;
}
