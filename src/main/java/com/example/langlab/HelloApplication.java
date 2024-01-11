package com.example.langlab;

import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.IO.InputBuffer;
import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Lexer.SymbolString;
import com.example.langlab.Lexer.Token;
import com.example.langlab.Lexer.Tokenizer;
import com.example.langlab.Parser.NonterminalLibrary;
import com.example.langlab.Parser.NonterminalParseTreeNode;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    String program = "";
    SymbolString lexemes = new SymbolString();
    OutputBuffer out = new OutputBuffer();
    ErrorManager errorManager = new ErrorManager(out);
    ParseTreeNode headPtn;
    Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        out.silence();
        Scene scene = new Scene(mainView(), 500,500);
        stage.setTitle("Language Lab");
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
    }

    public Parent mainView() {
        Tab codeTab = new Tab();
        codeTab.setText("Code");
        codeTab.setContent(codeView());
        codeTab.setClosable(false);

        Tab lexerTab = new Tab();
        lexerTab.setText("Lexer");
        lexerTab.setContent(lexView());
        lexerTab.setClosable(false);

        Tab parseTab = new Tab();
        parseTab.setText("Parser");
        parseTab.setContent(parseView());
        parseTab.setClosable(false);

        return new TabPane(codeTab, lexerTab, parseTab);
    }

    public VBox codeView() {
        TextArea code = new TextArea();
        code.setText(program);
        code.setFont(Font.font("Courier new", 16));

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(actionEvent -> {
            program = code.getText();
            Tokenizer tokenizer = new Tokenizer(new InputBuffer(program, errorManager), errorManager);
            lexemes = tokenizer.extractAllSymbols();
            System.out.println(lexemes);

            Parser parser = new Parser(tokenizer, errorManager);
            parser.setSymbols(lexemes.toList());
            NonterminalLibrary.file.apply(parser);
            headPtn = parser.buildTree();

            stage.setScene(new Scene(mainView(), 500, 500));
        });

        VBox mainBox = new VBox(code, submitButton);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));

        return mainBox;
        // mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));
        /// return mainBox;
    }

    public Node parseView() {
        Text title = new Text("Parse Tree");
        title.setFont(Font.font("Arial", 24));

        VBox mainBox = new VBox(title);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(15);

        if (headPtn != null) {
            mainBox.getChildren().add(new ScrollPane(headPtn.toNode()));
        } else {
            mainBox.getChildren().add(new Text("No Parse Tree..."));
        }

        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));

        return mainBox;
    }

    public Node lexView() {

        Text title = new Text("Lexemes");
        title.setFont(Font.font("Arial", 24));

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

        if (lexemes.toList().size() == 0) {
            codeBox.getChildren().add(new Text("No Lexemes..."));
        } else {
            codeBox.getChildren().add(linePane);
        }

        codeBox.setSpacing(10);
        codeBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(title, codeBox);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.setSpacing(15);
        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));

        return mainBox;
    }

    public static void main(String[] args) {
        launch();
    }
}