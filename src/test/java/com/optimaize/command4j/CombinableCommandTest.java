package com.optimaize.command4j;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.optimaize.command4j.commands.*;
import org.testng.annotations.Test;
import java.util.Arrays;
import static org.testng.Assert.assertEquals;

/**
 * Tests the api for combining commands.
 *
 * @author Fabian Kessler
 */
public class CombinableCommandTest {

    private static final CommandExecutor commandExecutor = new CommandExecutorBuilder().build();
    private static final Mode mode = Mode.create();


    @Test
    public void andThen() throws Exception {
        BaseCommand<Long,Long> command = new Multiply(2L).andThen(new Multiply(3L));
        Long result = commandExecutor.execute(command, mode, 5L).get();
        assertEquals(result, (Long)30L);
    }

    @Test
    public void ifTrueOr() throws Exception {
        Predicate<Long> cond = new Predicate<Long>() {
            @Override public boolean apply(Long input) {
                return input > 10L;
            }
        };
        BaseCommand<Long,Long> command = new Multiply(2L).ifTrueOr(cond, new Multiply(3L));
        Long result = commandExecutor.execute(command, mode, 5L).get();
        assertEquals(result, (Long)15L); //the 2nd was executed

        command = new Multiply(4L).ifTrueOr(cond, new Multiply(5L));
        result = commandExecutor.execute(command, mode, 5L).get();
        assertEquals(result, (Long)20L); //the 1st one's result matches
    }

    @Test
    public void ifNotNullOr() throws Exception {
        Optional<String> result = commandExecutor.execute(new ReturnConstructorString("first").ifNotNullOr(new ReturnConstructorString("second")), mode, null);
        assertEquals(result.get(), "first");

        result = commandExecutor.execute(new ReturnConstructorString(null).ifNotNullOr(new ReturnConstructorString("second")), mode, null);
        assertEquals(result.get(), "second");

        result = commandExecutor.execute(new ReturnConstructorString(null).ifNotNullOr(new ReturnConstructorString(null)), mode, null);
        assertEquals(result.orNull(), null);
    }

    @Test
    public void and() throws Exception {
        BaseListCommand<Long,Long> command = new Multiply(2L).and(new Multiply(3L));
        Iterable<Long> result = commandExecutor.execute(command, mode, 5L).get();
        assertEquals(Iterables.get(result,0), (Long)10L);
        assertEquals(Iterables.get(result,1), (Long)15L);
    }

    @Test
    public void concat() throws Exception {
        BaseListCommand<Iterable<Long>, Long> command = new Multiply(2L).concat(new Multiply(3L));
        Iterable<Long> result = commandExecutor.execute(command, mode, Arrays.asList(5L,6L)).get();
        assertEquals(Iterables.get(result,0), (Long)10L);
        assertEquals(Iterables.get(result,1), (Long)18L);
    }
    /**
     * Tests what happens when the argument list is shorter than the number of commands.
     */
    @Test(expectedExceptions=ArrayIndexOutOfBoundsException.class)
    public void concat_outOfBounds_tooShort() throws Exception {
        BaseListCommand<Iterable<Long>, Long> command = new Multiply(2L).concat(new Multiply(3L));
        commandExecutor.execute(command, mode, Arrays.asList(5L)).get();
    }
    /**
     * Tests what happens when the argument list is longer than the number of commands.
     * As of now it goes through, doesn't throw, because the impl cannot detect this situation.
     * It is possible that a future implementation will detect it and throw.
     */
    @Test
    public void concat_outOfBounds_tooLong() throws Exception {
        BaseListCommand<Iterable<Long>, Long> command = new Multiply(2L).concat(new Multiply(3L));
        Iterable<Long> result = commandExecutor.execute(command, mode, Arrays.asList(5L,6L,7L)).get();
        assertEquals(Iterables.get(result,0), (Long)10L);
        assertEquals(Iterables.get(result,1), (Long)18L);
    }

}
