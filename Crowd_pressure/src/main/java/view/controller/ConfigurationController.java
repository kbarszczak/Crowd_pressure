package view.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {

    private enum Engines{
        Single_thread,
        Multi_thread
    }

    private enum Map{
        Test_map,
        Map_1
    }

    @FXML
    private FlowPane mainFlowPane, countFlowPane, scaleFlowPane, radiusFlowPane, delayFlowPane, engineFlowPane, mapFlowPane;

    @FXML
    private Label welcomeLabel, agentCountLabel, scaleCoefficientLabel, destinationRadiusLabel, delayLabel, engineLabel, mapLabel;

    @FXML
    private Slider countSlider, scaleSlider, radiusSlider, delaySlider;

    @FXML
    private ComboBox<String> engineComboBox, mapComboBox;

    @FXML
    private Button submitButton;

    public void submit(ActionEvent event) {
        try{
            // todo: verify data and change view
            // prepare the FXML loader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/simulation-view.fxml"));
            // load the view
            Parent root = loader.load();
            // get the stage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            // get the controller for the loaded view
            SimulationController controller = loader.getController();

            // todo: configure controller here

            // load css stylesheet
            String css = Objects.requireNonNull(getClass().getResource("/styles/simulation-view.css")).toExternalForm();

            Scene scene = new Scene(root); // create scene that visualizes GUI
            scene.widthProperty().addListener(p -> controller.scaleDrawer()); // scale the drawer and the canvas when window changes its width
            scene.heightProperty().addListener(p -> controller.scaleDrawer()); // scale the drawer and the canvas when window changes its height
            scene.getStylesheets().add(css); // set up stylesheet

            stage.setTitle("Crowd pressure simulation"); // set up the stage title
            stage.setMinHeight(449);
            stage.setMinWidth(616);
            stage.setScene(scene); // set up main view/scene
            stage.setOnHidden(p -> controller.close()); // run the controller close method when the stage is closing
            stage.show(); // show the view
        }catch (Exception exception){
            // todo: handle the exception
            exception.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engineComboBox.getItems().addAll(Engines.Single_thread.toString(), Engines.Multi_thread.toString());
        engineComboBox.getSelectionModel().select(1);
        mapComboBox.getItems().addAll(Map.Map_1.toString(), Map.Test_map.toString());
        mapComboBox.getSelectionModel().select(0);
    }
}
