package com.example.langlab.Parser;

import javafx.scene.control.Tooltip;

abstract public class Nonterminal {
    public final String name;
    public String description;

    public Nonterminal(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public abstract void parse(Parser parser);

    public MarkClosed apply(Parser parser, MarkClosed closer) {
        MarkOpened opener = parser.openBefore(closer);
        parse(parser);
        return parser.close(opener, TreeKind.valid(this));
    }
    public MarkClosed apply(Parser parser) {
        MarkOpened opener = parser.open();
        parse(parser);
        return parser.close(opener, TreeKind.valid(this));
    }
}
