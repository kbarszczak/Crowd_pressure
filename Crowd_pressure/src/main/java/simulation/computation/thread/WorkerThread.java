package simulation.computation.thread;

import simulation.computation.task.Task;

import java.util.concurrent.CountDownLatch;

public class WorkerThread implements Runnable{

    private final Task task;
    private final CountDownLatch cdl;

    public WorkerThread(Task task) {
        this.task = task;
        this.cdl = null;
    }

    public WorkerThread(Task task, CountDownLatch cdl) {
        this.task = task;
        this.cdl = cdl;
    }

    @Override
    public void run() {
        try{
            task.execute();
        }catch (Exception exception){
            System.out.println("Computation thread \"" + Thread.currentThread().getName() + "\" is dead. Details: " + exception.getMessage());
        }
        if(cdl != null) cdl.countDown();
    }
}
