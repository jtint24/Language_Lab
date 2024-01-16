package com.example.langlab;

import com.example.langlab.Elements.Type;
import com.example.langlab.Elements.Value;
import com.example.langlab.Elements.ValueLibrary;
import com.example.langlab.Elements.ValueWrapper;
import com.example.langlab.ErrorManager.Error;
import com.example.langlab.ErrorManager.ErrorManager;
import com.example.langlab.IO.InputBuffer;
import com.example.langlab.IO.OutputBuffer;
import com.example.langlab.Interpreter.Expressions.Expression;
import com.example.langlab.Interpreter.Expressions.ExpressionBuilder;
import com.example.langlab.Interpreter.Interpreter;
import com.example.langlab.Interpreter.State;
import com.example.langlab.Interpreter.ValidationContext;
import com.example.langlab.Interpreter.ValidationNodeResult;
import com.example.langlab.Lexer.*;
import com.example.langlab.Parser.NonterminalLibrary;
import com.example.langlab.Parser.ParseTreeNode;
import com.example.langlab.Parser.Parser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainApplication extends Application {
    public static final Color BG_GRAY = Color.gray(0.05);
    public static final Color FG_GRAY = Color.gray(0.2);

    public final Background basic_bg = new Background(new BackgroundFill(FG_GRAY, new CornerRadii(20), Insets.EMPTY));

    String program = "";
    SymbolString lexemes = new SymbolString();
    OutputBuffer out = new OutputBuffer();
    ErrorManager errorManager = new ErrorManager(out);
    ParseTreeNode headPtn;
    ParseTreeNode simplifiedHeadPtn;
    Interpreter interpreter;
    Expression ast;
    boolean simplifyPtn;
    Stage stage;
    Tab codeTab;
    Tab lexerTab;
    Tab parserTab;
    Tab semanticsTab;
    Tab executionTab;
    @Override
    public void start(Stage stage) {
        out.silence();
        Scene scene = new Scene(mainView(), 800,500);
        scene.getStylesheets().add("custom.css");
        stage.setTitle("Language Lab");
        stage.setScene(scene);
        stage.show();

        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final URL imageResource = MainApplication.class.getClassLoader().getResource("llicon.png");
        final Image icon = defaultToolkit.getImage(imageResource);

        try {
            Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(icon);
        } catch (final UnsupportedOperationException e) {
            //stage.getIcons().add(SwingFXUtils.toFXImage(icon));
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }
        stage.getIcons().add(
                new javafx.scene.image.Image(
                        Objects.requireNonNull(
                                MainApplication.class.getClassLoader().getResourceAsStream("llicon.png")
                        )
                )
        );

        this.stage = stage;
    }

    public void runInterpretation(String program) {
        refresh(true);

        out.clear();
        errorManager = new ErrorManager(out);
        lexemes = new SymbolString();
        headPtn = null;
        ast = null;
        interpreter = null;


        Tokenizer tokenizer = new Tokenizer(new InputBuffer(program, errorManager), errorManager);
        try {
            lexemes = tokenizer.extractAllSymbols();
        } catch (Exception ignored) {
            // System.out.println("Caught exception");
            refresh(false);
            return;
        }
        System.out.println(lexemes);


        Parser parser = new Parser(tokenizer, errorManager);
        try {
            parser.setSymbols(lexemes.toList());
            NonterminalLibrary.file.apply(parser);
        } catch (Exception ignored) {
            refresh(false);
            return;
        } finally {
            headPtn = parser.buildTree();
            simplifiedHeadPtn = headPtn.simplify();
        }


        headPtn.removeSymbolsOfType(TokenLibrary.whitespace);
        System.out.println(headPtn);
        Expression expr = ExpressionBuilder.convert(headPtn);
        // System.out.println(expr);
        ValidationContext validationContext = new ValidationContext();
        expr.validate(validationContext);

        try {
            errorManager.logErrors(validationContext.getErrors());
        } catch (Exception ignored) {
            ast = expr;
            refresh(false);
            return;
        }
        ast = expr;

        errorManager.printErrors(true);

        // System.out.println("Ast: \n"+expr);

        interpreter = new Interpreter(expr, errorManager, out);
        // interpreter.run();

        refresh(false);
    }

    public Color getTabColor(HashMap<Error.ErrorType, Boolean> errorTypes, Error.ErrorType errorType) {
        if (errorTypes.containsKey(errorType)) {
            if (!errorTypes.get(errorType)) {
                return Color.YELLOW;
            } else if (errorTypes.get(errorType)) {
                return Color.RED;
            }
        }
        for (Error.ErrorType otherType : Error.ErrorType.values()) {
            if (otherType.ordinal() < errorType.ordinal() && errorTypes.containsKey(otherType) && errorTypes.get(otherType)) {
                return Color.GRAY;
            }
        }
        return Color.GREEN;
    }

    public void refresh(boolean first) {
        Text lexerLabel = new Text("Lexer");
        Text parserLabel = new Text("Parser");
        Text semanticLabel = new Text("Semantics");
        Text executionLabel = new Text("Execution");


        if (!first) {
            HashMap<Error.ErrorType, Boolean> errorLevels = errorManager.getPresentErrorTypes();
            for (Map.Entry<Error.ErrorType, Boolean> errorLevel : errorLevels.entrySet()) {
                System.out.println(errorLevel.getKey()+"\t"+errorLevel.getValue());
            }

            lexerLabel.setFill(getTabColor(errorLevels, Error.ErrorType.LEXER_ERROR));
            parserLabel.setFill(getTabColor(errorLevels, Error.ErrorType.PARSER_ERROR));
            semanticLabel.setFill(getTabColor(errorLevels, Error.ErrorType.INTERPRETER_ERROR));
            executionLabel.setFill(getTabColor(errorLevels, Error.ErrorType.RUNTIME_ERROR));
        } else {
            lexerLabel.setFill(Color.WHITE);
            parserLabel.setFill(Color.WHITE);
            semanticLabel.setFill(Color.WHITE);
            executionLabel.setFill(Color.WHITE);
        }

        lexerTab.setContent(lexView());
        parserTab.setContent(parseView());
        semanticsTab.setContent(semanticsView());
        executionTab.setContent(executionView());

        lexerTab.setGraphic(lexerLabel);
        parserTab.setGraphic(parserLabel);
        semanticsTab.setGraphic(semanticLabel);
        executionTab.setGraphic(executionLabel);
    }

    public Parent mainView() {
        codeTab = new Tab();
        codeTab.setText("Code");
        codeTab.setContent(codeView());
        codeTab.setClosable(false);

        lexerTab = new Tab();
        lexerTab.setText("");
        // lexerTab.setContent(lexView());
        lexerTab.setClosable(false);

        parserTab = new Tab();
        parserTab.setText("");
        // parserTab.setContent(parseView());
        parserTab.setClosable(false);

        semanticsTab = new Tab();
        semanticsTab.setText("");
        // semanticsTab.setContent(semanticsView());
        semanticsTab.setClosable(false);

        executionTab = new Tab();
        executionTab.setText("");
        // executionTab.setContent(executionView());
        executionTab.setClosable(false);

        TabPane mainPane = new TabPane(codeTab, lexerTab, parserTab);
        mainPane.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        refresh(true);

        return new TabPane(codeTab, lexerTab, parserTab, semanticsTab, executionTab);
    }

    public Node executionView() {
        Text title = text("Execution");
        title.setFont(Font.font("Courier New", 24));
        title.setFill(Color.WHITE);
        VBox mainBox = new VBox(title);

        VBox expressionBox = new VBox(text("Evaluation", 18));

        if (interpreter == null) {
            VBox errorText = new VBox(text("No code to run..."));
            errorText.setBackground(basic_bg);
            errorText.setPadding(new Insets(20));
            expressionBox.getChildren().add(errorText);
        } else {
            ScrollPane interpreterScroller = new ScrollPane(interpreter.toNode());
            interpreterScroller.setVvalue(1.0);
            VBox interpreterNode = new VBox(interpreterScroller);
            interpreterNode.setBackground(basic_bg);
            interpreterNode.setPadding(new Insets(10));
            interpreterNode.setMinWidth(stage.getWidth() * 0.47);
            expressionBox.getChildren().add(interpreterNode);
        }

        Button runButton = new Button("Run");
        runButton.setOnAction(actionEvent -> {
            interpreter.run();
            refresh(false);
        });
        Button stepButton = new Button("Step");
        stepButton.setOnAction(actionEvent -> {
            interpreter.step();
            refresh(false);
        });
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(actionEvent -> {
            out.clear();
            interpreter = new Interpreter(ast, errorManager, out);
            refresh(false);
        });

        HBox buttonsBox = new HBox();
        if (interpreter != null) {
            if (!interpreter.isCompleted()) {
                buttonsBox = new HBox(restartButton, runButton, stepButton);
            } else {
                buttonsBox = new HBox(restartButton);
            }
        }
        buttonsBox.setSpacing(10);
        expressionBox.setSpacing(10);

        expressionBox.getChildren().add(buttonsBox);

        TableColumn<State.MemoryEntry, String> nameColumn = new TableColumn<>("Variable");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("variable"));
        nameColumn.setMinWidth(115);
        nameColumn.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<State.MemoryEntry, Value> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setMinWidth(120);

        TableColumn<State.MemoryEntry, Type> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setMinWidth(125);

        TableView<State.MemoryEntry> memoryTable = new TableView<>();
        memoryTable.getColumns().add(nameColumn);
        memoryTable.getColumns().add(valueColumn);
        memoryTable.getColumns().add(typeColumn);
        if (stage != null) {
            // memoryTable.setMinWidth(stage.getWidth() * 0.4);
            memoryTable.setMaxHeight(stage.getHeight() * 0.47);
        }

        ArrayList<State.MemoryEntry> memoryEntries = new ArrayList<>();
        if (interpreter != null) {
            // System.out.println(Arrays.toString(interpreter.getState().getMemoryEntries().toArray()));
            memoryEntries = interpreter.getState().getMemoryEntries();
        }
        memoryEntries.add(new State.MemoryEntry("", new ValueWrapper<>(ValueLibrary.blankType, "")));
        memoryTable.setItems(FXCollections.observableArrayList(memoryEntries));



        HBox fullExecutionBox = new HBox(expressionBox);
        fullExecutionBox.setSpacing(20);

        VBox consoleBox = new VBox();
        if (interpreter != null) {
            Text consoleText = text(out.toString());
            consoleBox.getChildren().add(consoleText);
            consoleBox.setMinHeight(stage.getHeight() * .35);
        }

        VBox sidebarBox = new VBox(text("Console", 18), consoleBox);

        sidebarBox.getChildren().add(memoryTable);

        sidebarBox.setBackground(basic_bg);
        sidebarBox.setPadding(new Insets(10));

        fullExecutionBox.getChildren().add(sidebarBox);



        mainBox.getChildren().add(fullExecutionBox);

        mainBox.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        mainBox.setAlignment(Pos.TOP_CENTER);

        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));


        return mainBox;
    }

    public VBox semanticsView() {
        Text title = text("Semantics");
        title.setFont(Font.font("Courier New", 24));
        title.setFill(Color.WHITE);
        VBox mainBox = new VBox(title);

        int totalChecks = 0;
        int checksPassed = 0;

        VBox astBox = new VBox();
        if (ast == null) {
            astBox.getChildren().add(text("No AST..."));
        } else {
            ValidationNodeResult astVNR = ast.getValidationNode();
            totalChecks = astVNR.getTotalChecks();
            checksPassed = astVNR.getPassedChecks();
            astBox.getChildren().add(astVNR.toNode());
        }

        VBox checksSummaryBox = new VBox(
                text(checksPassed+"/"+totalChecks, 48),
                text("Checks Passed", 18)
        );

        HBox summaryBox = new HBox(astBox, checksSummaryBox);

        summaryBox.setBackground(basic_bg);

        mainBox.setBackground(new Background(
                new BackgroundFill(BG_GRAY, CornerRadii.EMPTY, Insets.EMPTY)
        ));
        summaryBox.setAlignment(Pos.TOP_CENTER);
        summaryBox.setSpacing(40);
        summaryBox.setPadding(new Insets(20));

        mainBox.getChildren().add(summaryBox);
        mainBox.setAlignment(Pos.TOP_CENTER);

        mainBox.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.NONE, new CornerRadii(0), BorderStroke.THICK)));

        return mainBox;

    }

    public VBox codeView() {
        TextArea code = new TextArea();
        code.setText(program);
        code.setFont(Font.font("Courier new", 16));
        code.setMaxHeight(Double.MAX_VALUE);

        Button submitButton = new Button("Compile");
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

        CheckBox checkbox = new CheckBox();
        checkbox.selectedProperty().set(simplifyPtn);
        HBox checkboxBox = new HBox(text("Simplify parse tree: "), checkbox);
        checkbox.setOnAction(actionEvent -> {
            simplifyPtn = checkbox.isSelected();
            refresh(false);
        });

        if (headPtn != null) {
            Node headNode = checkbox.isSelected() ? simplifiedHeadPtn.toNode() : headPtn.toNode();
            ScrollPane scroll = new ScrollPane(headNode);
            scroll.setBackground(basic_bg);
            scroll.setPannable(true);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            mainBox.getChildren().add(scroll);
        } else {
            VBox errorText = new VBox(text("No Parse Tree..."));
            errorText.setBackground(basic_bg);
            errorText.setPadding(new Insets(20));
            mainBox.getChildren().add(errorText);
        }

        if (errorManager.hasErrors(Error.ErrorType.PARSER_ERROR)) {
            mainBox.getChildren().add(text(errorManager.toString()));
        }

        mainBox.getChildren().add(checkboxBox);

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

    public static Text text(String s) {
        return text(s, 14);
    }

    public static Text text(String s, int size) {
        Text ret = new Text(s);
        ret.setFill(Color.WHITE);
        ret.setFont(Font.font("Courier New", size));
        return ret;
    }

    public static void main(String[] args) {
        launch();
    }
}