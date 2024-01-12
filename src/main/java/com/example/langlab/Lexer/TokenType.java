package com.example.langlab.Lexer;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TokenType {

    private final TokenValidator isCurrentlyValid;
    private final TokenValidator couldBeValid;
    private final String name;
    final Color mainColor;
    final Color highlightColor;

    public TokenType(String name, TokenValidator isCurrentlyValid, TokenValidator couldBeValid, Color mainColor) {
        this.isCurrentlyValid = isCurrentlyValid;
        this.couldBeValid = couldBeValid;
        this.name = name;
        this.mainColor = mainColor;
        this.highlightColor = Color.WHITE;
    }

    public boolean isCurrentlyValid(String s) {
        return isCurrentlyValid.isValid(s);
    }

    public boolean couldBeValid(String s) {
        return couldBeValid.isValid(s);
    }

    public Paint getColor() {
        return mainColor;
    }

    public Paint getHighlightColor() {
        return highlightColor;
    }

    public String getName() {
        return name;
    }

    public interface TokenValidator {
        boolean isValid(String s);
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean rightBindsTighter(TokenType left, TokenType right) {
        int leftTightness;
        int rightTightness;

        if (right instanceof BinderTokenType) {
            rightTightness = ((BinderTokenType) right).leftBindingPower;
        } else {
            return false;
        }

        if (left instanceof BinderTokenType) {
            leftTightness = ((BinderTokenType) left).rightBindingPower;
        } else {
            return true;
        }

        return rightTightness > leftTightness;
    }

    public BinderTokenType toBinder(BindingPowers pl) {
        return new BinderTokenType(name, isCurrentlyValid, couldBeValid, pl, mainColor, highlightColor);
    }
}
