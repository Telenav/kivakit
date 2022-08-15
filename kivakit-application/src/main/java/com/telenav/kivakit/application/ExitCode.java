package com.telenav.kivakit.application;

/**
 * Represents the success or failure of an application
 *
 * @author jonathanl (shibo)
 */
public enum ExitCode
{
    SUCCEEDED(0),
    FAILED(1);

    private final int code;

    ExitCode(int code)
    {
        this.code = code;
    }

    public int code()
    {
        return code;
    }
}
