package com.example.langlab.Lexer;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.IO.InputBuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {
    private final InputBuffer inputBuffer;
    private final ErrorManager errorManager;

    public Tokenizer(InputBuffer inputBuffer, ErrorManager errorManager) {
        this.inputBuffer = inputBuffer;
        this.errorManager = errorManager;
    }

    public SymbolString extractAllSymbols() {
        ArrayList<Token> retList = new ArrayList<>();
        while (!inputBuffer.isTerminated()) {
            retList.add(getSymbol());
        }
        return new SymbolString(retList);
    }

    public Token getSymbol() {
        List<TokenType> availableTokenTypes = Arrays.asList(TokenLibrary.getTokens());
        String currentLexeme = "";

        while (!availableTokenTypes.isEmpty()) {
            if (inputBuffer.isTerminated()) {
                break;
            } else {
                currentLexeme += inputBuffer.getChar();
            }

            List<TokenType> newAvailableTokenTypes = new ArrayList<>();

            for (TokenType availableTokenType : availableTokenTypes) {
                if (availableTokenType.couldBeValid(currentLexeme)) {
                    newAvailableTokenTypes.add(availableTokenType);
                }
            }

            availableTokenTypes = newAvailableTokenTypes;
        }

        ArrayList<TokenType> possibleTokenTypes;
        String originalLexeme = currentLexeme;

        do {
            possibleTokenTypes = new ArrayList<>();

            for (TokenType possibleTokenType : TokenLibrary.getTokens()) {
                if (possibleTokenType.isCurrentlyValid(currentLexeme)) {
                    possibleTokenTypes.add(possibleTokenType);
                }
            }

            if (possibleTokenTypes.size() == 1) {
                return new Token(currentLexeme, possibleTokenTypes.get(0));
            } else if (possibleTokenTypes.size() > 1) {
                errorManager.logError(new Error(Error.ErrorType.LEXER_ERROR, "Ambiguous tokens for `"+currentLexeme+"`", true));
            }

            inputBuffer.ungetChar(currentLexeme.charAt(currentLexeme.length() - 1));
            currentLexeme = currentLexeme.substring(0, currentLexeme.length() - 1);
        } while (!currentLexeme.equals(""));

        errorManager.logError(new Error(Error.ErrorType.LEXER_ERROR, "No possible token for `"+originalLexeme+"`", true));

        // System.out.println("currentLexeme: `"+currentLexeme+"`");


        return null;
    }

    public boolean isTerminated() {
        return inputBuffer.isTerminated();
    }
}
