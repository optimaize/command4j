package com.optimaize.command4j.impl;

import com.optimaize.command4j.Mode;
import com.optimaize.command4j.Command;
import com.optimaize.command4j.lang.Tuple2;
import org.jetbrains.annotations.NotNull;


/**
 * @author Eike Kettner
 */
public interface ExtensionHandler {

    @NotNull
    <A, R> Tuple2<Command<A, R>, Mode> applyModes(@NotNull Command<A, R> cmd, Mode mode);

}
