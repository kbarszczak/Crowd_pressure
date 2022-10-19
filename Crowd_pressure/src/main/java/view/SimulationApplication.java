package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import simulation.Simulation;
import simulation.computation.MultiThreadComputingEngine;
import simulation.heuristic.DirectionHeuristic;
import simulation.heuristic.DistanceHeuristic;
import simulation.initializer.FixedAgentInitializer;
import simulation.initializer.FixedBoardInitializer;
import simulation.physics.SocialForcePhysicalModel;

import java.io.IOException;
import java.util.List;

public class SimulationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimulationApplication.class.getResource("simulation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Crowed pressure");
        stage.setScene(scene);
        stage.show();
        // todo: initialize view and the layout here
    }

    public static void main(String[] args) {
        try{
            // create/set up simulation
            Simulation simulation = new Simulation(
                    new SocialForcePhysicalModel(), // the physical model used in the simulation
                    List.of(new DistanceHeuristic(), new DirectionHeuristic()), // the list of heuristics used in the simulation (don't pass null value)
                    new MultiThreadComputingEngine(), // the computing engine responsible for doing all the calculations
                    new FixedBoardInitializer(), // the object that is responsible for initializing the board
                    100,
                    new FixedAgentInitializer() // the object that is responsible for initializing the agent
            );
            // run/start the simulation
            launch(args);
            simulation.start();
        }catch (Exception exception){
            System.out.println("Unknown exception occurred. Details: " + exception.getMessage() + ". Application shuts down.");
        }
    }
}
