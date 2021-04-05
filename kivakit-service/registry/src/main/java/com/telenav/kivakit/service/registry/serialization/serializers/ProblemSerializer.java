package com.telenav.kivakit.service.registry.serialization.serializers;

import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.serialization.json.PrimitiveGsonSerializer;

import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

/**
 * @author jonathanl (shibo)
 */
public class ProblemSerializer extends PrimitiveGsonSerializer<Problem, String>
{
    public ProblemSerializer()
    {
        super(String.class);
    }

    @Override
    protected Problem toObject(final String message)
    {
        return new Problem(message);
    }

    @Override
    protected String toPrimitive(final Problem problem)
    {
        return problem.formatted(WITH_EXCEPTION);
    }
}
