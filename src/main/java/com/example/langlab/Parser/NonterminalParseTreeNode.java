package com.example.langlab.Parser;

import com.example.langlab.Lexer.TokenType;
import com.example.langlab.ErrorManager.Error;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NonterminalParseTreeNode extends ParseTreeNode {
    TreeKind kind;
    ArrayList<ParseTreeNode> children = new ArrayList<>();
    public NonterminalParseTreeNode(TreeKind kind) {
        this.kind = kind;
    }

    public void addChild(ParseTreeNode child) {
        children.add(child);
    }

    public TreeKind getKind() {
        return kind;
    }

    public ArrayList<ParseTreeNode> getChildren() {
        return children;
    }

    @Override
    public String getHierarchyString(int tabLevel) {
        StringBuilder retString = new StringBuilder("\t".repeat(tabLevel)+"Parser.Nonterminal(" + kind + ", [\n");
        for (int i = 0; i < children.size(); i++) {
            ParseTreeNode child = children.get(i);
            retString.append(child.getHierarchyString(tabLevel+1));
            if ( i != children.size()-1) {
                retString.append(",");
            }
            retString.append("\n");
        }
        return retString+"\t".repeat(tabLevel)+"])";
    }

    @Override
    public void removeSymbolsOfType(TokenType t) {
        ArrayList<ParseTreeNode> newChildren = new ArrayList<>();

        for (ParseTreeNode child : children) {
            if (child instanceof TerminalParseTreeNode) {
                if (!((TerminalParseTreeNode) child).wrappedToken.getTokenType().equals(t)) {
                    newChildren.add(child);
                }
            } else if (child instanceof NonterminalParseTreeNode) {
                // child.removeSymbolsOfType(t);
                newChildren.add(child);
            }
        }

        children = newChildren;
    }

    @Override
    public List<Error> getMalformedNodeErrors() {
        ArrayList<Error> errors = new ArrayList<>();
        if (!kind.isValid) {
            errors.add(new Error(Error.ErrorType.PARSER_ERROR, "Malformed tree node", true));
        }
        for (ParseTreeNode child : children) {
            errors.addAll(child.getMalformedNodeErrors());
        }
        return errors;
    }

    public String getSimplifiedHierarchyString(int tabLevel) {
        String header = "|\t".repeat(tabLevel)+"| "+kind + "\n";
        StringBuilder body = new StringBuilder();
        for (ParseTreeNode child : children) {
            body.append(child.getSimplifiedHierarchyString(tabLevel + 1));
        }
        if (tabLevel == 0) {
            body.deleteCharAt(body.length()-1);
        }
        return header + body;
    }



    @Override
    public String toString() {
        return getSimplifiedHierarchyString();
    }

    public String getFlatString() {
        StringBuilder retString = new StringBuilder("Parser.Nonterminal(" + kind + ", [");
        for (int i = 0; i < children.size(); i++) {
            ParseTreeNode child = children.get(i);
            retString.append(child);
            if ( i != children.size()-1) {
                retString.append(", ");
            }
        }
        return retString+"])";
    }

    public Node toNode() {


        Pane childBoxes;
        if (children.stream().anyMatch(
                parseTreeNode -> parseTreeNode instanceof NonterminalParseTreeNode &&
                        ((NonterminalParseTreeNode) parseTreeNode).kind.validTreeType == NonterminalLibrary.statement)
        ) {
            VBox vertChildren  = new VBox();
            vertChildren.setSpacing(5);
            vertChildren.setAlignment(Pos.CENTER);

            childBoxes = vertChildren;
        } else {
            HBox horizChildren = new HBox();
            horizChildren.setSpacing(5);
            horizChildren.setAlignment(Pos.CENTER);

            childBoxes = horizChildren;
        }

        for (ParseTreeNode ptn : children) {
            childBoxes.getChildren().add(ptn.toNode());
        }

        Text label;
        if (kind.isValid) {
            label = new Text(" "+kind.validTreeType.name+" ");
        } else {
            label = new Text("ERROR!");
        }

        label.setFont(Font.font("Courier New", 18));
        HBox labelBox = new HBox(label);
        labelBox.setMaxHeight(30);
        labelBox.setBackground(
                new Background(
                        new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)
                )
        );
        if (kind.isValid) {
            Tooltip.install(labelBox, new Tooltip(kind.validTreeType.description));
        }

        labelBox.setAlignment(Pos.CENTER);
        labelBox.setMaxWidth(10*(kind.validTreeType.name.length()+2));

        VBox mainBox = new VBox(labelBox, childBoxes);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setSpacing(10);
        mainBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, new CornerRadii(10),  BorderStroke.MEDIUM, new Insets(10))
        ));

        return mainBox;
    }
}
