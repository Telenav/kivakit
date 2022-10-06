package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * A problem object describing an issue with reflection
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE,
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
