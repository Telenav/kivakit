package com.telenav.kivakit.application;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Represents the success or failure of an application
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public enum ExitCode
{
    /** UNIX exit code of 0 represents success */
    SUCCEEDED(0),

    /** Generic failure exit code */
    FAILED(1);

    private final int code;

    /**
     * Creates an {@link ExitCode} for the given code
     */
    ExitCode(int code)
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
