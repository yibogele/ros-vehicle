package com.venusiot.vehicle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 11:30 2018/8/23
 * Modified By:
 */
public class PublishThreadManager {
    private static Logger logger = LoggerFactory.getLogger(PublishThreadManager.class);


    private PublishThreadManager() {
    }

//    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        executor.submit(runnable);
    }

    public static void shutdown() {
        try {
            logger.info("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                logger.error("cancel non-finished tasks");
            }
            executor.shutdownNow();
            logger.info("shutdown finished");
        }
    }
}
