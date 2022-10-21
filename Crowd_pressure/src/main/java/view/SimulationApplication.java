package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import simulation.Simulation;
import simulation.computation.ComputingEngine;
import simulation.computation.MultiThreadComputingEngine;
import simulation.heuristic.DirectionHeuristic;
import simulation.heuristic.DistanceHeuristic;
import simulation.initializer.FixedAgentInitializer;
import simulation.initializer.FixedBoardInitializer;
import simulation.physics.SocialForcePhysicalModel;
import view.controller.SimulationController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class SimulationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("simulation-view.fxml"));
        BorderPane root = loader.load();
        SimulationController controller = loader.getController();
        String css = Objects.requireNonNull(getClass().getResource("simulation-view.css")).toExternalForm();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);
        stage.setTitle("Crowd pressure");
        stage.setScene(scene);
        stage.setOnHidden(p -> controller.close());
        stage.show();
    }

    public static void main(String[] args) {
        try(ComputingEngine engine = new MultiThreadComputingEngine()){
            // create/set up simulation
            Simulation simulation = new Simulation(
                    new SocialForcePhysicalModel(), // the physical model used in the simulation
                    List.of(new DistanceHeuristic(), new DirectionHeuristic()), // the list of heuristics used in the simulation (don't pass null value)
                    engine, // the computing engine responsible for doing all the calculations
                    new FixedBoardInitializer(), // the object that is responsible for initializing the board
                    100,
                    new FixedAgentInitializer() // the object that is responsible for initializing the agent
            );
            // run/start the simulation
            //simulation.start();
            launch(args);
        }catch (Exception exception){
            System.out.println("Unknown exception occurred. Details: " + exception.getMessage() + ". Application shuts down.");
            exception.printStackTrace();
        }
    }
}
