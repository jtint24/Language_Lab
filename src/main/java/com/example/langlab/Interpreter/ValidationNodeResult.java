package com.example.langlab.Interpreter;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ValidationNodeResult {
    Node displayNode;
    ArrayList<ErrBadge> badges;

    public ValidationNodeResult(Node displayNode, ErrBadge... badges) {
        this.displayNode = displayNode;
        this.badges = new ArrayList<>() {{
            addAll(List.of(badges));
        }};
    }

    public Node toNode() {
        if (badges.size() == 0) {
            return displayNode;
        }
        HBox badgeBox = new HBox();
        for (ErrBadge badge : badges) {
            badgeBox.getChildren().add(badge.toNode());
        }
        VBox mainBox = new VBox(displayNode, badgeBox);
        return mainBox;
    }

    public void addBadge(ErrBadge badge) {
        badges.add(badge);
    }
}

