package com.example.langlab;

import com.example.langlab.ErrorManager.Error;
import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.IO.InputBuffer;
import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Interpreter.Expression;
import com.example.langlab.Interpreter.ExpressionBuilder;
import com.example.langlab.Interpreter.ValidationContext;
import com.example.langlab.Lexer.*;
import com.example.langlab.Parser.NonterminalLibrary;
import com.example.langlab.Parser.ParseTreeNode;
import com.example.langlab.Parser.Parser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public static final Color BG_GRAY = Color.gray(0.05);
    public static final Color FG_GRAY = Color.gray(0.2);

    public final Background basic_bg = new Background(new BackgroundFill(FG_GRAY, new CornerRadii(20), Insets.EMPTY));

    String program = "";
    SymbolString lexemes = new SymbolString();
    OutputBuffer out = new OutputBuffer();
    ErrorManager errorManager = new ErrorManager(out);
    ParseTreeNode headPtn;
    Stage stage;
    Tab codeTab;
    Tab lexerTab;
    Tab parserTab;
    Tab semanticsTab;
    @Override
    public void start(Stage stage) {
        out.silence();
        Scene scene = new Scene(mainView(), 800,500);
        scene.getStylesheets().add("custom.css");
        stage.setTitle("Language Lab");
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
    }

    public void runInterpretation(String program) {
        errorManager = new ErrorManager(out);
        lexemes = new SymbolString();
        headPtn = null;

        Tokenizer tokenizer = new Tokenizer(new InputBuffer(program, errorManager), errorManager);
        try {
            lexemes = tokenizer.extractAllSymbols();
        } catch (RuntimeException ignored) {}
        System.out.println(lexemes);


        Parser parser = new Parser(tokenizer, errorManager);
        try {
            parser.setSymbols(lexemes.toList());
            NonterminalLibrary.file.apply(parser);
        } catch (RuntimeException ignored) {}
        headPtn = parser.buildTree();


        headPtn.removeSymbolsOfType(TokenLibrary.whitespace);
        System.out.println(headPtn);
        Expression expr = ExpressionBuilder.convert(headPtn);
        System.out.println(expr);
        ValidationContext validationContext = new ValidationContext();
        expr.validate(validationContext);
        errorManager.logErrors(validationContext.getErrors());

        errorManager.printErrors(true);

        refresh();
    }

    public void refresh() {
        lexerTab.setContent(lexView());
        parserTab.setContent(parseView());

    }

    public Parent mainView() {
        codeTab = new Tab();
        codeTab.setText("Code");
        codeTab.setContent(codeView());
        codeTab.setClosable(false);

        lexerTab = new Tab();
        lexerTab.setText("Lexer");
        lexerTab.setContent(lexView());
        lexerTab.setClosable(false);

        parserTab = new Tab();
        parserTab.setText("Parser");
        parserTab.setContent(parseView());
        parserTab.setClosable(false);

        TabPane mainPane = new TabPane(codeTab, lexerTab, parserTab);
        mainPane.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        return new TabPane(codeTab, lexerTab, parserTab);
    }

    public VBox codeView() {
        TextArea code = new TextArea();
        code.setText(program);
        code.setFont(Font.font("Courier new", 16));
        code.setMaxHeight(Double.MAX_VALUE);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(actionEvent -> runInterpretation(code.getText()));

        VBox mainBox = new VBox(code, submitButton);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));

        mainBox.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        mainBox.setSpacing(10);

        return mainBox;
    }

    public Node parseView() {
        Text title = text("Parse Tree");
        title.setFont(Font.font("Courier New", 24));
        title.setFill(Color.WHITE);

        VBox mainBox = new VBox(title);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(15);

        if (headPtn != null) {
            ScrollPane scroll = new ScrollPane(headPtn.toNode());
            scroll.setBackground(basic_bg);
            scroll.setPannable(true);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            mainBox.getChildren().add(scroll);
        } else {
            mainBox.getChildren().add(text("No Parse Tree..."));
        }

        if (errorManager.hasErrors(Error.ErrorType.PARSER_ERROR)) {
            mainBox.getChildren().add(text(errorManager.toString()));
        }

        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));


        mainBox.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        return mainBox;
    }

    public Node lexView() {

        Text title = text("Lexemes");
        title.setFont(Font.font("Courier New", 24));
        title.setFill(Color.WHITE);

        VBox codeBox = new VBox();
        FlowPane linePane = new FlowPane();
        linePane.setHgap(10);

        for (Token token : lexemes.toList()) {
            linePane.getChildren().add(token.toNode());
            if (token.getLexeme().contains("\n")) {
                codeBox.getChildren().add(linePane);
                linePane = new FlowPane();
                linePane.setHgap(10);
            }
        }

        codeBox.setBackground(new Background(
                new BackgroundFill(FG_GRAY, new CornerRadii(20), Insets.EMPTY)
        ));
        codeBox.setPadding(new Insets(20));

        if (lexemes.toList().size() == 0) {
            codeBox.getChildren().add(text("No Lexemes..."));
        } else {
            codeBox.getChildren().add(linePane);
        }

        if (errorManager.hasErrors(Error.ErrorType.LEXER_ERROR)) {
            codeBox.getChildren().add(text(errorManager.toString()));
        }

        codeBox.setSpacing(10);
        codeBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(title, codeBox);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setSpacing(15);
        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));


        mainBox.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        return mainBox;
    }

    public Text text(String s) {
        Text ret = new Text(s);
        ret.setFill(Color.WHITE);
        ret.setFont(Font.font("Courier New", 14));
        return ret;
    }

    public static void main(String[] args) {
        launch();
    }
}