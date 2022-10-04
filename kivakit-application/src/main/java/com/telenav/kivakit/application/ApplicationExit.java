package com.telenav.kivakit.application;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Information about the exit of an application, including its {@link ExitCode} and any exception.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class ApplicationExit
{
    /** Successful exit */
    public static ApplicationExit SUCCESS = new ApplicationExit(ExitCode.SUCCEEDED);

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
