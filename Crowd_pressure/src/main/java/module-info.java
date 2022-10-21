module simulation.crowed_pressure {
    requires javafx.controls;
    requires javafx.fxml;


    opens view to javafx.fxml;
    exports view;
    exports view.controller;
    opens view.controller to javafx.fxml;
}