module simulation.crowed_pressure {
    requires javafx.controls;
    requires javafx.fxml;


    opens simulation.crowed_pressure to javafx.fxml;
    exports simulation.crowed_pressure;
}