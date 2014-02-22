package com.optimaize.command4j.ext.extensions.timeout.configurabletimeout;

import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.CommandExecutorBuilder;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.commands.Sleep;
import com.optimaize.command4j.ext.extensions.timeout.TimeoutExtensions;
import com.optimaize.command4j.lang.Duration;
import org.testng.annotations.Test;

import java.util.concurrent.TimeoutException;

/**
 * @author Fabian Kessler
 */
public class TimeoutExtensionTest {

    /**
     * Runs a command that takes longer than the timeout extension allows.
     */
    @Test(expectedExceptions=TimeoutException.class)
    public void takesTooLong() throws Exception {
        CommandExecutor nakedExecutor = new CommandExecutorBuilder().build();
        BaseCommand<Void,Void> cmd = new Sleep(600);
        cmd = TimeoutExtensions.withTimeout(cmd, Duration.millis(500));
        nakedExecutor.execute(cmd, Mode.create(), null);
    }

    /**
     * Runs a command that finishes in time.
     */
    @Test
    public void isInTime() throws Exception {
        CommandExecutor nakedExecutor = new CommandExecutorBuilder().build();
        BaseCommand<Void,Void> cmd = new Sleep(100);
        cmd = TimeoutExtensions.withTimeout(cmd, Duration.millis(500));
        nakedExecutor.execute(cmd, Mode.create(), null);
    }

}
