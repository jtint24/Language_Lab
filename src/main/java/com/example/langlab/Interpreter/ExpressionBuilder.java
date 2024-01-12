package com.example.langlab.Interpreter;

import com.example.langlab.Parser.NonterminalLibrary;
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
                case "identifier":
                    return new VariableExpression(lexeme);
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
                case "Statement", "Expression", "Delimited Expression" -> {
                    return unwrap((NonterminalParseTreeNode) ptn);
                }
            }
        }

        return null;
    }

    private static Expression unwrap(NonterminalParseTreeNode ptn) {
        return convert(ptn.getChildren().get(0));
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
