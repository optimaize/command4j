package com.optimaize.command4j.ext.extensions.exception.exceptiontranslation;

import org.jetbrains.annotations.NotNull;

/**
 * Translates exceptions from one type to another.
 *
 * TODO this interface may as well be in some crema class (or guava or wherever).
 * At least in similar form, usable for all kinds of use cases. It's a very
 * non-domain-specific thing.
 *
 * @author Fabian Kessler
 */
public interface ExceptionTranslator {

    /**
     * Tells if this translator can handle the given Throwable.
     * Only then the framework calls {@link #translate}.
     */
    boolean canTranslate(@NotNull Throwable t);

    /**
     * Translates the throwable to another one, or keeps the same.
     * Note: the framework only calls this if {@link #canTranslate(Throwable)}.
     * @param t The Throwable to translate.
     * @return Never return, always throw.
     * @throws Exception The same or another Throwable where the input Throwable may optionally
     *         be added as a cause (usually a good practice).
     */
    @NotNull
    Exception translate(@NotNull Throwable t) throws Exception;

}
