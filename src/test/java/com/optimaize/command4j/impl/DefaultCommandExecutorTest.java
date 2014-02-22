package com.optimaize.command4j.impl;

import com.google.common.base.Stopwatch;
import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.CommandExecutorBuilder;
import com.optimaize.command4j.CommandExecutorService;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.commands.Sleep;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

/**
 * @author Fabian Kessler
 */
public class DefaultCommandExecutorTest {

    @Test
    public void runSingleThreaded() throws Exception {
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 1;
        int allowedMargin = 100;
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin);
    }

    @Test
    public void runMultiThreaded() throws Exception {
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 2;
        int allowedMargin = 100;
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin);
    }

    private void run(int commandSleepMillis, int numExecutions, int numThreads, int allowedMargin) throws InterruptedException {
        int delta = 2; //stopwatch timing can differ slightly...
        CommandExecutor commandExecutor = new CommandExecutorBuilder().build();
        ExecutorService javaExecutor = Executors.newFixedThreadPool(numThreads);
        CommandExecutorService executorService = commandExecutor.service(javaExecutor);
        Mode mode = Mode.create();
        BaseCommand<Void,Void> cmd = new Sleep(commandSleepMillis);

        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i=0; i<numExecutions; i++) {
            executorService.submit(cmd, mode, null);
        }
        javaExecutor.shutdown();
        javaExecutor.awaitTermination(10, TimeUnit.SECONDS);

        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        assertTrue(elapsed >= (numExecutions*commandSleepMillis)/numThreads -delta, "elapsed was: "+elapsed);
        assertTrue(elapsed <= ((numExecutions*commandSleepMillis)/numThreads + allowedMargin), "elapsed was: "+elapsed);
    }

}
