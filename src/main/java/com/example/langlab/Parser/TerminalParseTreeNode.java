package com.example.langlab.Parser;

import com.example.langlab.Lexer.Token;
import com.example.langlab.Lexer.TokenLibrary;
import com.example.langlab.Lexer.TokenType;
import com.example.langlab.ErrorManager.Error;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class TerminalParseTreeNode extends ParseTreeNode {
    Token wrappedToken;
    public TerminalParseTreeNode(Token wrappedToken) {
        this.wrappedToken = wrappedToken;
    }

    public Token getWrappedSymbol() {
        return wrappedToken;
    }
    @Override
    public String getHierarchyString(int tabLevel) {
        return "\t".repeat(tabLevel) + wrappedToken;
    }

    @Override
    String getSimplifiedHierarchyString(int tabLevel) {
        return "|\t".repeat(tabLevel)+"| "+ wrappedToken +"\n";
    }

    @Override
    public void removeSymbolsOfType(TokenType t) {}
    @Override
    public List<Error> getMalformedNodeErrors() {
        return new ArrayList<>();
    }
    @Override
    public String toString() {
        return "Terminal("+ wrappedToken +")";
    }

    public Node toNode() {
        if (wrappedToken.getTokenType() == TokenLibrary.whitespace) {
            return new VBox();
        }

        Text mainText = new Text(" "+wrappedToken.getLexeme()+" ");
        mainText.setFont(Font.font("Courier New", 18));
        mainText.setFill(wrappedToken.getTokenType().getHighlightColor());
        VBox mainBox = new VBox(mainText);
        mainBox.setBackground(new Background(
                new BackgroundFill(wrappedToken.getTokenType().getColor(), new CornerRadii(10), Insets.EMPTY)
        ));
        mainBox.setMaxHeight(20);
        return mainBox;
    }
}
