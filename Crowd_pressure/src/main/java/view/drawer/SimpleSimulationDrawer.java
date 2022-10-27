package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simulation.Simulation;
import simulation.model.Agent;
import simulation.model.Point;
import simulation.model.Wall;

public class SimpleSimulationDrawer implements SimulationDrawer{

    @Override
    public void draw(GraphicsContext surface, Simulation simulation) {
        // todo: use the graphic context object to draw the simulation state
        surface.clearRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight());

        for(Agent agent : simulation.getAgents()){
            Point position = agent.getPosition();
            if(agent.getVelocity().getValue() < agent.getAgentComfortableSpeed()) surface.setFill(Color.RED);
            else surface.setFill(Color.GREEN);
            double radius = agent.getAgentRadius();
            surface.fillOval(position.getX(), position.getY(), radius, radius);
        }

        surface.setFill(Color.BLACK);
        surface.setLineWidth(1);
        for(Wall wall : simulation.getBoard().getWalls()){
            Point start = wall.getStartPoint();
            Point end = wall.getEndPoint();
            surface.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }
}
