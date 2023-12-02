package view.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Simulation;
import simulation.computation.ComputingEngine;
import simulation.computation.MultiThreadComputingEngine;
import simulation.computation.SingleThreadComputingEngine;
import simulation.heuristic.DirectionHeuristic;
import simulation.heuristic.DistanceHeuristic;
import simulation.heuristic.Heuristic;
import simulation.initializer.board.BoardInitializer;
import simulation.initializer.agent.AgentsInitializer;
import simulation.physics.SocialForcePhysicalModel;
import view.drawer.SimulationDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationController {

    private Long elapsedTicks = 0L;
    private Timeline timeline;
    private SimulationDrawer drawer;
    private Simulation simulation;

    @FXML
    private Button startButton, stopButton, resetButton, exitButton, configuratorButton;
    @FXML
    private CheckBox directionCheckBox, distanceCheckBox;
    @FXML
    private RadioButton multiRadioButton, singleRadioButton;
    @FXML
    public ToggleGroup thread;
    @FXML
    private FlowPane menuFlowPane, optionsFlowPane;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Canvas simulationCanvas;
    @FXML
    private Pane simulationPane;
    @FXML
    private Text timeText;

    @FXML
    private void start() {
        try {
            timeline.play();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("Cannot start the simulation due to the following exception: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void stop() {
        try {
            timeline.stop();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("Cannot stop the simulation due to the following exception: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void reset() {
        try {
            timeline.stop();
            simulation.restoreInitState();
            drawer.draw(simulationCanvas.getGraphicsContext2D(), simulation);
            elapsedTicks = 0L;
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("Cannot reset the simulation due to the following exception: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
            timeline.play();
        }
    }

    @FXML
    private void configurator(ActionEvent event) {
        try {
            // close the simulation
            close();

            // get stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // load the view and the css
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/configuration-view.fxml"));
            String css = Objects.requireNonNull(getClass().getResource("/styles/configuration-view.css")).toExternalForm();
            Parent root = loader.load();

            // create scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add(css);

            // set up stage
            stage.setTitle("Crowd pressure configuration");
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("The following exception occurred: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void exit() {
        try {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        } catch (Exception ignore) {
        } // the exception is handled by the close() method
    }

    @FXML
    private void changeHeuristics(ActionEvent event) {
        try {
            List<Heuristic> heuristics = new ArrayList<>();
            if (directionCheckBox.isSelected()) heuristics.add(new DirectionHeuristic());
            if (distanceCheckBox.isSelected()) heuristics.add(new DistanceHeuristic());
            simulation.setHeuristics(heuristics);
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("Cannot change the heuristic because of the following exception: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void changeEngine(ActionEvent event) {
        try {
            if (singleRadioButton.isSelected()) {
                simulation.setEngine(new SingleThreadComputingEngine());
            } else if (multiRadioButton.isSelected()) {
                simulation.setEngine(new MultiThreadComputingEngine());
            }
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("An Exception occurred");
            alert.setContentText("Cannot change the computing engine because of the following exception: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
            alert.showAndWait();
        }
    }

    public void scaleDrawer() {
        try {
            drawer.scale((int) simulationPane.getWidth(), (int) simulationPane.getHeight(), simulationCanvas.getGraphicsContext2D(), simulation);
        } catch (Exception ignore) {
        }
    }

    public void initialize(int agentCount, double scaleCoefficient, double destinationRadius, double delayMs, ComputingEngine engine, AgentsInitializer agentsInitializer, BoardInitializer boardInitializer, SimulationDrawer drawer) throws IllegalStateException {
        if (simulation != null)
            throw new IllegalStateException("Cannot initialize controller because the controller is already initialized");

        this.timeline = new Timeline(new KeyFrame(new Duration(delayMs), this::step));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.simulation = new Simulation(
                (int) simulationCanvas.getWidth(),
                (int) simulationCanvas.getHeight(),
                agentCount,
                new SocialForcePhysicalModel(scaleCoefficient, destinationRadius, delayMs),
                List.of(new DistanceHeuristic(), new DirectionHeuristic()),
                engine,
                boardInitializer,
                agentsInitializer
        );
        this.drawer = drawer;
        this.drawer.draw(simulationCanvas.getGraphicsContext2D(), this.simulation);
        this.drawer.scale((int) simulationPane.getWidth(), (int) simulationPane.getHeight(), simulationCanvas.getGraphicsContext2D(), simulation);

        if (engine instanceof MultiThreadComputingEngine) {
            multiRadioButton.selectedProperty().setValue(true);
        } else if (engine instanceof SingleThreadComputingEngine) {
            singleRadioButton.selectedProperty().setValue(true);
        }
    }

    public void close() {
        try {
            timeline.stop();
            simulation.close();
        } catch (Exception exception) {
            Platform.exit();
        }
    }

    private void step(ActionEvent event) {
        try {
            // do all the necessary calculations in the simulation
            double currentTime = timeline.currentTimeProperty().getValue().toSeconds() * elapsedTicks;
            if(!simulation.step()) {
                timeText.setText(String.format("Simulation time %.2fs", currentTime)); // set current simulation time
                drawer.draw(simulationCanvas.getGraphicsContext2D(), simulation); // draw the simulation state on the canvas. In other words, visualize the simulations state
                elapsedTicks += 1;
            } else {
                timeText.setText(String.format("Simulation finished in %.2fs", currentTime)); // set the total time of the simulation
                stop();
            }
        } catch (Exception exception) {
            System.out.println("Step() method generated the following exception: " + exception.getClass().getName() + ". Details: " + exception.getMessage());
        }
    }
}