package view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import simulation.computation.ComputingEngine;
import simulation.computation.MultiThreadComputingEngine;
import simulation.computation.SingleThreadComputingEngine;
import simulation.initializer.agent.*;
import simulation.initializer.board.*;
import view.drawer.SimpleSimulationDrawer;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {

    public enum Engine {
        Single_thread,
        Multi_thread
    }

    public enum Map {
        Test_map,
        Map_1,
        Map_2,
        Map_3,
        Map_4,
        Map_5,
        Map_6,
        Map_7
    }

    @FXML
    private FlowPane mainFlowPane, countFlowPane, scaleFlowPane, radiusFlowPane, delayFlowPane, engineFlowPane, mapFlowPane;

    @FXML
    private Label welcomeLabel, agentCountLabel, scaleCoefficientLabel, destinationRadiusLabel, delayLabel, engineLabel, mapLabel;

    @FXML
    private Slider countSlider, scaleSlider, radiusSlider, delaySlider;

    @FXML
    private ComboBox<Engine> engineComboBox;

    @FXML
    private ComboBox<Map> mapComboBox;

    @FXML
    private Button submitButton;

    @FXML
    public void submit(ActionEvent event) {
        try {
            int agentCount = (int) countSlider.getValue();
            double scaleCoefficient = scaleSlider.getValue();
            double destinationRadius = radiusSlider.getValue();
            double delayMs = delaySlider.getValue();
            ComputingEngine engine = null;
            BoardInitializer boardInitializer = null;
            AgentsInitializer agentsInitializer = null;

            switch (engineComboBox.getSelectionModel().getSelectedItem()) {
                case Single_thread -> engine = new SingleThreadComputingEngine();
                case Multi_thread -> engine = new MultiThreadComputingEngine();
            }

            if (engine == null) throw new Exception("Engine is null");

            switch (mapComboBox.getSelectionModel().getSelectedItem()) {
                case Map_1 -> {
                    agentsInitializer = new Map1AgentsInitializer();
                    boardInitializer = new Map1BoardInitializer();
                }
                case Map_2 ->{
                    agentsInitializer = new Map2AgentsInitializer();
                    boardInitializer = new Map2BoardInitializer();
                }
                case Map_3 ->{
                    agentsInitializer = new Map3AgentsInitializer();
                    boardInitializer = new Map3BoardInitializer();
                }
                case Map_4 ->{
                    agentsInitializer = new Map4AgentsInitializer();
                    boardInitializer = new Map4BoardInitializer();
                }
                case Map_5 ->{
                    agentsInitializer = new Map5AgentsInitializer();
                    boardInitializer = new Map5BoardInitializer();
                }
                case Map_6 ->{
                    agentsInitializer = new Map6AgentsInitializer();
                    boardInitializer = new Map6BoardInitializer();
                }
                case Map_7 ->{
                    agentsInitializer = new Map7AgentsInitializer();
                    boardInitializer = new Map7BoardInitializer();
                }
                case Test_map -> {
                    agentsInitializer = new TestAgentsInitializer();
                    boardInitializer = new TestBoardInitializer();
                }
            }

            if (agentsInitializer == null) {
                engine.close();
                throw new Exception("Either Agents or Board initializer is null");
            }

            // read css and get stage
            String css = Objects.requireNonNull(getClass().getResource("/styles/simulation-view.css")).toExternalForm();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // leading view and setting up controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/simulation-view.fxml"));
            Parent root = loader.load();
            SimulationController controller = loader.getController();
            controller.initialize(
                    agentCount,
                    scaleCoefficient,
                    destinationRadius,
                    delayMs,
                    engine,
                    agentsInitializer,
                    boardInitializer,
                    new SimpleSimulationDrawer(destinationRadius)
            );

            // creating scene
            Scene scene = new Scene(root);
            scene.widthProperty().addListener((ov, ab, t1) -> controller.scaleDrawer());
            scene.heightProperty().addListener((ov, ab, t1) -> controller.scaleDrawer());
            scene.getStylesheets().add(css);

            // initializing stage
            stage.setTitle("Crowd pressure simulation");
            stage.setOnHidden(p -> controller.close());
            stage.fullScreenProperty().addListener((ov, ab, t1) -> controller.scaleDrawer());
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("During the initialization the following error occurred: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engineComboBox.getItems().addAll(Engine.Multi_thread, Engine.Single_thread);
        engineComboBox.getSelectionModel().select(0);
        mapComboBox.getItems().addAll(Map.Map_1, Map.Map_2, Map.Map_3, Map.Map_4, Map.Map_5, Map.Map_6, Map.Map_7, Map.Test_map);
        mapComboBox.getSelectionModel().select(0);
    }
}
