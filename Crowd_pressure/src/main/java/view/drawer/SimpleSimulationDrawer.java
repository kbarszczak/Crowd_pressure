package view.drawer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import simulation.Simulation;
import simulation.model.Agent;
import simulation.model.Point;
import simulation.model.Vector;
import simulation.model.Wall;
import view.SimulationApplication;

public class SimpleSimulationDrawer implements SimulationDrawer{

    @Override
    public void draw(GraphicsContext surface, Simulation simulation) {
        // todo: use the graphic context object to draw the simulation state
        surface.clearRect(0, 0, surface.getCanvas().getWidth(), surface.getCanvas().getHeight());

        for(Agent agent : simulation.getAgents()){
            if(agent.getVelocity().getValue() < agent.getAgentComfortableSpeed()) surface.setFill(Color.RED);
            else surface.setFill(Color.GREEN);

            Point position = agent.getPosition();
            double radius = agent.getAgentRadius();
            surface.fillOval(position.getX()-radius, position.getY()-radius, 2*radius, 2*radius);

            if(agent.isStopped()) continue;
            if(SimulationApplication.DEBUG_MODE){
                // todo: delete below
                // current velocity
                double angle = agent.getVelocity().getAngle();
                double value = 300;
                Point point = new Vector(value, angle).toPoint();
                double x = position.getX() + point.getX();
                double y = position.getY() + point.getY();
                surface.setStroke(Color.RED);
                surface.strokeLine(position.getX(), position.getY(), x, y);

                // desired position line
                surface.setStroke(Color.BLUE);
                surface.strokeLine(position.getX(), position.getY(), agent.getAgentDesiredPosition().getX(), agent.getAgentDesiredPosition().getY());

                // desired point
                surface.setFill(Color.GREEN);
                surface.fillOval(agent.getAgentDesiredPosition().getX(), agent.getAgentDesiredPosition().getY(), 3, 3);

                // desired velocity line
                Vector tmpVec = agent.getDesiredVelocity();
                tmpVec.setValue(300);
                point = tmpVec.toPoint();
                surface.setStroke(Color.PINK);
                surface.strokeLine(position.getX(), position.getY(), position.getX()+point.getX(), position.getY()+point.getY());

                // view angles
                surface.setStroke(Color.MAGENTA);
                tmpVec = agent.getDesiredVelocity();
                tmpVec.setValue(30000);
                tmpVec.setAngle(tmpVec.getAngle() - agent.getAgentVisionAngle());
                point = tmpVec.toPoint();
                surface.strokeLine(position.getX(), position.getY(), position.getX()+point.getX(), position.getY()+point.getY());

                tmpVec.setAngle(tmpVec.getAngle() + 2*agent.getAgentVisionAngle());
                point = tmpVec.toPoint();
                surface.strokeLine(position.getX(), position.getY(), position.getX()+point.getX(), position.getY()+point.getY());
            }
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
        double scaleX = width / surface.getCanvas().getWidth();
        double scaleY = height / surface.getCanvas().getHeight();

//        System.out.println(scaleX + " " +scaleY);
//        System.out.println(width + " " + height);
//        System.out.println(surface.getCanvas().getWidth() + " " + surface.getCanvas().getHeight());
//        System.out.println();

        surface.scale(scaleX, scaleY);
        surface.getCanvas().setWidth(surface.getCanvas().getWidth() * scaleX);
        surface.getCanvas().setHeight(surface.getCanvas().getHeight() * scaleY);
        draw(surface, simulation);
    }
}
