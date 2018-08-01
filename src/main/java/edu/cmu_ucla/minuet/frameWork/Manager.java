package edu.cmu_ucla.minuet.frameWork;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Manager {
    public Manager() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        System.nanoTime();
    }

}
