package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Value;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.ErrorManager.Error;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class State {
    ArrayDeque<HashMap<String, Value>> scopes = new ArrayDeque<>();
    ErrorManager errorManager;

    public State(ErrorManager errorManager) {
        this.errorManager = errorManager;

        scopes.push(ValueLibrary.builtinValues);
    }

    public void addScope() {
        scopes.push(new HashMap<>());
    }

    public void put(String id, Value val) {
        if (contains(id)) {
            errorManager.logError(new Error(Error.ErrorType.RUNTIME_ERROR, "Scope already contains `"+id+"`!", true));
        }

        scopes.peek().put(id, val);
    }

    public boolean contains(String id) {
        Iterator<HashMap<String, Value>> iterator = scopes.descendingIterator();
        while (iterator.hasNext()) {
            HashMap<String, Value> scope = iterator.next();
            if (scope.containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public Value get(String id) {
        Iterator<HashMap<String, Value>> iterator = scopes.descendingIterator();
        while (iterator.hasNext()) {
            HashMap<String, Value> scope = iterator.next();
            if (scope.containsKey(id)) {
                return scope.get(id);
            }
        }

        errorManager.logError(new Error(Error.ErrorType.RUNTIME_ERROR, "Can't find value `"+id+"` in scope!", true));
        return null;
    }

    public String toString() {
        String retString = "";

        Iterator<HashMap<String, Value>> iterator = scopes.descendingIterator();
        while (iterator.hasNext()) {
            HashMap<String, Value> scope = iterator.next();
            retString += "------\n";
            for (Map.Entry<String, Value> variable : scope.entrySet()) {
                retString += variable.getKey()+": "+variable.getValue() + "\n";
            }
        }

        return retString;
    }


    public void killScope() {
        scopes.pop();
    }
}
