module com.example.langlab {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.langlab to javafx.fxml;
    exports com.example.langlab;
}