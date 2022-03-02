package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.core.messaging.messages.status.Problem;

public class ReflectionError extends Problem
{
    private final String error;

    public ReflectionError(String error)
    {
        this.error = error;
    }

    public ReflectionError(Exception cause, String message)
    {
        super(cause, message);
        this.error = message + "\n" + cause;
    }
}
