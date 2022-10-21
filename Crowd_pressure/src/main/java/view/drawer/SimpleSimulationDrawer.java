package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import simulation.Simulation;

public class SimpleSimulationDrawer implements SimulationDrawer{

    @Override
    public void draw(GraphicsContext surface, Simulation simulation) {
        // todo: use the graphic context object to draw the simulation state
        surface.clearRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight()); // clear the surface
    }
}
