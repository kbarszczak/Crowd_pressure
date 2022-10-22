package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import simulation.Simulation;

public class SimpleSimulationDrawer implements SimulationDrawer{

    @Override
    public void draw(GraphicsContext surface, Simulation simulation) {
        // todo: use the graphic context object to draw the simulation state
        surface.setFill(Color.GREEN);
        surface.fillRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight());
        int borderWidth = 2;
        surface.clearRect(borderWidth, borderWidth, surface.getCanvas().getWidth()-borderWidth*2, surface.getCanvas().getHeight()-borderWidth*2);

    }
}
