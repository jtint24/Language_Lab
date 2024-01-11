package com.example.langlab.Lexer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Token {
    private final String lexeme;
    private final TokenType tokenType;

    public Token(String lexeme, TokenType tokenType) {
        this.lexeme = lexeme;
        this.tokenType = tokenType;
    }

    public String toString() {
        return "{ lexeme = `"+lexeme.replace("\n", "\\n")+"` tokenType = "+tokenType+" }";
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Node toNode() {
        VBox mainBox = new VBox();
        Text lexemeText = new Text(" "+lexeme+" ");
        lexemeText.setFont(Font.font("Courier New", 18));
        lexemeText.setFill(tokenType.darkColor);

        Text typeText;
        if (tokenType != TokenLibrary.whitespace) {
            typeText = new Text(" "+tokenType+" ");
        } else {
            typeText = new Text("");
        }
        typeText.setFont(Font.font("Courier New", 12));
        typeText.setFill(tokenType.darkColor);

        mainBox.getChildren().add(lexemeText);
        mainBox.getChildren().add(typeText);

        mainBox.setAlignment(Pos.CENTER);

        mainBox.setBackground(
                new Background(
                    new BackgroundFill(tokenType.mainColor, new CornerRadii(5), Insets.EMPTY)
                )
        );

        // mainB
        return mainBox;
    }
}
