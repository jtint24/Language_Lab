package com.example.langlab.Lexer;

import java.util.ArrayList;
import java.util.List;

public class SymbolString {
    private final ArrayList<Token> tokens;

    public SymbolString(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public SymbolString() {
        this.tokens = new ArrayList<>();
    }

    public SymbolString(Token... initTokens) {
        this();
        for (Token token : initTokens) {
            append(token);
        }
    }

    public void append(Token s) {
        tokens.add(s);
    }
    public void append(SymbolString string2) {
        tokens.addAll(string2.tokens);
    }
    public Token get(int i) { return tokens.get(i); }

    public int length() {
        return tokens.size();
    }

    public SymbolString substring(int startIdx, int untilIdx) {
        return new SymbolString(new ArrayList<>(tokens.subList(startIdx, untilIdx)));
    }

    public SymbolString substring(int untilIdx) {
        return substring(untilIdx, tokens.size());
    }

    public String toString() {
        StringBuilder retString = new StringBuilder();
        for (Token s : tokens) {
            retString.append(s);
        }
        return retString.toString();
    }

    public List<Token> toList() {
        return tokens;
    }
}
