package com.telenav.kivakit.core.os;

import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.interfaces.io.Flushable;

import java.io.PrintStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.core.os.Console.OutputType.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputType.NORMAL;

public class Console implements Flushable<Duration>
{
    public static Console get()
    {
        return new Console();
    }

    public static void print(String text, Object... arguments)
    {
        print(NORMAL, text, arguments);
    }

    public static void print(OutputType output, String text, Object... arguments)
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

    public static void println(OutputType output, String text, Object... arguments)
    {
        output.stream().println(Strings.format(text, arguments));
    }

    public enum OutputType
    {
        NORMAL,
        ERROR;

        PrintStream stream()
        {
            return this == NORMAL ? System.out : System.err;
        }
    }

    @Override
    public void flush(Duration maximumWaitTime)
    {
        NORMAL.stream().flush();
        ERROR.stream().flush();
    }

    @Override
    public Duration maximumWaitTime()
    {
        return Duration.MAXIMUM;
    }
}
