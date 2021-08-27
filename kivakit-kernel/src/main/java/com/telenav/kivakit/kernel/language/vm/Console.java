package com.telenav.kivakit.kernel.language.vm;

import com.telenav.kivakit.kernel.interfaces.io.Flushable;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.messaging.Message;

import java.io.PrintStream;

import static com.telenav.kivakit.kernel.language.vm.Console.OutputType.ERROR;
import static com.telenav.kivakit.kernel.language.vm.Console.OutputType.NORMAL;

public class Console implements Flushable
{
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
    public void flush(final Duration maximumWaitTime)
    {
        NORMAL.stream().flush();
        ERROR.stream().flush();
    }

    public void print(final String text, final Object... arguments)
    {
        print(NORMAL, text, arguments);
    }

    public void print(final OutputType output, final String text, final Object... arguments)
    {
        output.stream().print(Message.format(text, arguments));
    }

    public void printLine(final OutputType output, final String text, final Object... arguments)
    {
        output.stream().println(Message.format(text, arguments));
    }

    public void printLine(final String text, final Object... arguments)
    {
        printLine(NORMAL, text, arguments);
    }
}
