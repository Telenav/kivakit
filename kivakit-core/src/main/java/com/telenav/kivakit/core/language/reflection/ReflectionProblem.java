package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A problem object describing an issue with reflection
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class ReflectionProblem extends Problem
{
    public ReflectionProblem(String message)
    {
        super(message);
    }

    public ReflectionProblem(Exception cause, String message)
    {
        super(cause, message);
    }
}
