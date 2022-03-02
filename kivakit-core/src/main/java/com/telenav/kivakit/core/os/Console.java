package com.telenav.kivakit.core.os;

import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.interfaces.io.Flushable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;

import java.io.PrintStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.core.os.Console.OutputType.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputType.NORMAL;

public class Console implements Flushable
{
    public static Console get()
    {
        return new Console();
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
    public void flush(LengthOfTime maximumWaitTime)
    {
        NORMAL.stream().flush();
        ERROR.stream().flush();
    }

    public void print(String text, Object... arguments)
    {
        print(NORMAL, text, arguments);
    }

    public void print(OutputType output, String text, Object... arguments)
    {
        output.stream().print(Formatter.format(text, arguments));
    }

    public void printLine(OutputType output, String text, Object... arguments)
    {
        output.stream().println(Formatter.format(text, arguments));
    }

    public void printLine(String text, Object... arguments)
    {
        printLine(NORMAL, text, arguments);
    }

    public PrintWriter printWriter()
    {
        return new PrintWriter(NORMAL.stream());
    }
}
