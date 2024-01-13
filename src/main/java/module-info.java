module com.example.langlab {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.langlab to javafx.fxml, javafx.base;
    opens com.example.langlab.Interpreter to javafx.base;

    exports com.example.langlab;
}