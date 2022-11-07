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
        surface.clearRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight());

        for(Agent agent : simulation.getAgents()){
            if(agent.getVelocity().getValue() < agent.getAgentComfortableSpeed()/3.0) surface.setFill(Color.RED);
            else if(agent.getVelocity().getValue() < agent.getAgentComfortableSpeed()/2.0) surface.setFill(Color.ORANGE);
            else surface.setFill(Color.GREEN);

            Point position = agent.getPosition();
            double radius = agent.getAgentRadius();
            surface.fillOval(position.getX()-radius, position.getY()-radius, 2*radius, 2*radius);
        }

        surface.setStroke(Color.BLACK);
        surface.setLineWidth(1);
        for(Wall wall : simulation.getBoard().getWalls()){
            Point start = wall.getStartPoint();
            Point end = wall.getEndPoint();
            surface.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
        }
    }

    @Override
    public void scale(int width, int height, GraphicsContext surface, Simulation simulation) {
        if(width<simulation.getBoard().getWidth() || height<simulation.getBoard().getHeight()) return;

        double scaleX = width / surface.getCanvas().getWidth();
        double scaleY = height / surface.getCanvas().getHeight();

        surface.scale(scaleX, scaleY);
        surface.getCanvas().setWidth(surface.getCanvas().getWidth() * scaleX);
        surface.getCanvas().setHeight(surface.getCanvas().getHeight() * scaleY);
        draw(surface, simulation);
    }
}
