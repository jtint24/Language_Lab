package com.example.langlab.Parser;

import com.example.langlab.ErrorManager.Error;
import com.example.langlab.Interpreter.LayeredMap;
import com.example.langlab.Lexer.TokenLibrary;
import com.example.langlab.Lexer.TokenType;

import java.util.HashSet;

public class NonterminalLibrary {
    public static Nonterminal file = new Nonterminal("File", "A nonterminal to capture the whole program") {
        @Override
        public void parse(Parser parser) {

            while (!parser.eof()) {
                parser.eat(TokenLibrary.whitespace);

                //if (parser.at(TokenLibrary.let)) {
                //    letStatement.apply(parser);
                //} else {
                //    parser.advanceWithError(new Error(Error.ErrorType.PARSER_ERROR, "Expected a statement", false));
                //}
                statement.apply(parser);
            }

        }
    };

    static Nonterminal varStatement = new Nonterminal("Var", "A nonterminal that captures a var statement") {
        @Override
        public void parse(Parser parser) {

            parser.expect(TokenLibrary.var);
            parser.eat(TokenLibrary.whitespace);
            parser.expect(TokenLibrary.identifier);
            parser.eat(TokenLibrary.whitespace);
            parser.expect(TokenLibrary.equals);
            parser.eat(TokenLibrary.whitespace);
            fullExpression.apply(parser);

        }
    };
    public static Nonterminal returnStatement = new Nonterminal("Return", "A nonterminal that captures a return statement") {
        @Override
        public void parse(Parser parser) {

            parser.expect(TokenLibrary.ret);
            parser.eat(TokenLibrary.whitespace);
            fullExpression.apply(parser);
            parser.eat(TokenLibrary.whitespace);

        }
    };

    /*
    public static Nonterminal lambda = new Nonterminal("Lambda", "A nonterminal that captures a lambda expression") {
        @Override
        public void parse(Parser parser) {

            parser.expect(TokenLibrary.lBrace);

            parameterList.apply(parser);

            parser.eat(TokenLibrary.whitespace);

            parser.expect(TokenLibrary.arrow);

            parser.eat(TokenLibrary.whitespace);

            do {
                fullExpression.apply(parser);
                parser.eat(TokenLibrary.whitespace);
            } while (!parser.at(TokenLibrary.rBrace));

            parser.expect(TokenLibrary.rBrace);
            // Expression lambdas and statement lambdas are identical in the parse: expression lambdas contain 1 expression and statement lambdas contain multiple.

        }
    };
    */

    public static Nonterminal parameterList = new Nonterminal("Parameter List", "A nonterminal that captures a list of parameters to a function") {
        @Override
        public void parse (Parser parser) {

            do {
                parser.eat(TokenLibrary.whitespace);
                fullExpression.apply(parser);
                parser.eat(TokenLibrary.whitespace);
                parser.expect(TokenLibrary.identifier);
            } while (parser.eat(TokenLibrary.comma));

        }
    };

    public static Nonterminal argumentList = new Nonterminal("Argument List", "A nonterminal that captures a list of arguments to a function call") {
        @Override
        public void parse(Parser parser) {

            do {
                parser.eat(TokenLibrary.whitespace);
                fullExpression.apply(parser);
            } while (parser.eat(TokenLibrary.comma));

        }
    };

    public static Nonterminal statement = new Nonterminal("Statement", "A nonterminal that captures any statement that performs an action") {
        @Override
        public void parse(Parser parser) {
            if (parser.at(TokenLibrary.var)) {
                varStatement.apply(parser);
            } else if (parser.at(TokenLibrary.ret)) {
                returnStatement.apply(parser);
            } else {
                fullExpression.apply(parser);
            }
            parser.eat(TokenLibrary.whitespace);

        }
    };

    public static Nonterminal assignment = new Nonterminal("Assignment", "A nonterminal that captures an assignment statement") {
        @Override
        public void parse(Parser parser) {
            parser.expect(TokenLibrary.identifier);
            parser.eat(TokenLibrary.whitespace);
            parser.expect(TokenLibrary.equals);
            parser.eat(TokenLibrary.whitespace);
            fullExpression.apply(parser);
            parser.eat(TokenLibrary.whitespace);
        }
    };


    // Basic expressions: including expressions in parentheses, literals, identifiers, lambdas
    public static Nonterminal delimitedExpression = new Nonterminal("Delimited Expression", "A nonterminal that captures an simple expression without operators") {
        @Override
        public void parse(Parser parser) {

            parser.eat(TokenLibrary.whitespace);
            if (parser.at(TokenLibrary.INT_TOKEN_TYPE)) {
                System.out.println("Check!");
                parser.eat(TokenLibrary.INT_TOKEN_TYPE);
            } else if (parser.at(TokenLibrary.FLOAT_TOKEN_TYPE)) {
                parser.eat(TokenLibrary.FLOAT_TOKEN_TYPE);
            } else if (parser.at(TokenLibrary.stringLiteral)) {
                parser.eat(TokenLibrary.stringLiteral);
            } else if (parser.at(TokenLibrary.lParen)) {
                parser.eat(TokenLibrary.lParen);
                fullExpression.apply(parser);
                parser.expect(TokenLibrary.rParen);
            } else if (parser.hasDistance(1) && parser.at(TokenLibrary.identifier) && (parser.nth(1) == TokenLibrary.equals || (parser.hasDistance(2) && parser.nth(1) == TokenLibrary.whitespace && parser.nth(2) == TokenLibrary.equals))) {
                assignment.apply(parser);
            } else if (parser.at(TokenLibrary.identifier)) {
                parser.eat(TokenLibrary.identifier);
            } else {
                parser.advanceWithError(new Error(Error.ErrorType.PARSER_ERROR, "Malformed expression", true, 0));
            }

            parser.eat(TokenLibrary.whitespace);
        }
    };

    public static Nonterminal expressionCall = new Nonterminal("Expression Call", "A nonterminal that captures when an expression is called like a function") {
        @Override
        public void parse(Parser parser) {}
    };

    public static Nonterminal fullExpression = new Nonterminal("Expression", "A nonterminal capturing any expression that represents a value") {
        @Override
        public void parse(Parser parser) {
            recursiveExpression(parser, TokenLibrary.eof);
        }

        public void recursiveExpression(Parser parser, TokenType leftTokenType) {
            MarkClosed lefthandSide = delimitedExpression.apply(parser);

            while (parser.at(TokenLibrary.lParen)) {
                MarkOpened opener = parser.openBefore(lefthandSide);
                parser.expect(TokenLibrary.lParen);
                argumentList.apply(parser);
                parser.expect(TokenLibrary.rParen);
                lefthandSide = parser.close(opener, TreeKind.valid(expressionCall));
            }
            // System.out.println("Recursively parsing with left "+leftToken);

            // while (parser.at(TokenLibrary.lParen)) {
            //     Parser.MarkOpened opener = parser.openBefore(lefthandSide);
            // }

            while (!parser.eof()) {
                TokenType rightTokenType = parser.nth(0);
                // System.out.println("Right token is "+rightToken);

                // if (rightToken instanceof BinderToken) {
                    // System.out.println("rbp: "+((BinderToken) rightToken).leftBindingPower);
                // }
                // if (leftToken instanceof BinderToken) {
                    // System.out.println("lbp: "+((BinderToken) leftToken).rightBindingPower);
                // }

                if (TokenType.rightBindsTighter(leftTokenType, rightTokenType)) {
                    // System.out.println("Right binds tighter.");

                    MarkOpened opener = parser.openBefore(lefthandSide);
                    parser.advance();
                    recursiveExpression(parser, rightTokenType);
                    lefthandSide = parser.close(opener, TreeKind.valid(binaryExpression));
                } else {
                    // System.out.println("Left binds tighter: BREAK!");

                    break;
                }
            }
        }
    };

    public static Nonterminal binaryExpression = new Nonterminal("Binary Expression", "A nonterminal capturing expressions containing binary operators ") {
        @Override
        public void parse(Parser parser) {}
    };

    public static HashSet<Nonterminal> removable = new HashSet<>() {{
        add(statement);
        add(fullExpression);
    }};

    public static HashSet<Nonterminal> statements = new HashSet<>() {{
        add(assignment);
        add(varStatement);
        add(returnStatement);
        add(statement);
    }};

}
