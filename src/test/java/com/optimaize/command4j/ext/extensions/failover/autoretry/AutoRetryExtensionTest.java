package com.optimaize.command4j.ext.extensions.failover.autoretry;

import com.google.common.base.Optional;
import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.CommandExecutorBuilder;
import com.optimaize.command4j.ExecutionContext;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.commands.CallCounter;
import com.optimaize.command4j.ext.extensions.failover.FailoverExtensions;
import com.optimaize.command4j.lang.Duration;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author Fabian Kessler
 */
public class AutoRetryExtensionTest {

    /**
     * Runs a command that fails the first time, has auto-retry extension on, and works the 2nd time.
     */
    @Test
    public void worksOnSecondCall_wrappedCommand() throws Exception {
        CommandExecutor nakedExecutor = new CommandExecutorBuilder().build();

        //keeps track of how often the first command was called.
        final CallCounter.Counter counter = new CallCounter.Counter();

        BaseCommand<Void, Integer> cmd = makeCommandWhereFirstCallFails(counter);

        cmd = FailoverExtensions.withAutoRetry(cmd, AutoRetryStrategies.alwaysOnce());

        Optional<Integer> result = nakedExecutor.execute(cmd, Mode.create(), null);
        assertEquals(result.get(), (Integer)2);
        assertEquals(counter.get(), 2);
    }

    /**
     * Same as worksOnSecondCall() but having the retry in the executor, not in the command.
     */
    @Test
    public void worksOnSecondCall_retryExecutor() throws Exception {
        CommandExecutor retryExecutor = new CommandExecutorBuilder()
                .withExtension(new AutoRetryExtension())
        .build();
        Mode mode = Mode.create().with(AutoRetryExtension.STRATEGY, AutoRetryStrategies.alwaysOnce());

        //keeps track of how often the first command was called.
        final CallCounter.Counter counter = new CallCounter.Counter();

        BaseCommand<Void, Integer> cmd = makeCommandWhereFirstCallFails(counter);

        cmd = FailoverExtensions.withAutoRetry(cmd, new AutoRetryStrategy() {
            @Override
            public Duration doRetry(int executionCounter, @NotNull Exception exception) {
                if (executionCounter!=1) {
                    return null;
                }
                return Duration.millis(0); //always retry
            }
        });

        Optional<Integer> result = retryExecutor.execute(cmd, mode, null);
        assertEquals(result.get(), (Integer)2);
        assertEquals(counter.get(), 2);
    }

    private BaseCommand<Void, Integer> makeCommandWhereFirstCallFails(final CallCounter.Counter counter) {
        BaseCommand<Void,Integer> cmd = new CallCounter(counter);
        cmd = cmd.andThen(new BaseCommand<Integer, Integer>() {
            @Override
            public Integer call(@NotNull Optional<Integer> arg, @NotNull ExecutionContext ec) throws Exception {
                if (counter.get() == 1) {
                    throw new RuntimeException("Nah!");
                }
                return counter.get();
            }
        });
        return cmd;
    }


}
