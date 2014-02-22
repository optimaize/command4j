package com.optimaize.command4j.impl;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.optimaize.command4j.*;
import com.optimaize.command4j.cache.ExecutorCache;
import com.optimaize.command4j.lang.Immutable;
import com.optimaize.command4j.lang.Key;
import com.optimaize.command4j.lang.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eike Kettner
 * @author Fabian Kessler
 */
@Immutable
public class DefaultCommandExecutor implements CommandExecutor {

    private final static Key<Boolean> extensionsApplied = Key.booleanKey("ExtensionHandler.extensionsApplied");

    private final Logger logger;
    private final List<ModeExtension> extensions;

    /**
     * See rememberServiceForCleanup()
     */
    private final ConcurrentMap<DefaultCommandExecutorService, Object> services = new MapMaker().weakKeys().weakValues().makeMap();
    private final DefaultCommandExecutorService executorService;

    private final Runner runner;

    private final ExtensionHandler extensionHandler = new ExtensionHandler() {
        @Override @NotNull
        public <A, R> Tuple2<Command<A, R>, Mode> applyModes(@NotNull Command<A, R> cmd, Mode mode) {
            Mode m = mode;
            Command wrapped = cmd;
            if (!mode.is(extensionsApplied)) {
                for (ModeExtension me : extensions) { //immutable, thus no one can touch it while we iterate.
                    //noinspection unchecked
                    wrapped = me.extend(wrapped, mode);
                }
                m = mode.with(extensionsApplied);
            }
            //noinspection unchecked
            return (Tuple2<Command<A, R>, Mode>) new Tuple2(wrapped, m);
        }
    };

    public DefaultCommandExecutor(@NotNull Logger logger, @NotNull ExecutorCache cache, @NotNull List<ModeExtension> extensions) {
        this.logger = logger;
        this.extensions = ImmutableList.copyOf(extensions);
        this.runner = new Runner(this, cache);
        this.executorService = new DefaultCommandExecutorService(this.logger, runner, extensionHandler, Executors.newSingleThreadExecutor());
    }

    /**
     * TODO eike: What's the concept here? No one calls this as of now.
     * Should this method be added to the CommandExecutor interface so that the user
     * can actually call it?
     * Should we add a finalize() method to do it automatically? But the DefaultCommandExecutorService already
     * has such a finalizer.
     */
    public void destroy() {
        //TODO eike: i believe this should be called with a thread pool because each shutdown() can
        //take up to 10 seconds.
        //also, it would then be easier to just stick executorService into the services too.
        if (executorService!=null) {
            executorService.shutdown();
        }
        if (!services.isEmpty()) {
            for (DefaultCommandExecutorService s : services.keySet()) {
                if (s != null) {
                    s.shutdown();
                }
            }
        }
    }

    @NotNull @Override
    public <A, R> Optional<R> execute(@NotNull Command<A, R> cmd, @NotNull Mode mode, @Nullable A arg) throws Exception {
        Tuple2<Command<A, R>, Mode> wrapped = extensionHandler.applyModes(cmd, mode);
        return runner.run(wrapped._1, wrapped._2, Optional.fromNullable(arg));
    }

    @NotNull @Override
    public CommandExecutorService service(@NotNull final ExecutorService executorService) {
        DefaultCommandExecutorService service = new DefaultCommandExecutorService(logger, runner, extensionHandler, executorService);
        rememberServiceForCleanup(service);
        return service;
    }

    /**
     * @return The same default executor service on each call, in this impl it's a single thread executor service.
     */
    @NotNull @Override
    public CommandExecutorService service() {
        return executorService;
    }

    /**
     * Because the {@link CommandExecutorService} lacks a shutdown() method (and even if we would add one
     * we could not be sure that the userland code calls it), we must keep track of all of the created
     * command executor services in a weak map.
     * Comment from eike: (gc'ed pools are shutdown in the finalize() method) and call shutdown on destroy
     */
    private void rememberServiceForCleanup(@NotNull DefaultCommandExecutorService service) {
        services.put(service, 1); //the 1 has no meaning, it's just here to add a non-null value.
    }

}
