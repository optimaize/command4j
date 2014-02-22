package com.optimaize.command4j.ext.extensions.timeout.configurabletimeout;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Eike Kettner
 */
final class Util {

    /**
     * Creates an executor service with one thread that is removed automatically after 10 second idle time.
     * The pool is therefore garbage collected and shutdown automatically.
     *
     * <p>This executor service can be used for situations, where a task should be executed immediately
     * in a separate thread and the pool is not kept around.</p>
     *
     * impl notes: eike couldn't find a good place to put this class. thus for now it lives just here.
     *             maybe in the future the same code will be used in other places too ... then it may be moved.
     */
    @NotNull
    static ExecutorService newExecutor() {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(0, 1, 10, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        tpe.allowCoreThreadTimeOut(true);
        return tpe;
    }
}
