package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simulation.Simulation;
import simulation.model.Agent;
import simulation.model.Point;
import simulation.model.Wall;

import java.util.HashSet;
import java.util.Set;

public class SimpleSimulationDrawer implements SimulationDrawer{

    private final double desiredPointRadius;

    public SimpleSimulationDrawer(double desiredPointRadius) {
        this.desiredPointRadius = desiredPointRadius;
    }

    private record AgentDesiredPoint(Point point, Color color){}

    @Override
    public void draw(GraphicsContext surface, Simulation simulation) {
        surface.clearRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight());

        Set<AgentDesiredPoint> desiredPointSet = new HashSet<>();
        for(Agent agent : simulation.getAgents().stream().filter(agent -> !agent.isStopped()).toList()){
            Point position = agent.getPosition();
            double radius = agent.getAgentRadius();
            surface.setFill(getColor(agent));
            surface.fillOval(position.getX()-radius, position.getY()-radius, 2*radius, 2*radius);
            desiredPointSet.add(new AgentDesiredPoint(agent.getAgentDesiredPosition(), agent.getColor()));
        }

        surface.setLineWidth(1);
        for(AgentDesiredPoint point : desiredPointSet){
            Point p = point.point;
            surface.setStroke(point.color);
            surface.strokeOval(p.getX() - desiredPointRadius, p.getY() - desiredPointRadius, 2*desiredPointRadius, 2*desiredPointRadius);
        }

        surface.setStroke(Color.BLACK);
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

    private Color getColor(Agent agent){
        Color color = agent.getColor();
        // todo: calculate proper color change
        return color;
    }
}
