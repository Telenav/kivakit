package com.telenav.kivakit.application;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Represents the success or failure of an application under UNIX, where an exit code of 0 represents success and any
 * non-zero value represents failure.
 *
 * @param code The UNIX exit code
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public record ExitCode(int code)
{
    /** UNIX exit code of 0 represents success */
    public static final ExitCode SUCCEEDED = new ExitCode(0);

    /** Generic failure exit code */
    public static final ExitCode FAILED = new ExitCode(1);

    /**
     * Creates an {@link ExitCode} for the given code
     */
    public ExitCode
    {
    }

    /**
     * Returns this exit code
     */
    @Override
    public int code()
    {
        return code;
    }
}
