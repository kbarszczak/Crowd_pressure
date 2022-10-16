module simulation.crowed_pressure {
    requires javafx.controls;
    requires javafx.fxml;


    opens view to javafx.fxml;
    exports view;
    exports view.contoller;
    opens view.contoller to javafx.fxml;
}