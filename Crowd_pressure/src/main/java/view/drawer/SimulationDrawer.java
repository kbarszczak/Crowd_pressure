package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import simulation.Simulation;

public interface SimulationDrawer {

    /**
     * The method is responsible to draw the simulation on the given graphic context
     * @param surface the graphic context that is used to draw the simulation state
     * @param simulation the simulation that is drawn
     */
    void draw(GraphicsContext surface, Simulation simulation);

    /**
     * The method can be used to set up drawing environment
     * @param stage the stage of the view
     * @param surface the canvas surface object
     * @param simulation the simulation that is drawn // todo: write this
     */
    void scale(int width, int height, GraphicsContext surface, Simulation simulation);

}
