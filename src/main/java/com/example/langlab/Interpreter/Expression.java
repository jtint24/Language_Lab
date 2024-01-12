package com.example.langlab.Interpreter;

import com.example.langlab.Elements.Type;
import com.example.langlab.MainApplication;
import javafx.scene.Node;
import javafx.scene.text.Text;

public abstract class Expression {
    public abstract State evaluate(State s);
    public abstract ValidationContext validate(ValidationContext context);
    public abstract Type getType();
    public abstract Node toNode();

    public static Node errBadge(boolean pass) {
        return pass ? MainApplication.text("✓") : MainApplication.text("x");
    }
}
