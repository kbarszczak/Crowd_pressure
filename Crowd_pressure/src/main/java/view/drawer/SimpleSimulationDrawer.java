package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import simulation.Simulation;
import simulation.model.Agent;
import simulation.model.Point;
import simulation.model.Wall;

public class SimpleSimulationDrawer implements SimulationDrawer{

    @Override
    public void draw(GraphicsContext surface, Simulation simulation) {
        // todo: use the graphic context object to draw the simulation state
        surface.setFill(Color.GREEN);
        surface.fillRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight());
        int borderWidth = 2;
        surface.clearRect(borderWidth, borderWidth, surface.getCanvas().getWidth()-borderWidth*2, surface.getCanvas().getHeight()-borderWidth*2);

        for(Agent agent : simulation.getAgents()){
            Point position = agent.getPosition();
            double radius = agent.getAgentRadius();
            surface.fillOval(position.getX(), position.getY(), radius, radius);

            System.out.printf("(%d, %d)\n", agent.getPosition().getX(), agent.getPosition().getY());
        }

        for(Wall wall : simulation.getBoard().getWalls()){
            Point start = wall.getStartPoint();
            Point end = wall.getEndPoint();
            surface.setLineWidth(3);
            surface.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }
}
