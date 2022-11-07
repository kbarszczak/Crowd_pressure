package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SimulationApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // prepare the FXML loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/configuration-view.fxml"));
        // load the view
        Parent root = loader.load();
        // load css stylesheet
        String css = Objects.requireNonNull(getClass().getResource("/styles/configuration-view.css")).toExternalForm();

        // create screen
        Scene scene = new Scene(root); // create scene that visualizes GUI
        scene.getStylesheets().add(css); // set up stylesheet

        // set up stage
        stage.setTitle("Crowd pressure configuration"); // set up the stage title
        stage.setMinHeight(480);
        stage.setMinWidth(600);
        stage.setScene(scene); // set up main view/scene
        stage.show(); // show the view
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception exception) {
            System.out.println("Unknown exception occurred. Details: " + exception.getMessage() + ". Application shuts down.");
            exception.printStackTrace();
        }
    }
}
