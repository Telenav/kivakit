package com.telenav.kivakit.core.messaging;

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
import com.telenav.kivakit.core.time.Frequency;

/**
 * Methods that transmit different kinds of messages. This interface extends {@link DebugTransceiver}
 * because some messages are only sent when {@link #isDebugOn()} returns true.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public interface MessageTransceiver extends DebugTransceiver
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
     * @param maximumFrequency The maximum frequency at which this message should appear
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Glitch glitch(Frequency maximumFrequency, String text, Object... arguments)
    {
        return (Glitch) transmit(new Glitch(text, arguments).maximumFrequency(maximumFrequency));
    }

    /**
     * Sends a formatted {@link Glitch} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Glitch glitch(Frequency maximumFrequency, Throwable cause, String text,
                          Object... arguments)
    {
        return (Glitch) transmit(new Glitch(cause, text, arguments).maximumFrequency(maximumFrequency));
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
     * Throws an {@link IllegalArgumentException} with the given formatted message
     *
     * @param text The message to format
     * @param arguments The arguments
     * @param <T> The type of message
     * @return Return value is null because the compiler cannot determine that an exception is thrown
     */
    default <T> T illegalArgument(String text, Object... arguments)
    {
        var problem = new Problem(text, arguments);
        transmit(problem);
        problem.throwAsIllegalArgumentException();
        return null;
    }

    /**
     * Throws an {@link IllegalStateException} with the given formatted message
     *
     * @param <T> The type of message
     * @return Return value is null because the compiler cannot determine that an exception is thrown
     */
    default <T> T illegalState(String message, Object... arguments)
    {
        var problem = new Problem(message, arguments);
        transmit(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    /**
     * Throws an {@link IllegalStateException} with the given exception and formatted message
     *
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return Return value is null because the compiler cannot determine that an exception is thrown
     */
    default <T> T illegalState(Throwable cause, String text, Object... arguments)
    {
        var problem = new Problem(cause, text, arguments);
        transmit(problem);
        problem.throwAsIllegalStateException();
        return null;
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
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Problem problem(Frequency maximumFrequency, String text, Object... arguments)
    {
        return (Problem) transmit(new Problem(text, arguments).maximumFrequency(maximumFrequency));
    }

    /**
     * Sends a formatted {@link Problem} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Problem problem(Frequency maximumFrequency, Throwable cause, String text,
                            Object... arguments)
    {
        return (Problem) transmit(new Problem(cause, text, arguments).maximumFrequency(maximumFrequency));
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
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Quibble quibble(Frequency maximumFrequency, String text, Object... arguments)
    {
        return (Quibble) transmit(new Quibble(text, arguments).maximumFrequency(maximumFrequency));
    }

    /**
     * Sends a formatted {@link Quibble} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Quibble quibble(Frequency maximumFrequency, Throwable cause, String text,
                            Object... arguments)
    {
        return (Quibble) transmit(new Quibble(cause, text, arguments).maximumFrequency(maximumFrequency));
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
     * Sends a formatted {@link Trace} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Trace trace(Frequency maximumFrequency, String text, Object... arguments)
    {
        if (isDebugOn())
        {
            return (Trace) transmit(new Trace(text, arguments).maximumFrequency(maximumFrequency));
        }
        return null;
    }

    /**
     * Sends a formatted {@link Trace} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Trace trace(Frequency maximumFrequency, Throwable cause, String text,
                        Object... arguments)
    {
        if (isDebugOn())
        {
            return (Trace) transmit(new Trace(cause, text, arguments).maximumFrequency(maximumFrequency));
        }
        return null;
    }

    /**
     * Sends a formatted {@link Warning} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Warning warning(Frequency maximumFrequency, String text, Object... arguments)
    {
        return (Warning) transmit(new Warning(text, arguments).maximumFrequency(maximumFrequency));
    }

    /**
     * Sends a formatted {@link Warning} message to this {@link Transceiver}
     *
     * @param maximumFrequency The maximum frequency at which this message should be logged
     * @param cause The cause of the fatal problem
     * @param text The message to format
     * @param arguments The arguments
     * @return The message
     */
    default Warning warning(Frequency maximumFrequency, Throwable cause, String text,
                            Object... arguments)
    {
        return (Warning) transmit(new Warning(cause, text, arguments).maximumFrequency(maximumFrequency));
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
}
