package view.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import simulation.Simulation;
import simulation.computation.MultiThreadComputingEngine;
import simulation.computation.SingleThreadComputingEngine;
import simulation.heuristic.DirectionHeuristic;
import simulation.heuristic.DistanceHeuristic;
import simulation.initializer.Map1AgentsInitializer;
import simulation.initializer.Map1BoardInitializer;
import simulation.initializer.TestAgentsInitializer;
import simulation.initializer.TestBoardInitializer;
import simulation.physics.SocialForcePhysicalModel;
import view.drawer.SimpleSimulationDrawer;
import view.drawer.SimulationDrawer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SimulationController implements Initializable {

    private final Timeline timeline;
    private SimulationDrawer drawer;
    private Simulation simulation;

    @FXML
    private Button startButton, stopButton, resetButton, exitButton;
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

    public SimulationController() {
        this.timeline = new Timeline(new KeyFrame(Duration.millis(10), this::step));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.simulation = null;
    }

    @FXML
    private void start(){
        try{
            timeline.play();
        }catch (Exception exception){
            // todo: handle start exception
        }
    }

    @FXML
    private void stop(){
        try{
            timeline.stop();
        }catch (Exception exception){
            // todo: handle stop exception
        }
    }

    @FXML
    private void reset(){
        try{
            // todo: write reset
        }catch (Exception exception){
            // todo: handle reset exception
        }
    }

    @FXML
    private void exit(){
        try{
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }catch (Exception exception){
            // todo: handle exit exception
        }
    }

    @FXML
    private void changeHeuristics(ActionEvent event){
        try{
            // todo: write change heuristics code
        }catch (Exception exception){
            // todo: handle change heuristic exception
        }
    }

    @FXML
    private void changeEngine(ActionEvent event){
        try{
            if(singleRadioButton.isSelected()){
                simulation.setEngine(new SingleThreadComputingEngine());
            }else if(multiRadioButton.isSelected()){
                simulation.setEngine(new MultiThreadComputingEngine());
            }
        }catch (Exception exception){
            // todo: handle change engine exception
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            if(simulation != null) simulation.close();
            simulation = new Simulation(
                    (int)simulationCanvas.getWidth(),
                    (int)simulationCanvas.getHeight(),
                    80,
                    new SocialForcePhysicalModel(1000, 3, timeline.getKeyFrames().get(0).getTime().toMillis()), // the physical model used in the simulation
                    List.of(new DistanceHeuristic(), new DirectionHeuristic()), // the list of heuristics used in the simulation (don't pass null value)
                    new MultiThreadComputingEngine(), // the computing engine responsible for doing all the calculations
                    //new SingleThreadComputingEngine(), // the computing engine responsible for doing all the calculations
                    new Map1BoardInitializer(), // the object that is responsible for initializing the board
                    new Map1AgentsInitializer() // the object that is responsible for initializing the agent
            );
            drawer = new SimpleSimulationDrawer();
            drawer.draw(simulationCanvas.getGraphicsContext2D(), simulation);
        }catch (Exception exception){
            // todo: handle initialization exception
        }
    }

    public void scaleDrawer(){
        drawer.scale((int)simulationPane.getWidth(), (int)simulationPane.getHeight(), simulationCanvas.getGraphicsContext2D(), simulation);
    }

    public void close() {
        try{
            simulation.close();
        }catch (Exception exception){
            System.out.println("Could not close the simulation. Details: " + exception.getMessage());
        }
    }

    private void step(ActionEvent event){
        try{
            simulation.step(); // do all the necessary calculations in the simulation
            drawer.draw(simulationCanvas.getGraphicsContext2D(), simulation); // draw the simulation state on the canvas. In other words, visualize the simulations state
        }catch (Exception exception){
            // todo: handle step exception
        }
    }
}