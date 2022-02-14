package com.telenav.kivakit.kernel.language.traits;

import com.telenav.kivakit.kernel.interfaces.code.Code;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.messages.Result;

/**
 * Contains methods for executing code and capturing messages transmitted by that code.
 *
 * @author jonathanl (shibo)
 */
public interface OperationTrait extends Repeater
{
    /**
     * Returns the result of executing the given {@link Code}. Captures any value or any failure messages that result
     * from the call and returns a {@link Result}.
     *
     * @return The {@link Result} of the call
     */
    default <T> Result<T> result(Code<T> code)
    {
        // Create an empty result that captures messages from this object,
        var result = new Result<T>(this);

        try
        {
            // call the code and store any result,
            result.set(code.run());
        }
        catch (Exception e)
        {
            // and if the code throws an exception, store that as a problem.
            result.problem(e, "Operation failed with exception");
        }

        // Return the result of the method call.
        return result;
    }
}
