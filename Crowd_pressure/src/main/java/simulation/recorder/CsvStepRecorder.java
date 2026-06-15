package simulation.recorder;

import simulation.model.Agent;
import simulation.model.Point;
import simulation.model.Vector;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvStepRecorder implements StepRecorder {

    private static final String HEADER = "step,t,id,group,x,y,vx,vy,speed,pressure,stopped";

    private final Writer writer;
    private final double timeQuantumSeconds;
    private final int sampleEvery;

    public CsvStepRecorder(Path outputFile, double timeQuantumMs, int sampleEvery) throws IOException {
        Files.createDirectories(outputFile.toAbsolutePath().getParent());
        this.writer = new BufferedWriter(Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8));
        this.timeQuantumSeconds = timeQuantumMs / 1000.0;
        this.sampleEvery = Math.max(1, sampleEvery);
        this.writer.write(HEADER);
        this.writer.write('\n');
    }

    @Override
    public void record(long step, List<Agent> agents) throws Exception {
        if (step % sampleEvery != 0) return;
        double time = step * timeQuantumSeconds;
        StringBuilder builder = new StringBuilder();
        for (int id = 0; id < agents.size(); ++id) {
            appendRow(builder, step, time, id, agents.get(id));
        }
        writer.write(builder.toString());
    }

    private void appendRow(StringBuilder builder, long step, double time, int id, Agent agent) {
        Point position = agent.getPosition();
        Vector velocity = agent.getVelocity();
        Point velocityComponents = velocity.toPoint();
        builder.append(step).append(',')
                .append(time).append(',')
                .append(id).append(',')
                .append(agent.getColor().toString()).append(',')
                .append(position.getX()).append(',')
                .append(position.getY()).append(',')
                .append(velocityComponents.getX()).append(',')
                .append(velocityComponents.getY()).append(',')
                .append(velocity.getValue()).append(',')
                .append(agent.getPressure()).append(',')
                .append(agent.isStopped() ? 1 : 0).append('\n');
    }

    @Override
    public void close() throws IOException {
        writer.flush();
        writer.close();
    }
}
