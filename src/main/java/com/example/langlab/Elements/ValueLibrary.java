package com.example.langlab.Elements;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Interpreter.LayeredMap;

import java.util.HashMap;

public class ValueLibrary {
    public static Type intType = new Type("Int") {
        @Override
        public boolean matchesValue(Value v) {
            return v instanceof ValueWrapper && (((ValueWrapper<?>) v).wrappedValue) instanceof Integer;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this || type == anyType || type == floatType;
        }
    };

    public static Type floatType = new Type("Float") {
        @Override
        public boolean matchesValue(Value v) {
            return v instanceof ValueWrapper && ((((ValueWrapper<?>) v).wrappedValue) instanceof Float || (((ValueWrapper<?>) v).wrappedValue) instanceof Integer);
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this || type == anyType;
        }
    };
    public static Type anyType = new Type("Any") {
        @Override
        public boolean matchesValue(Value v) {
            return true;
        }
        @Override
        public boolean subTypeOf(Type type) {
            return type == this;
        }
    };
    public static Type voidType = new Type("Void") {
        @Override
        public boolean matchesValue(Value v) {
            return v == voidResult;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return type == this;
        }
    };
    public static Function plusOperator = new Function(new BinaryOpFunctionType(floatType, floatType, floatType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager, OutputBuffer outputBuffer) {
            Value leftVal = args[0];
            Value rightVal = args[1];

            float left;

            if (leftVal.type == intType) {
                left = ((ValueWrapper<Integer>) leftVal).wrappedValue;
            } else {
                left = ((ValueWrapper<Float>) leftVal).wrappedValue;
            }

            float right;

            if (rightVal.type == intType) {
                right = ((ValueWrapper<Integer>) rightVal).wrappedValue;
            } else {
                right = ((ValueWrapper<Float>) rightVal).wrappedValue;
            }

            if (left % 1 == 0 && right % 1 == 0) {
                return new ValueWrapper<>(intType, left+right);
            } else {
                return new ValueWrapper<>(floatType, left+right);
            }
        }
    };
    public static Function divOperator = new Function(new BinaryOpFunctionType(floatType, floatType, floatType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager, OutputBuffer outputBuffer) {
            Value leftVal = args[0];
            Value rightVal = args[1];

            float left;

            if (leftVal.type == intType) {
                left = ((ValueWrapper<Integer>) leftVal).wrappedValue;
            } else {
                left = ((ValueWrapper<Float>) leftVal).wrappedValue;
            }

            float right;

            if (rightVal.type == intType) {
                right = ((ValueWrapper<Integer>) rightVal).wrappedValue;
            } else {
                right = ((ValueWrapper<Float>) rightVal).wrappedValue;
            }

            if (left % 1 == 0 && right % 1 == 0) {
                return new ValueWrapper<>(intType, left/right);
            } else {
                return new ValueWrapper<>(floatType, left/right);
            }
        }
    };
    public static Function subOperator = new Function(new BinaryOpFunctionType(floatType, floatType, floatType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager, OutputBuffer outputBuffer) {
            Value leftVal = args[0];
            Value rightVal = args[1];

            float left;

            if (leftVal.type == intType) {
                left = ((ValueWrapper<Integer>) leftVal).wrappedValue;
            } else {
                left = ((ValueWrapper<Float>) leftVal).wrappedValue;
            }

            float right;

            if (rightVal.type == intType) {
                right = ((ValueWrapper<Integer>) rightVal).wrappedValue;
            } else {
                right = ((ValueWrapper<Float>) rightVal).wrappedValue;
            }

            if (left % 1 == 0 && right % 1 == 0) {
                return new ValueWrapper<>(intType, left-right);
            } else {
                return new ValueWrapper<>(floatType, left-right);
            }
        }
    };
    public static Function multOperator = new Function(new BinaryOpFunctionType(floatType, floatType, floatType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager, OutputBuffer outputBuffer) {
            Value leftVal = args[0];
            Value rightVal = args[1];

            float left;

            if (leftVal.type == intType) {
                left = ((ValueWrapper<Integer>) leftVal).wrappedValue;
            } else {
                left = ((ValueWrapper<Float>) leftVal).wrappedValue;
            }

            float right;

            if (rightVal.type == intType) {
                right = ((ValueWrapper<Integer>) rightVal).wrappedValue;
            } else {
                right = ((ValueWrapper<Float>) rightVal).wrappedValue;
            }

            if (left % 1 == 0 && right % 1 == 0) {
                return new ValueWrapper<>(intType, left*right);
            } else {
                return new ValueWrapper<>(floatType, left*right);
            }
        }
    };
    public static Function printlnFunction = new Function(new FunctionType(voidType, anyType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager, OutputBuffer outputBuffer) {
            outputBuffer.println(args[0]);
            return voidResult;
        }
    };
    public static Function printFunction = new Function(new FunctionType(voidType, anyType)) {
        @Override
        public Value prevalidatedApply(Value[] args, ErrorManager errorManager, OutputBuffer outputBuffer) {
            outputBuffer.print(args[0]);
            return voidResult;
        }
    };
    public static HashMap<String,Value> builtins = new HashMap<>() {{
        put("+", plusOperator);
        put("/", divOperator);
        put("*", multOperator);
        put("-", subOperator);
        put("println", printlnFunction);
        put("print", printFunction);
    }};
    public static Value voidResult = new VoidValue();
    public static Type blankType = new Type("") {
        @Override
        public boolean matchesValue(Value v) {
            return false;
        }

        @Override
        public boolean subTypeOf(Type type) {
            return false;
        }
    };
}
