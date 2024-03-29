package com.telenav.kivakit.application;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.application.ExitCode.SUCCEEDED;

/**
 * Information about the exit of an application, including its {@link ExitCode} and any exception.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ApplicationExit
{
    /** Successful exit */
    public static final ApplicationExit EXIT_SUCCESS = new ApplicationExit(SUCCEEDED);

    /** This application's exit code */
    private final ExitCode code;

    /** This application's exception */
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

    /**
     * Returns the application's exit code
     */
    public ExitCode code()
    {
        return code;
    }

    /**
     * Returns any exception related to application termination
     */
    public Exception exception()
    {
        return exception;
    }
}
