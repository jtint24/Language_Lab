package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Interpreter.Expressions.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State {
    public HashMap<Expression, Value> callResults = new HashMap<>();
    public LayeredMap<String, Value> variables = new LayeredMap<>(true);
    public ErrorManager errorManager;
    public OutputBuffer outputBuffer;

    public State(ErrorManager errorManager, OutputBuffer outputBuffer) {
        variables.addLayer();
        for (Map.Entry<String, Value> builtin : ValueLibrary.builtins.entrySet()) {
            variables.put(builtin.getKey(), builtin.getValue());
        }
        variables.addLayer();
        this.errorManager = errorManager;
        this.outputBuffer = outputBuffer;
    }

    public ArrayList<MemoryEntry> getMemoryEntries() {
        ArrayList<MemoryEntry> memoryEntries = new ArrayList<>();
        for (Map.Entry<String, Value> entry : variables.toHashMap(1).entrySet()) {
            memoryEntries.add(new MemoryEntry(entry.getKey(), entry.getValue()));
        }
        return memoryEntries;
    }

    public static class MemoryEntry {
        public String variable;
        public Value value;
        public Type type;

        public MemoryEntry(String variable, Value value) {
            this.variable = variable;
            this.value = value;
            this.type = value.getType();
        }
        @Override
        public String toString() {
            return variable+"\t"+value+"\t"+type;
        }

        public String getVariable() {
            return variable;
        }

        public Value getValue() {
            return value;
        }

        public Type getType() {
            return type;
        }
    }
}
