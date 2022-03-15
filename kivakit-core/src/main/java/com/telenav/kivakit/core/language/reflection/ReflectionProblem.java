package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.core.messaging.messages.status.Problem;

public class ReflectionProblem extends Problem
{
    private final String error;

    public ReflectionProblem(String error)
    {
        this.error = error;
    }

    public ReflectionProblem(Exception cause, String message)
    {
        super(cause, message);
        this.error = message + "\n" + cause;
    }
}
