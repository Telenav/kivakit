package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.function.ResultTrait;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.comparison.Matcher;

import java.util.function.Predicate;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * Executes a block of code, handling exceptions and problem messages.
 *
 * <ul>
 *     <li>{@link #check(String, Code)} - Runs the given code, broadcasting the given problem message if it fails</li>
 *     <li>{@link #check(String, Matcher, Code)} - Runs the given code, broadcasting the given problem message if
 *     the code or the given predicate fail</li>
 *     <li>{@link #check(String, Runnable)} - Runs the given code, broadcasting the given problem message if it fails.
 *     returns a boolean {@link Result} indicating success or failure</li>
 *     <li>{@link #check(Runnable)} - Runs the given code, broadcasting a problem message if it fails.
 *     returns a boolean {@link Result} indicating success or failure</li>
 * </ul>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * Here is a (contrived) example that handles exceptions and failure messages automatically.
 * The check method executes the code block, which returns a {@link Result}. If the result
 * represents failure because {@link Result#failed()} is true, a failure {@link Result} is returned
 * containing a {@link Problem} message with the given text. If an optional post-condition {@link Matcher}
 * (which extends {@link Predicate}) fails to match (in this case the text returned by readText()),
 * the same thing occurs. If the result represents success, as determined by {@link Result#succeeded()},
 * the result is returned unchanged.
 * </p>
 *
 * <pre>
 * Result&lt;String&gt; read(File file)
 * {
 *     return check("Could not read string", s -> !s.isBlank(), () ->
 *     {
 *         return success(listenTo(file).reader().readText());
 *     });
 * }
 * </pre>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * Here, a method is called, returning a boolean result. If reading the file, or processing it broadcasts a problem,
 * the {@link Result} will be a failure containing the broadcast message(s).
 * </p>
 *
 * <pre>
 * Result&lt;Boolean&gt; process(File file)
 * {
 *     return check(() -> processText(listenTo(file).reader().readText()));
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public interface CheckTrait extends
        Repeater,
        ResultTrait
{
    /**
     * Executes the given code block, trapping any exceptions or failures
     *
     * @param code The code to execute, returning a {@link Result}
     * @param postCondition A predicate to test on the return value of the code
     * @param message The problem message to return if there's a failure
     * @return The result, signaling either one or more failures, or success with a value.
     */
    default <T> Result<T> check(String message, Matcher<T> postCondition, Code<Result<T>> code)
    {
        try
        {
            var result = code.run();
            if (result.failed())
            {
                return failure("Code failed: $", message);
            }
            if (!ok())
            {
                return failure("Code broadcast failure: $", message);
            }
            if (!postCondition.test(result.get()))
            {
                return failure("Result failed post-condition: $", message);
            }
            return result;
        }
        catch (Exception e)
        {
            return failure(e, "Code threw exception: $", message);
        }
    }

    /**
     * Executes the given code block, trapping any exceptions or failures
     *
     * @param code The code to execute, returning a {@link Result}
     * @param message The problem message to return if there's a failure
     * @return The result, signaling either one or more failures, or success with a value.
     */
    default <T> Result<T> check(String message, Code<Result<T>> code)
    {
        return check(message, ignored -> true, code);
    }

    /**
     * Executes the given code block, returning a boolean for success, if no messages are broadcast
     *
     * @param code The code to execute
     * @return The boolean result
     */
    default Result<Boolean> check(Runnable code)
    {
        return check(null, code);
    }

    /**
     * Executes the given code block, trapping any exceptions or failures
     *
     * @param code The code to execute, returning a {@link Result}
     * @param message The problem message to return if there's a failure
     */
    default Result<Boolean> check(String message, Runnable code)
    {
        var result = failure(message);
        try
        {
            result.listenTo(this);
            code.run();

            if (!ok())
            {
                var formatted = stringList("Code broadcast failure")
                        .appendingIfNotNull(message)
                        .join(": ");

                problem(formatted);
                return failure(formatted);
            }
            return success(true);
        }
        catch (Exception e)
        {
            var formatted = stringList("Code threw exception")
                    .appendingIfNotNull(message)
                    .join(": ");

            problem(e, formatted);
            return failure(e, formatted);
        }
        finally
        {
            removeListener(result);
        }
    }
}
