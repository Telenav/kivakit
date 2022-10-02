package com.telenav.kivakit.application;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Represents the success or failure of an application
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
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
