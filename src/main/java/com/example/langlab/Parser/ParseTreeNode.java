package com.example.langlab.Parser;

import com.example.langlab.Lexer.TokenType;
import com.example.langlab.ErrorManager.Error;
import javafx.scene.Node;

import java.util.List;

public abstract class ParseTreeNode {
    public String getHierarchyString() {
        return getHierarchyString(0);
    }
    abstract String getHierarchyString(int tabLevel);
    abstract String getSimplifiedHierarchyString(int tabLevel);
    public String getSimplifiedHierarchyString() { return getSimplifiedHierarchyString(0); }
    public abstract void removeSymbolsOfType(TokenType tokenType);
    public abstract List<Error> getMalformedNodeErrors();
    public abstract ParseTreeNode simplify();

    public abstract Node toNode();
}
