package com.example.langlab.Elements;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.Interpreter.LayeredMap;

import java.util.HashMap;

public class ValueLibrary {
    public static Type intType = new Type("int") {
        @Override
        public boolean matchesValue(Value v) {
            return v instanceof ValueWrapper && (((ValueWrapper<?>) v).wrappedValue) instanceof Integer;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this;
        }
    };
    public static Type voidType = new Type("void") {
        @Override
        public boolean matchesValue(Value v) {
            return false;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this;
        }
    };
    public static Function plusOperator = new Function(new BinaryOpFunctionType(intType, intType, intType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager) {
            Value leftVal = args[0];
            Value rightVal = args[1];
            int left = ((ValueWrapper<Integer>) leftVal).wrappedValue;
            int right = ((ValueWrapper<Integer>) rightVal).wrappedValue;

            return new ValueWrapper<>(intType, left+right);
        }
    };
    public static HashMap<String,Value> builtins = new HashMap<>() {{
        put("+", plusOperator);
    }};
    public static Value voidResult = new VoidValue();
}
