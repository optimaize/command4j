package com.optimaize.command4j.ext.extensions.exception;

import com.optimaize.command4j.Command;
import com.optimaize.command4j.commands.BaseCommand;
import com.optimaize.command4j.ext.extensions.exception.exceptiontranslation.ExceptionTranslationExtension;
import com.optimaize.command4j.ext.extensions.exception.exceptiontranslation.ExceptionTranslator;
import org.jetbrains.annotations.NotNull;

/**
 * A hub-class for easy access to built-in exception extensions.
 *
 * @author Fabian Kessler
 */
public class ExceptionExtensions {

    public static final ExceptionExtensions INSTANCE = new ExceptionExtensions();
    private ExceptionExtensions(){}


    /**
     * @see ExceptionTranslationExtension
     */
    public static <A, R> BaseCommand<A, R> withExceptionTranslation(@NotNull Command<A, R> cmd,
                                                                          @NotNull ExceptionTranslator exceptionTranslator) {
        return new ExceptionTranslationExtension.Interceptor<>(cmd, exceptionTranslator);
    }

}
