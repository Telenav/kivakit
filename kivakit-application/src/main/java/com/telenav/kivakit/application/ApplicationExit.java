package com.telenav.kivakit.application;

/**
 * Information about the exit of an application, including its {@link ExitCode} and any exception.
 *
 * @author jonathanl (shibo)
 */
public class ApplicationExit
{
    public static ApplicationExit SUCCESS = new ApplicationExit(ExitCode.SUCCEEDED);

    private final ExitCode code;

    private final Exception exception;

    ApplicationExit(ExitCode code, Exception exception)
    {
        this.code = code;
        this.exception = exception;
    }

    ApplicationExit(ExitCode code)
    {
        this(code, null);
    }

    public ExitCode code()
    {
        return code;
    }

    public Exception exception()
    {
        return exception;
    }
}
