module ucf.assignments {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens ucf.assignments to javafx.fxml;
    exports ucf.assignments;
}