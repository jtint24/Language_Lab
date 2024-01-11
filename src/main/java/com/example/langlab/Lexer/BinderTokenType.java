package com.example.langlab.Lexer;

import javafx.scene.paint.Color;

public class BinderTokenType extends TokenType {
    public int leftBindingPower;
    public int rightBindingPower;

    public BinderTokenType(String name, TokenValidator isCurrentlyValid, TokenValidator couldBeValid, BindingPowers precedenceLevel, Color mainColor, Color darkColor) {
        super(name, isCurrentlyValid, couldBeValid, mainColor, darkColor);
        leftBindingPower = precedenceLevel.leftBindingPower();
        rightBindingPower = precedenceLevel.rightBindingPower();
    }
}
