package com.example.langlab.Interpreter;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ErrBadge {
    String tooltip;
    boolean pass;
    public ErrBadge(String success, String fail, boolean pass) {
        this.pass = pass;
        this.tooltip = pass ? success : fail;
    }

    public Node toNode() {
        Text passTxt = new Text("✓");
        Text failTxt = new Text("x");
        passTxt.setFill(Color.GREEN);
        failTxt.setFill(Color.RED);
        Text a = pass ? passTxt : failTxt;
        Tooltip.install(a, new Tooltip(tooltip));
        return a;
    }
}
