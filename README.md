# Command4j

### ABOUT
Command4j is a general-purpose command framework for Java (>= 7).
It lets you program custom interceptors (wrappers, filters), and handle cross-cutting concerns.

### USE CASE EXAMPLES

A good example is calling a remote web service. By putting the call itself into a command,
command4j offers a couple of built-in extensions to deal with situations such as:

* logging
* failover/retry
* exception translation
* enforce timeout


### BASE CONCEPTS

##### Command

The command is the code to be executed. It can get an optional argument A and
can return a result R. (If more than one argument must be passed to the command,
an aggregator argument containing the others must be used.)

Commands can be chained: Execute command 1, take the result, feed it to command 2, return (and such).

##### Mode

The mode contains definitions that apply to a whole execution of a command, and is usually
used for executing many commands.

##### CommandExecutor

The executor can execute a command directly. And it can create a CommandExecutorService, which
can run commands concurrently.

##### ModeExtension

Such extensions allow it to wrap the Command. This allows for executing code before, after or
around each command. An example is a logging extension that informs before and after each execution.



### CODE EXAMPLES

##### Command with Timeout

    //a command that sleeps 100ms, then returns null.
    Command<Void, Void> command = new Command<Void, Void>() {
        @Nullable @Override
        public Void call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
            Thread.sleep(100);
            return null;
        }
    };
    //wrap the command with the timeout extension and allow max 500ms:
    command = TimeoutExtensions.withTimeout(command, Duration.millis(500));
    CommandExecutor executor = new CommandExecutorBuilder().build();
    executor.execute(cmd, Mode.create(), null); //works, 100 is less than 500


##### Exception Translation

    //a command that throws
    Command<Void, Void> command = new Command<Void, Void>() {
        @Nullable @Override
        public Void call(@NotNull Optional<Void> arg, @NotNull ExecutionContext ec) throws Exception {
            throw new UnsupportedOperationException("Nah, can't do!");
        }
    };
    //some exception translator impl
    ExceptionTranslator myExceptionTranslator = new ExceptionTranslator() {
        @Override
        public boolean canTranslate(@NotNull Throwable t) {
            return t instanceof UnsupportedOperationException;
        }
        @NotNull @Override
        public Exception translate(@NotNull Throwable t) throws Exception {
            throw new MyException("Translated", t);
        }
    };
    //creating an executor that features translation:
    CommandExecutor exceptionTranslationExecutor = new CommandExecutorBuilder()
        .withExtension(new ExceptionTranslationExtension())
    .build();
    //enabling my translator in the mode:
    Mode mode = Mode.create().with(ExceptionTranslationExtension.TRANSLATOR, myExceptionTranslator);
    exceptionTranslationExecutor.command(cmd, mode, null);


##### Using an Executor Service

    CommandExecutor commandExecutor = new CommandExecutorBuilder()
            //put in my extensions
            //.withExtension(...)
            .build();
    ExecutorService javaExecutor = Executors.newFixedThreadPool(numThreads);
    CommandExecutorService executorService = commandExecutor.service(javaExecutor);
    Command<Void,Void> command = new Sleep(100);
    for (int i=0; i<999; i++) {
        ListenableFuture<Optional<Void>> submit = executorService.submit(cmd, mode, null);
    }


> For these and more examples see the unit tests.



### DEPENDENCIES

##### Guava (usually the latest version)
For the `Optional` class, and the `ListeningExecutorService`, and probably some more.

##### org.slf4j slf4j-api
For the `Logger` interface.

##### com.intellij annotations
For the `@Nullable` and `@NotNull` annotations


### STATE

Command4j has been in development and use for long time internally at Optimaize, and is now published on GitHub.
The interfaces and classes are well documented, and there is a fair amount of unit tests.


### LICENSE

MIT License


### AUTHORS

Command4j was prototyped and written in large parts by Eike Kettner.
