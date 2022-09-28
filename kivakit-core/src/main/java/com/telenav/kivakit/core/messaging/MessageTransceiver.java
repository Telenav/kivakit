package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.core.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.messaging.messages.status.FatalProblem;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Narration;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Methods that transmit different kinds of messages.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public interface MessageTransceiver extends Transceiver
{
    /**
     * Sends a formatted {@link Announcement} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Announcement announce(String text, Object... arguments)
    {
        return transmit(new Announcement(text, arguments));
    }

    /**
     * @return Debug object for this
     */
    default Debug debug()
    {
        return Debug.of(debugClassContext(), this);
    }

    /**
     * @return The class where this transceiver is
     */
    @UmlExcludeMember
    default Class<?> debugClassContext()
    {
        return getClass();
    }

    /**
     * <b>Not public API</b>
     *
     * @return The context of this broadcaster in code
     */
    @UmlExcludeMember
    default CodeContext debugCodeContext()
    {
        return new CodeContext(debugClassContext());
    }

    /**
     * <b>Not public API</b>
     *
     * @param context The context in code
     */
    @UmlExcludeMember
    default void debugCodeContext(CodeContext context)
    {
    }

    /**
     * Throws a formatted {@link FatalProblem} message as an {@link IllegalStateException}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default <T> T fatal(String text, Object... arguments)
    {
        var problem = new FatalProblem(text, arguments);
        transmit(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    /**
     * Throws a formatted {@link FatalProblem} message as an {@link IllegalStateException}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @param <T> The type of message
     * @return The message
     */
    default <T> T fatal(Throwable cause, String text, Object... arguments)
    {
        var problem = new FatalProblem(cause, text, arguments);
        transmit(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    /**
     * Sends a formatted {@link Glitch} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Glitch glitch(String text, Object... arguments)
    {
        return transmit(new Glitch(text, arguments));
    }

    /**
     * Sends a formatted {@link Glitch} message to this {@link Transceiver}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Glitch glitch(Throwable cause, String text, Object... arguments)
    {
        if (isDebugOn())
        {
            return transmit(new Glitch(cause, text, arguments));
        }
        return null;
    }

    /**
     * Sends a formatted {@link OperationHalted} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default OperationHalted halted(String text, Object... arguments)
    {
        return transmit(new OperationHalted(text, arguments));
    }

    /**
     * Sends a formatted {@link OperationHalted} message to this {@link Transceiver}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default OperationHalted halted(Throwable cause, String text, Object... arguments)
    {
        return transmit(new OperationHalted(cause, text, arguments));
    }

    /**
     * Runs the given code if debug is turned on for this {@link Transceiver}
     *
     * @param code The code to run if debug is off
     */
    default void ifDebug(Runnable code)
    {
        if (isDebugOn())
        {
            code.run();
        }
    }

    /**
     * Sends a formatted {@link Information} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Information information(String text, Object... arguments)
    {
        return transmit(new Information(text, arguments));
    }

    /**
     * @return True if debugging is on for this {@link Transceiver}
     */
    default boolean isDebugOn()
    {
        return debug().isDebugOn();
    }

    /**
     * Sends a formatted {@link Narration} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Narration narrate(String text, Object... arguments)
    {
        return transmit(new Narration(text, arguments));
    }

    /**
     * Sends a formatted {@link Problem} message to this {@link Transceiver}
     *
     * @return The message
     */
    default Problem problem(String text, Object... arguments)
    {
        return transmit(new Problem(text, arguments));
    }

    /**
     * Sends a formatted {@link Problem} message to this {@link Transceiver}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Problem problem(Throwable cause, String text, Object... arguments)
    {
        return transmit(new Problem(cause, text, arguments));
    }

    /**
     * Broadcasts a problem if the given value is null
     *
     * @param value The value to check
     * @param text The message to format
     * @param arguments The arguments
     * @return The value
     */
    default <T> T problemIfNull(T value, String text, Object... arguments)
    {
        if (value == null)
        {
            problem(text, arguments);
        }
        return value;
    }

    /**
     * Sends a formatted {@link Quibble} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Quibble quibble(String text, Object... arguments)
    {
        return transmit(new Quibble(text, arguments));
    }

    /**
     * Sends a formatted {@link Quibble} message to this {@link Transceiver}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Quibble quibble(Throwable cause, String text, Object... arguments)
    {
        if (isDebugOn())
        {
            return transmit(new Quibble(cause, text, arguments));
        }
        return null;
    }

    /**
     * Broadcasts a quibble if the given value is null
     *
     * @param value The value to check
     * @param text The message to format
     * @param arguments The arguments
     * @return The value
     */
    default <T> T quibbleIfNull(T value, String text, Object... arguments)
    {
        if (value == null)
        {
            quibble(text, arguments);
        }
        return value;
    }

    /**
     * Sends a formatted {@link Trace} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Trace trace(String text, Object... arguments)
    {
        if (isDebugOn())
        {
            return transmit(new Trace(text, arguments));
        }
        return null;
    }

    /**
     * Sends a formatted {@link Trace} message to this {@link Transceiver}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Trace trace(Throwable cause, String text, Object... arguments)
    {
        if (isDebugOn())
        {
            return transmit(new Trace(cause, text, arguments));
        }
        return null;
    }

    /**
     * Sends a formatted {@link Warning} message to this {@link Transceiver}
     *
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Warning warning(String text, Object... arguments)
    {
        return transmit(new Warning(text, arguments));
    }

    /**
     * Sends a formatted {@link Warning} message to this {@link Transceiver}
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Warning warning(Throwable cause, String text, Object... arguments)
    {
        return transmit(new Warning(cause, text, arguments));
    }

    /**
     * Broadcasts a warning if the given value is null
     *
     * @param value The value to check
     * @param text The message to format
     * @param arguments The arguments
     * @return The value
     */
    default <T> T warningIfNull(T value, String text, Object... arguments)
    {
        if (value == null)
        {
            warning(text, arguments);
        }
        return value;
    }
}
