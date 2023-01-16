package com.telenav.kivakit.application;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Represents the success or failure of an application under UNIX, where an exit code of 0 represents success and any
 * non-zero value represents failure.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class ExitCode
{
    /** UNIX exit code of 0 represents success */
    public static ExitCode SUCCEEDED = new ExitCode(0);

    /** Generic failure exit code */
    public static ExitCode FAILED = new ExitCode(1);

    /** The UNIX exit code */
    private final int code;

    /**
     * Creates an {@link ExitCode} for the given code
     */
    public ExitCode(int code)
    {
        this.code = code;
    }

    /**
     * Returns this exit code
     */
    public int code()
    {
        return code;
    }
}
