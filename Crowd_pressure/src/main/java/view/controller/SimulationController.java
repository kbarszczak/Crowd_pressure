package view.controller;

import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class SimulationController {

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

    public void start(ActionEvent event){
        // todo: start the simulation
    }

    public void stop(ActionEvent event){
        // todo: stop the simulation
    }

    public void reset(ActionEvent event){
        // todo: reset the simulation
    }

    public void exit(ActionEvent event){
        // todo: exit app
    }

    public void changeHeuristics(ActionEvent event){
        // todo: write change heuristics code
    }

    public void changeEngine(ActionEvent event){
        // todo: write change engine code
    }

}
