package com.optimaize.command4j.ext.extensions.loadlimit.maxconcurrent;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ListenableFuture;
import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.CommandExecutorBuilder;
import com.optimaize.command4j.CommandExecutorService;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.commands.Sleep;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

/**
 * @author Fabian Kessler
 */
public class MaxConcurrentRunningExtensionTest {

    /**
     * This must work: even though we run 10, and only 2 are allowed at once, only one is running at any given
     * time because we run with just 1 thread.
     */
    @Test
    public void runMaxTwoWithOneThread() throws Exception {
        int maxAtATime = 2;
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 1;
        int allowedMargin = 100;
        Mode mode = Mode.create().with(MaxConcurrentRunningExtension.STRATEGY, MaxConcurrentStateImpl.withThrowing(maxAtATime));
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin, mode);
    }

    /**
     * This must work: even though we run 10, and only 2 are allowed at once, only two are running at any given
     * time because we run with just 2 threads.
     */
    @Test
    public void runMaxTwoWithTwoThreads() throws Exception {
        int maxAtATime = 2;
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 2;
        int allowedMargin = 100;
        Mode mode = Mode.create().with(MaxConcurrentRunningExtension.STRATEGY, MaxConcurrentStateImpl.withThrowing(maxAtATime));
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin, mode);
    }

    /**
     * This must fail, 4 threads use up the 2 concurrent and more.
     */
    @Test(expectedExceptions = ExecutionException.class)
    public void runMaxTwoWithFourThreads() throws Exception {
        int maxAtATime = 2;
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 4;
        int allowedMargin = 100;
        Mode mode = Mode.create().with(MaxConcurrentRunningExtension.STRATEGY, MaxConcurrentStateImpl.withThrowing(maxAtATime));
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin, mode);
    }

    /**
     * This must work: even though we have too many worker threads, we do some waiting.
     */
    @Test
    public void runMaxTwoWithFourThreadsWithWaiting() throws Exception {
        int maxAtATime = 2;
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 4;
        int allowedMargin = 500; //the whole time should be 500ms
        Mode mode = Mode.create().with(MaxConcurrentRunningExtension.STRATEGY, MaxConcurrentStateImpl.withWaiting(maxAtATime, 1000));
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin, mode);
    }

    /**
     * This must fail: even though we do some waiting, it's not enough.
     */
    @Test(expectedExceptions = ExecutionException.class)
    public void runMaxTwoWithFourThreadsWithWaitingInsufficiently() throws Exception {
        int maxAtATime = 2;
        int commandSleepMillis = 100;
        int numExecutions = 10;
        int numThreads = 4;
        int allowedMargin = 99999; //irrelevant
        Mode mode = Mode.create().with(MaxConcurrentRunningExtension.STRATEGY, MaxConcurrentStateImpl.withWaiting(maxAtATime, 100));
        run(commandSleepMillis, numExecutions, numThreads, allowedMargin, mode);
    }


    private void run(int commandSleepMillis, int numExecutions, int numThreads, int allowedMargin, Mode mode) throws InterruptedException, ExecutionException {
        int delta = 2; //stopwatch timing can differ slightly...

        CommandExecutor commandExecutor = new CommandExecutorBuilder()
                .withExtension(new MaxConcurrentRunningExtension())
                .build();
        ExecutorService javaExecutor = Executors.newFixedThreadPool(numThreads);
        CommandExecutorService executorService = commandExecutor.service(javaExecutor);
        BaseCommand<Void,Void> cmd = new Sleep(commandSleepMillis);

        List<ListenableFuture<Optional<Void>>> futures = new ArrayList<>(numExecutions);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i=0; i<numExecutions; i++) {
            futures.add( executorService.submit(cmd, mode, null) );
        }
        javaExecutor.shutdown();
        javaExecutor.awaitTermination(10, TimeUnit.SECONDS);

        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        checkSuccess(futures);
        assertTrue(elapsed >= (numExecutions*commandSleepMillis)/numThreads -delta, "elapsed was: "+elapsed);
        assertTrue(elapsed <= ((numExecutions*commandSleepMillis)/numThreads + allowedMargin), "elapsed was: "+elapsed);
    }

    private void checkSuccess(List<ListenableFuture<Optional<Void>>> futures) throws ExecutionException, InterruptedException {
        for (ListenableFuture<Optional<Void>> future : futures) {
            assertTrue(future.isDone());
            future.get();
        }
    }

}
