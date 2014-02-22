package com.optimaize.command4j.ext.extensions.exception.exceptiontranslation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used to combine multiple translators to one.
 *
 * @author Fabian Kessler
 */
public class CombinedExceptionTranslator implements ExceptionTranslator {

    @NotNull
    private final List<ExceptionTranslator> translators;

    /**
     * @param translators Pass them in in the order of priority. May be empty by definition.
     */
    public CombinedExceptionTranslator(@NotNull List<ExceptionTranslator> translators) {
        this.translators = new ArrayList<>(translators);
    }
    /**
     * @param translators Pass them in in the order of priority. May be empty by definition.
     */
    public CombinedExceptionTranslator(@NotNull ExceptionTranslator ... translators) {
        this.translators = new ArrayList<>(Arrays.asList(translators));
    }

    public boolean canTranslate(@NotNull Throwable t) {
        for (ExceptionTranslator translator : translators) {
            if (translator.canTranslate(t)) return true;
        }
        return false;
    }

    @NotNull @Override
    public Exception translate(@NotNull Throwable t) throws Exception {
        for (ExceptionTranslator translator : translators) {
            if (translator.canTranslate(t)) {
                throw translator.translate(t);
            }
        }
        throw new AssertionError("Must have called canTranslate() first!");
    }

}
