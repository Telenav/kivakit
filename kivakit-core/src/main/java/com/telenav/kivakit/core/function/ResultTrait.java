package com.telenav.kivakit.core.function;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.interfaces.code.Code;

/**
 * Contains methods to facilitate working with {@link Result} monads.
 *
 * <hr>
 *
 * <p><b>Results</b></p>
 *
 * <ul>
 *     <li>{@link #absent()} - Returns a {@link Result} that represents a missing value</li>
 *     <li>{@link #success(Object)} - Returns a {@link Result} for the given value</li>
 *     <li>{@link #success(Maybe)} - Returns a {@link Result} for the given value</li>
 *     <li>{@link #run(Code)} - Runs the given code and returns a {@link Result}. If the code failed, the result it will contain one or more {@link Message}s</li>
 *     <li>{@link #failure(String, Object...)} - Creates a {@link Result} with the given failure message</li>
 *     <li>{@link #failure(Throwable, String, Object...)} - Creates a {@link Result} with the given failure message</li>
 *     <li>{@link #failure(Message)} - Creates a {@link Result} with the given failure message</li>
 * </ul>
 *
 * <hr>
 *
 * @author jonathanl (shibo)
 * @see Maybe
 * @see Result
 * @see Code
 */
public interface ResultTrait extends Repeater
{
    /**
     * Returns a {@link Result} with no value
     */
    default <T> Result<T> absent()
    {
        return Result.absent();
    }

    /**
     * Returns a {@link Result} with the given {@link Problem} message
     */
    default <T> Result<T> failure(String message, Object... arguments)
    {
        return listenTo(Result.failure(message, arguments));
    }

    /**
     * Returns a {@link Result} with the given message
     */
    default <T> Result<T> failure(Message message)
    {
        return listenTo(Result.failure(message));
    }

    /**
     * Returns a {@link Result} with the given {@link Problem} message
     */
    default <T> Result<T> failure(Throwable throwable, String message, Object... arguments)
    {
        return listenTo(Result.failure(throwable, message, arguments));
    }

    /**
     * Returns the result of executing the given {@link Code}. Captures any value, or any failure messages broadcast by
     * this object during the call, and returns a {@link Result}.
     *
     * @return The {@link Result} of the call
     */
    default <T> Result<T> run(Code<T> code)
    {
        return Result.run(this, code);
    }

    /**
     * Returns a {@link Result} for the given value
     */
    default <T> Result<T> success(Maybe<T> value)
    {
        return listenTo(Result.success(value));
    }

    /**
     * Returns a {@link Result} for the given value
     */
    default <T> Result<T> success(T value)
    {
        return listenTo(Result.success(value));
    }
}
