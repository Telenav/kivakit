package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * A problem object describing an issue with reflection
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
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
