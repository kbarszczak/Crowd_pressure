package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import view.controller.SimulationController;

import java.io.IOException;
import java.util.Objects;

public class SimulationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // prepare the FXML loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("simulation-view.fxml"));
        // load the view
        BorderPane root = loader.load();
        // get the controller for the loaded view
        SimulationController controller = loader.getController();
        // load css stylesheet
        String css = Objects.requireNonNull(getClass().getResource("simulation-view.css")).toExternalForm();

        // set minimal size of the window
        stage.setMinHeight(449);
        stage.setMinWidth(616);

        Scene scene = new Scene(root); // create scene that visualizes GUI
        scene.getStylesheets().add(css); // set up stylesheet
        stage.setTitle("Crowd pressure"); // set up the stage title
        stage.setScene(scene); // set up main view/scene
        stage.setOnHidden(p -> controller.close()); // run the controller close method when the stage is closing
        stage.show(); // show the view
    }

    public static void main(String[] args) {
        try{
            launch(args);
        }catch (Exception exception){
            System.out.println("Unknown exception occurred. Details: " + exception.getMessage() + ". Application shuts down.");
            exception.printStackTrace();
        }
    }
}
