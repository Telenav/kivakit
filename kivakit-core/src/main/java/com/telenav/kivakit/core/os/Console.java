package com.telenav.kivakit.core.os;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.interfaces.io.Flushable;

import java.io.PrintStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.os.Console.OutputType.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputType.NORMAL;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * Simple console access.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #console()}</li>
 * </ul>
 *
 * <p><b>Printing</b></p>
 *
 * <ul>
 *     <li>{@link #print(String, Object...)}</li>
 *     <li>{@link #println(String, Object...)}</li>
 *     <li>{@link #print(OutputType, String, Object...)}</li>
 *     <li>{@link #println(OutputType, String, Object...)}</li>
 *     <li>{@link #printWriter()}</li>
 *     <li>{@link #flush(Duration)}</li>
 *     <li>{@link #flush()}</li>
 *     <li>{@link #maximumFlushTime()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("resource")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class Console implements
        Flushable<Duration>,
        Listener
{
    /**
     * Returns an instance of {@link Console}
     */
    public static Console console()
    {
        return new Console();
    }

    /**
     * The output stream type
     */
    public enum OutputType
    {
        /** stdout */
        NORMAL,

        /** stderr */
        ERROR;

        PrintStream stream()
        {
            return this == NORMAL ? System.out : System.err;
        }
    }

    private Console()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush(Duration maximumWaitTime)
    {
        NORMAL.stream().flush();
        ERROR.stream().flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration maximumFlushTime()
    {
        return Duration.FOREVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message message)
    {
        switch (message.operationStatus())
        {
            case FAILED, HALTED ->
            {
                print(ERROR, message.asString());
                return;
            }
        }

        switch (message.status())
        {
            case FAILED, PROBLEM ->
            {
                print(ERROR, message.asString());
                return;
            }
        }

        print(NORMAL, message.asString());
    }

    public void print(OutputType output, String text, Object... arguments)
    {
        output.stream().print(format(text, arguments));
    }

    public void print(String text, Object... arguments)
    {
        print(NORMAL, text, arguments);
    }

    public PrintWriter printWriter()
    {
        return new PrintWriter(NORMAL.stream());
    }

    public void println(OutputType output, String text, Object... arguments)
    {
        output.stream().println(format(text, arguments));
    }

    public void println(String text, Object... arguments)
    {
        println(NORMAL, text, arguments);
    }

    /**
     * Returns the width of this console
     *
     * @return The console width
     */
    public Count width()
    {
        return count(150);
    }
}
