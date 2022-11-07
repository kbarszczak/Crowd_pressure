package view.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import simulation.computation.ComputingEngine;
import simulation.computation.MultiThreadComputingEngine;
import simulation.computation.SingleThreadComputingEngine;
import simulation.heuristic.DirectionHeuristic;
import simulation.heuristic.DistanceHeuristic;
import simulation.heuristic.Heuristic;
import simulation.initializer.*;
import simulation.physics.SocialForcePhysicalModel;
import view.drawer.SimulationDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimulationController {

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
    private void start(){
        try{
            timeline.play();
        }catch (Exception exception){
            // todo: handle start exception
            exception.printStackTrace();
        }
    }

    @FXML
    private void stop(){
        try{
            timeline.stop();
        }catch (Exception exception){
            // todo: handle stop exception
            exception.printStackTrace();
        }
    }

    @FXML
    private void reset(){
        try{
            timeline.stop();
            simulation.restoreInitState();
            drawer.draw(simulationCanvas.getGraphicsContext2D(), simulation);
        }catch (Exception exception){
            // todo: handle reset exception
            exception.printStackTrace();
        }
    }

    @FXML
    private void configurator(ActionEvent event){
        try{
            // close the simulation
            close();

            // get stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

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
        }catch (Exception exception){
            // todo: handle configuration exception
            exception.printStackTrace();
        }
    }

    @FXML
    private void exit(){
        try{
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }catch (Exception exception){
            // todo: handle exit exception
            exception.printStackTrace();
        }
    }

    @FXML
    private void changeHeuristics(ActionEvent event){
        try{
            List<Heuristic> heuristics = new ArrayList<>();
            if(directionCheckBox.isSelected()) heuristics.add(new DirectionHeuristic());
            if(distanceCheckBox.isSelected()) heuristics.add(new DistanceHeuristic());
            simulation.setHeuristics(heuristics);
        }catch (Exception exception){
            // todo: handle change heuristic exception
            exception.printStackTrace();
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
            exception.printStackTrace();
        }
    }

    public void scaleDrawer(){
        try{
            drawer.scale((int)simulationPane.getWidth(), (int)simulationPane.getHeight(), simulationCanvas.getGraphicsContext2D(), simulation);
        }catch (Exception exception){
            // todo: handle scale exception
            exception.printStackTrace();
        }
    }

    public void initialize(int agentCount, double scaleCoefficient, double destinationRadius, double delayMs, ComputingEngine engine, AgentsInitializer agentsInitializer, BoardInitializer boardInitializer, SimulationDrawer drawer) throws IllegalStateException{
        try{
            if(simulation != null) throw new IllegalStateException("Cannot initialize controller because the controller is already initialized");

            this.timeline = new Timeline(new KeyFrame(new Duration(delayMs), this::step));
            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.simulation = new Simulation(
                    (int)simulationCanvas.getWidth(),
                    (int)simulationCanvas.getHeight(),
                    agentCount,
                    new SocialForcePhysicalModel(scaleCoefficient, destinationRadius, delayMs),
                    List.of(new DistanceHeuristic(), new DirectionHeuristic()),
                    engine,
                    boardInitializer,
                    agentsInitializer
            );
            this.drawer = drawer;
            this.drawer.draw(simulationCanvas.getGraphicsContext2D(), this.simulation);
            this.drawer.scale((int)simulationPane.getWidth(), (int)simulationPane.getHeight(), simulationCanvas.getGraphicsContext2D(), simulation);

            if(engine instanceof MultiThreadComputingEngine){
                multiRadioButton.selectedProperty().setValue(true);
            }else if(engine instanceof SingleThreadComputingEngine){
                singleRadioButton.selectedProperty().setValue(true);
            }
        } catch (IllegalStateException exception){
            throw exception;
        } catch (Exception exception){
            // todo: catch the exception
            exception.printStackTrace();
        }
    }

    public void close() {
        try{
            timeline.stop();
            simulation.close();
        }catch (Exception exception){
            System.out.println("Could not close the simulation. Details: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void step(ActionEvent event){
        try{
            simulation.step(); // do all the necessary calculations in the simulation
            drawer.draw(simulationCanvas.getGraphicsContext2D(), simulation); // draw the simulation state on the canvas. In other words, visualize the simulations state
        }catch (Exception exception){
            // todo: handle step exception
            exception.printStackTrace();
        }
    }
}