package com.example.langlab.Interpreter;

public class ErrBadge {
    String tooltip;
    boolean pass;
    public ErrBadge(String success, String fail, boolean pass) {
        this.pass = pass;
        this.tooltip = pass ? success : fail;
    }
}
