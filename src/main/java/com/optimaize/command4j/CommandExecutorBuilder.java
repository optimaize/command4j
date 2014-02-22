package com.optimaize.command4j;

import com.optimaize.command4j.cache.ExecutorCache;
import com.optimaize.command4j.impl.DefaultCommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder for a {@link CommandExecutor}.
 *
 * <p>This builder is not thread safe.</p>
 *
 * @author Fabian Kessler
 */
public class CommandExecutorBuilder {

    private static final Logger defaultLogger = LoggerFactory.getLogger(CommandExecutor.class); //yes, the class is CommandExecutor.class

    private Logger logger;
    private ExecutorCache cache;
    private List<ModeExtension> extensions;


    /**
     * If not set then <code>LoggerFactory.getLogger(CommandExecutor.class)</code> is used.
     */
    @NotNull
    public CommandExecutorBuilder logger(@NotNull Logger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * If not set then <code>new ExecutorCache()</code> is used.
     */
    @NotNull
    public CommandExecutorBuilder cache(@NotNull ExecutorCache cache) {
        this.cache = cache;
        return this;
    }

    /**
     * Adds an extension to the executor. This is used for intercepting commands executed by the executor
     * built by this builder.
     *
     * <p>The extension is added in the order of the call; the first added extension will be the first executed,
     * and so on.</p>
     *
     * It is perfectly valid to add the same extension more than once. Such situations occur when there are
     * multiple extensions in use. Example cases:
     * <pre>
     *  - logging
     *     - once inside, right around the actual command execution,
     *     - and once outside, before returning the result.
     *    If there was an auto-retry extension in between then we can see in the logs later that
     *    a command failed (the inner logger logged it), but in the end it succeeded (the outer
     *    logger records the success).
     *  - timeout (give up if it takes too long)
     *     - once inside, right around the actual command execution,
     *     - and once outside, before returning the result.
     *    If there was an auto-retry extension in between then we can use a per-execution timeout,
     *    and a total execution timeout. If this is a remove method invocation then we could
     *    switch the destination server in between and try again, but still enforce a total maximal
     *    permitted execution time.
     * </pre>
     *
     * <p>No method for removing extensions is provided. For two reasons.
     * <ol>
     *     <li>First, because this is a builder and you're just about assembling it, so your code
     *     logic should not need it. (If it was added, then adding extensions would also require the
     *     feature to add in a specific position and not just append at the end. And that's all way
     *     above the scope of this. We would then use a separate builder class just for making the
     *     list of extensions.)</li>
     *     <li>Second, it would be a difficult api. In case the same extension is added more than once,
     *     then it must be possible to specify which to remove.</li>
     * </ol>
     * </p>
     *
     * <p>If never called then no extensions are used.</p>
     */
    @NotNull
    public CommandExecutorBuilder withExtension(@NotNull ModeExtension extension) {
        if (extensions==null) {
            extensions = new ArrayList<>();
        }
        extensions.add(extension);
        return this;
    }

    /**
     * Constructs the object with the added extensions. Uses defaults for the {@link #logger logger}
     * and {@link #cache cache}.
     */
    @NotNull
    public CommandExecutor build() {
        return new DefaultCommandExecutor(
                logger!=null ? logger : defaultLogger,
                cache!=null ? cache : new ExecutorCache(),
                extensions==null ? Collections.<ModeExtension>emptyList() : extensions
        );
    }

}
