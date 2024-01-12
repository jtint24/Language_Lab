package com.example.langlab.Interpreter;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ValidationNodeResult {
    public Node displayNode;
    public ArrayList<ErrBadge> badges;

    public ValidationNodeResult(Node displayNode, ErrBadge... badges) {
        this.displayNode = displayNode;
        this.badges = new ArrayList<>() {{
            addAll(List.of(badges));
        }};
    }

    public ValidationNodeResult(Node displayNode, ArrayList<ErrBadge> badges) {
        this.displayNode = displayNode;
        this.badges = badges;
    }

    public Node toNode() {
        if (badges.size() == 0) {
            return displayNode;
        }
        HBox badgeBox = new HBox();
        for (ErrBadge badge : badges) {
            badgeBox.getChildren().add(badge);
        }
        VBox mainBox = new VBox(displayNode, badgeBox);
        mainBox.setSpacing(10);
        return mainBox;
    }

    public void addBadge(ErrBadge badge) {
        badges.add(badge);
    }

    public int getTotalChecks() {
        return getAllSubBadges().size();
    }

    public ArrayList<ErrBadge> getAllSubBadges() {
        ArrayList<Node> searchedNodes = new ArrayList<>() {{
            add(displayNode);
        }};

        ArrayList<ErrBadge> badges = new ArrayList<>();

        while (searchedNodes.size() > 0) {
            Node searchedNode = searchedNodes.remove(0);

            if (searchedNode instanceof ErrBadge) {
                badges.add((ErrBadge) searchedNode);
            } else if (searchedNode instanceof Pane) {
                searchedNodes.addAll(((Pane) searchedNode).getChildren());
            }
        }

        return badges;
    }
    public int getPassedChecks() {
        int i = 0;
        for (ErrBadge badge : getAllSubBadges()) {
            if (badge.pass) {
                i++;
            }
        }
        return i;
    }
}

