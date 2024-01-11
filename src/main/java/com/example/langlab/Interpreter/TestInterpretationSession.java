package com.example.langlab.Interpreter;

import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Lexer.SymbolString;
import com.example.langlab.Parser.NonterminalLibrary;
import com.example.langlab.Parser.NonterminalParseTreeNode;
import com.example.langlab.Parser.ParseTreeNode;

import java.util.Arrays;

public class TestInterpretationSession extends InterpretationSession {

    public TestInterpretationSession(String body) {
        super(body);
        this.outputBuffer.silence();
        this.outputBuffer.terse = true;
    }

    public OutputBuffer testGetParseTree() {
        try {
            SymbolString symbolString = tokenizer.extractAllSymbols();
            llParser.setSymbols(symbolString.toList());
            NonterminalLibrary.file.apply(llParser);
            ParseTreeNode parseTree = llParser.buildTree();
            outputBuffer.println(parseTree);
        } catch (RuntimeException exception) {
            outputBuffer.println(exception);
            outputBuffer.println(Arrays.toString(exception.getStackTrace()));
        }

        return outputBuffer;
    }

    public OutputBuffer testGetInterpretation() {
        try {
            SymbolString symbolString = tokenizer.extractAllSymbols();

            llParser.setSymbols(symbolString.toList());
            NonterminalLibrary.file.apply(llParser);
            ParseTreeNode parseTree = llParser.buildTree();

            Expression expr = expressionBuilder.buildExpression((NonterminalParseTreeNode) parseTree);
            ValidationContext startContext = new ValidationContext();
            ValidationContext endContext = expr.validate(startContext);
            errorManager.logErrors(endContext.errors);

            outputBuffer.println(expr);
        } catch (RuntimeException exception) {
            // outputBuffer.println(exception);
            // outputBuffer.println(Arrays.toString(exception.getStackTrace()));
        }

        return outputBuffer;
    }

    public OutputBuffer testBinaryExpressions() {
        SymbolString symbolString = tokenizer.extractAllSymbols();
        llParser.setSymbols(symbolString.toList());
        NonterminalLibrary.fullExpression.apply(llParser);
        ParseTreeNode parseTree = llParser.buildTree();
        outputBuffer.println(parseTree);
        return outputBuffer;
    }

    public OutputBuffer testGetLexerString() {
        try {
            SymbolString symbolString = tokenizer.extractAllSymbols();
            outputBuffer.println(symbolString);
        } catch (RuntimeException exception) {
            outputBuffer.println(exception);
            outputBuffer.println(Arrays.toString(exception.getStackTrace()));
        }

        return outputBuffer;
    }


}
