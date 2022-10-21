package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import simulation.Simulation;

public interface SimulationDrawer {

    /**
     * The method is responsible to draw the simulation on the given graphic context
     * @param surface the graphic context that is used to draw the simulation state
     * @param simulation the simulation that is drawn
     */
    void draw(GraphicsContext surface, Simulation simulation);

}
