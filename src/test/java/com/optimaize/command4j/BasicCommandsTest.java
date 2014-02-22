package com.optimaize.command4j;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.optimaize.command4j.commands.*;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.*;

/**
 * Tests the commands without any extensions applied
 *
 * @author Fabian Kessler
 */
public class BasicCommandsTest {

    private static final CommandExecutor commandExecutor = new CommandExecutorBuilder().build();
    private static final Mode mode = Mode.create();

    @Test
    public void multiply() throws Exception {
        Optional<Long> result = commandExecutor.execute(new Multiply(2), mode, 5L);
        assertEquals(result.get(), (Long)10L);
    }

    @Test
    public void returnInputString() throws Exception {
        Optional<String> result = commandExecutor.execute(new ReturnInputString(), mode, "foo");
        assertEquals(result.get(), "foo");
    }

    @Test
    public void sleep() throws Exception {
        int sleepMs = 100;
        int delta = 2; //stopwatch timing can differ slightly...
        Stopwatch stopwatch = Stopwatch.createStarted();
        Optional<String> result = commandExecutor.execute(new Sleep(sleepMs).andThen(new ReturnConstructorString("foo")), mode, null);
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        assertEquals(result.get(), "foo");
        assertTrue(elapsed >= sleepMs-delta, "Elapsed was: "+elapsed);
    }

    @Test(expectedExceptions=UnsupportedOperationException.class)
    public void throwUnsupportedOperation() throws Exception {
        commandExecutor.execute(new ThrowUnsupportedOperation(), mode, null);
    }

}
