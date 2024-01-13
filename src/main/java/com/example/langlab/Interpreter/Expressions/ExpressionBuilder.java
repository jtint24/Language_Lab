package com.example.langlab.Interpreter.Expressions;

import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.Elements.ValueWrapper;
import com.example.langlab.Lexer.TokenLibrary;
import com.example.langlab.Parser.NonterminalParseTreeNode;
import com.example.langlab.Parser.ParseTreeNode;
import com.example.langlab.Parser.TerminalParseTreeNode;

import java.util.ArrayList;

public class ExpressionBuilder {
    public static Expression convert(ParseTreeNode ptn) {
        if (ptn instanceof TerminalParseTreeNode) {
            String name =  (((TerminalParseTreeNode) ptn).getWrappedToken().getTokenType().getName());
            String lexeme = ((TerminalParseTreeNode) ptn).getWrappedToken().getLexeme();
            switch (name) {
                case "identifier", "+", "/", "-", "*":
                    return new VariableExpression(lexeme);
                case "int":
                    return buildIntExpression(lexeme);
                default:
                    return null;
            }
        } else if (ptn instanceof NonterminalParseTreeNode) {
            String name = ((NonterminalParseTreeNode) ptn).getKind().getName();
            switch (name) {
                case "Var" -> {
                    return buildDeclarationExpression((NonterminalParseTreeNode) ptn);
                }
                case "Return" -> {
                    return buildReturnExpression((NonterminalParseTreeNode) ptn);
                }
                case "File" -> {
                    return buildExpressionSeries((NonterminalParseTreeNode) ptn);
                }
                case "Assignment" -> {
                    return buildAssignmentExpression((NonterminalParseTreeNode) ptn);
                }
                case "Binary Expression" -> {
                    return buildBinaryExpression((NonterminalParseTreeNode) ptn);
                }
                case "Expression Call" -> {
                    return buildExpressionCall((NonterminalParseTreeNode) ptn);
                }
                case "Statement", "Expression", "Delimited Expression" -> {
                    return unwrap((NonterminalParseTreeNode) ptn);
                }
            }
        }

        return null;
    }

    private static Expression buildExpressionCall(NonterminalParseTreeNode ptn) {
        ParseTreeNode funcNode = ptn.getChildren().get(0);
        NonterminalParseTreeNode argumentList = (NonterminalParseTreeNode) ptn.getChildren().get(2);
        ArrayList<Expression> subExpressions = new ArrayList<>();
        for (ParseTreeNode argumentChild : argumentList.getChildren()) {
            if (argumentChild instanceof NonterminalParseTreeNode) {
                subExpressions.add(convert(argumentChild));
            }
        }
        return new FunctionExpression(convert(funcNode), subExpressions);
    }

    private static Expression buildIntExpression(String lexeme) {
        return new ValueExpression(new ValueWrapper<>(ValueLibrary.intType, Integer.parseInt(lexeme)));
    }

    private static Expression buildBinaryExpression(NonterminalParseTreeNode ptn) {
        Expression operator = convert(ptn.getChildren().get(1));

        Expression left = convert(ptn.getChildren().get(0));
        Expression right = convert(ptn.getChildren().get(2));

        ArrayList<Expression> args = new ArrayList<>() {{
            add(left);
            add(right);
        }};

        return new FunctionExpression(operator, args);
    }

    private static Expression buildAssignmentExpression(NonterminalParseTreeNode ptn) {
        ArrayList<ParseTreeNode> children = ptn.getChildren();
        ParseTreeNode nameNode = children.get(0);
        ParseTreeNode equals = children.get(1);
        ParseTreeNode expression = children.get(2);
        String name = ((TerminalParseTreeNode) nameNode).getWrappedToken().getLexeme();

        return new AssignmentExpression(name, convert(expression));
    }

    private static Expression unwrap(NonterminalParseTreeNode ptn) {
        ParseTreeNode child = ptn.getChildren().get(0);
        if (child instanceof TerminalParseTreeNode && ((TerminalParseTreeNode) child).getWrappedToken().getTokenType() == TokenLibrary.lParen) {
            child = ptn.getChildren().get(1);
        }
        return convert(child);
    }

    private static Expression buildReturnExpression(NonterminalParseTreeNode ptn) {
        ArrayList<ParseTreeNode> children = ptn.getChildren();
        ParseTreeNode keyword = children.get(0);
        ParseTreeNode expression = children.get(1);
        return new ReturnExpression(convert(expression));
    }

    private static Expression buildExpressionSeries(NonterminalParseTreeNode ptn) {
        ArrayList<ParseTreeNode> children = ptn.getChildren();
        ArrayList<Expression> statements = new ArrayList<>();
        for (ParseTreeNode child : children) {
            statements.add(convert(child));
        }
        return new ExpressionSeries(statements);
    }

    private static DeclarationExpression buildDeclarationExpression(NonterminalParseTreeNode ptn) {
        ArrayList<ParseTreeNode> children = ptn.getChildren();
        ParseTreeNode keyword = children.get(0);
        ParseTreeNode nameNode = children.get(1);
        ParseTreeNode equals = children.get(2);
        ParseTreeNode expression = children.get(3);
        String name = ((TerminalParseTreeNode) nameNode).getWrappedToken().getLexeme();
        return new DeclarationExpression(name, convert(expression));
    }
}
