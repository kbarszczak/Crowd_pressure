package app;

import simulation.Simulation;
import simulation.computation.SingleThreadComputingEngine;
import simulation.heuristic.DirectionHeuristic;
import simulation.heuristic.DistanceHeuristic;
import simulation.heuristic.Heuristic;
import simulation.initializer.agent.*;
import simulation.initializer.board.*;
import simulation.model.Agent;
import simulation.physics.SocialForcePhysicalModel;
import simulation.recorder.CsvStepRecorder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BatchRunner {

    private static final int WIDTH = 750;
    private static final int HEIGHT = 450;
    private static final double SCALE_COEFFICIENT = 1000.0;
    private static final double DESTINATION_RADIUS = 3.0;
    private static final double DELAY_MS = 50.0;
    private static final int MAX_STEPS = 2500;
    private static final int DETAIL_SAMPLE_EVERY = 5;

    private static final int[] AGENT_COUNTS = {25, 50, 75, 100, 150};
    private static final int REPETITIONS = 3;
    private static final String[] MAPS = {"Map1", "Map7"};
    private static final String[] SCENARIOS = {"none", "both"};

    public static void main(String[] args) throws Exception {
        Path outputDir = Paths.get(args.length > 0 ? args[0] : "data");
        Path detailDir = outputDir.resolve("detail");
        Files.createDirectories(detailDir);

        try (BufferedWriter summary = Files.newBufferedWriter(outputDir.resolve("summary.csv"), StandardCharsets.UTF_8)) {
            summary.write("map,scenario,agent_count,scale,rep,steps,sim_time_s,evacuated,total,evac_fraction,evac_time_s,mean_pressure,max_pressure,mean_speed\n");
            for (String map : MAPS) {
                for (String scenario : SCENARIOS) {
                    for (int agentCount : AGENT_COUNTS) {
                        for (int rep = 0; rep < REPETITIONS; ++rep) {
                            runOne(map, scenario, agentCount, rep, summary, detailDir);
                        }
                    }
                }
            }
        }
        System.out.println("Batch finished. Output in: " + outputDir.toAbsolutePath());
    }

    private static void runOne(String map, String scenario, int agentCount, int rep, BufferedWriter summary, Path detailDir) throws Exception {
        Simulation simulation = new Simulation(
                WIDTH, HEIGHT, agentCount,
                new SocialForcePhysicalModel(SCALE_COEFFICIENT, DESTINATION_RADIUS, DELAY_MS),
                heuristics(scenario),
                new SingleThreadComputingEngine(),
                boardInitializer(map),
                agentInitializer(map)
        );

        boolean detailed = rep == 0 && agentCount == 150 && scenario.equals("both");
        if (detailed) {
            String name = map + "_" + scenario + "_n" + agentCount + "_rep" + rep + ".csv";
            simulation.setRecorder(new CsvStepRecorder(detailDir.resolve(name), DELAY_MS, DETAIL_SAMPLE_EVERY));
        }

        RunStats stats = simulate(simulation);
        writeSummaryRow(summary, map, scenario, agentCount, rep, stats);
        summary.flush();
        simulation.close();
        System.out.printf("done: %s %s n=%d rep=%d -> evac %d/%d in %.1fs%n",
                map, scenario, agentCount, rep, stats.evacuated(), stats.total(), stats.evacTimeSeconds());
    }

    private static RunStats simulate(Simulation simulation) {
        double dt = DELAY_MS / 1000.0;
        double pressureSum = 0, pressureMax = 0, speedSum = 0;
        long sampleCount = 0;
        int steps = 0;
        double evacTime = -1;

        boolean finished = false;
        while (!finished && steps < MAX_STEPS) {
            for (Agent agent : simulation.getAgents()) {
                if (agent.isStopped()) continue;
                pressureSum += agent.getPressure();
                pressureMax = Math.max(pressureMax, agent.getPressure());
                speedSum += agent.getVelocity().getValue();
                sampleCount++;
            }
            finished = simulation.step();
            steps++;
            if (finished && evacTime < 0) evacTime = steps * dt;
        }

        List<Agent> agents = simulation.getAgents();
        int evacuated = (int) agents.stream().filter(Agent::isStopped).count();
        return new RunStats(steps, steps * dt, evacuated, agents.size(),
                finished ? evacTime : -1,
                sampleCount == 0 ? 0 : pressureSum / sampleCount, pressureMax,
                sampleCount == 0 ? 0 : speedSum / sampleCount);
    }

    private static void writeSummaryRow(BufferedWriter summary, String map, String scenario, int agentCount, int rep, RunStats s) throws IOException {
        summary.write(String.format(java.util.Locale.ROOT, "%s,%s,%d,%.1f,%d,%d,%.3f,%d,%d,%.4f,%.3f,%.5f,%.5f,%.5f%n",
                map, scenario, agentCount, SCALE_COEFFICIENT, rep, s.steps(), s.simTimeSeconds(),
                s.evacuated(), s.total(), s.total() == 0 ? 0 : (double) s.evacuated() / s.total(),
                s.evacTimeSeconds(), s.meanPressure(), s.maxPressure(), s.meanSpeed()));
    }

    private static List<Heuristic> heuristics(String scenario) {
        List<Heuristic> heuristics = new ArrayList<>();
        if (scenario.equals("both") || scenario.equals("distance")) heuristics.add(new DistanceHeuristic());
        if (scenario.equals("both") || scenario.equals("direction")) heuristics.add(new DirectionHeuristic());
        return heuristics;
    }

    private static BoardInitializer boardInitializer(String map) {
        return map.equals("Map7") ? new Map7BoardInitializer() : new Map1BoardInitializer();
    }

    private static AgentsInitializer agentInitializer(String map) {
        return map.equals("Map7") ? new Map7AgentsInitializer() : new Map1AgentsInitializer();
    }

    private record RunStats(int steps, double simTimeSeconds, int evacuated, int total,
                            double evacTimeSeconds, double meanPressure, double maxPressure, double meanSpeed) {
    }
}
