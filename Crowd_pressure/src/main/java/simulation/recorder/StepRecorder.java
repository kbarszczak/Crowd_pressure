package simulation.recorder;

import simulation.model.Agent;

import java.io.Closeable;
import java.util.List;

public interface StepRecorder extends Closeable {

    void record(long step, List<Agent> agents) throws Exception;
}
