package com.telenav.kivakit.core.os;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.interfaces.io.Flushable;

import java.io.PrintStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.os.Console.OutputStream.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputStream.NORMAL;

/**
 * Simple console access
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("resource")
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class Console implements
        Flushable<Duration>,
        Listener
{
    private static final Console console = new Console();

    public static Console console()
    {
        return console;
    }

    public static void print(String text, Object... arguments)
    {
        print(NORMAL, text, arguments);
    }

    public static void print(OutputStream output, String text, Object... arguments)
    {
        output.stream().print(Strings.format(text, arguments));
    }

    public static PrintWriter printWriter()
    {
        return new PrintWriter(NORMAL.stream());
    }

    public static void println(String text, Object... arguments)
    {
        println(NORMAL, text, arguments);
    }

    public static void println(OutputStream output, String text, Object... arguments)
    {
        output.stream().println(Strings.format(text, arguments));
    }

    /**
     * The output stream type
     */
    public enum OutputStream
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
        return Duration.MAXIMUM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message message)
    {
        switch (message.operationStatus())
        {
            case FAILED:
            case HALTED:
                print(ERROR, message.asString());
                return;
        }

        switch (message.status())
        {
            case FAILED:
            case PROBLEM:
                print(ERROR, message.asString());
                return;
        }

        print(NORMAL, message.asString());
    }
}
