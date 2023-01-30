package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * A problem object describing an issue with reflection
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class ReflectionProblem extends Problem
{
    public ReflectionProblem(String message, Object... arguments)
    {
        super(message, arguments);
    }

    public ReflectionProblem(Exception cause, String message, Object... arguments)
    {
        super(cause, message, arguments);
    }
}
