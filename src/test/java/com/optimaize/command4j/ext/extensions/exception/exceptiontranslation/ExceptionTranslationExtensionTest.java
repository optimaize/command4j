package com.optimaize.command4j.ext.extensions.exception.exceptiontranslation;

import com.optimaize.command4j.CommandExecutor;
import com.optimaize.command4j.CommandExecutorBuilder;
import com.optimaize.command4j.Mode;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.commands.ThrowUnsupportedOperation;
import com.optimaize.command4j.ext.extensions.exception.ExceptionExtensions;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Test;

/**
 * @author Fabian Kessler
 */
public class ExceptionTranslationExtensionTest {

    private static class MyException extends Exception {
        private MyException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final ExceptionTranslator myExceptionTranslator = new ExceptionTranslator() {
        @Override
        public boolean canTranslate(@NotNull Throwable t) {
            return t instanceof UnsupportedOperationException;
        }
        @NotNull @Override
        public Exception translate(@NotNull Throwable t) throws Exception {
            throw new MyException("Translated", t);
        }
    };


    @Test(expectedExceptions=UnsupportedOperationException.class)
    public void withoutTranslation() throws Exception {
        CommandExecutor nakedExecutor = new CommandExecutorBuilder().build();
        BaseCommand<Void,Void> cmd = new ThrowUnsupportedOperation();
        nakedExecutor.execute(cmd, Mode.create(), null);
    }

    /**
     * Runs a command that throws UnsupportedOperationException but because the COMMAND is wrapped
     * with the exception translator it comes out as a MyException.
     */
    @Test(expectedExceptions=MyException.class)
    public void commandWrapped() throws Exception {
        CommandExecutor nakedExecutor = new CommandExecutorBuilder().build();
        BaseCommand<Void,Void> cmd = new ThrowUnsupportedOperation();
        cmd = ExceptionExtensions.withExceptionTranslation(cmd, myExceptionTranslator);
        nakedExecutor.execute(cmd, Mode.create(), null);
    }

    /**
     * Runs a command that throws UnsupportedOperationException but because the EXECUTOR is wrapped
     * with the exception translator it comes out as a MyException.
     */
    @Test(expectedExceptions=MyException.class)
    public void executorWrapped() throws Exception {
        CommandExecutor exceptionTranslationExecutor = new CommandExecutorBuilder()
            .withExtension(new ExceptionTranslationExtension())
        .build();
        Mode mode = Mode.create().with(ExceptionTranslationExtension.TRANSLATOR, myExceptionTranslator);
        BaseCommand<Void,Void> cmd = new ThrowUnsupportedOperation();
        exceptionTranslationExecutor.execute(cmd, mode, null);
    }

}
