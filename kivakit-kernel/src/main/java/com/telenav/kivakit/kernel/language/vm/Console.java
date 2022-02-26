package com.telenav.kivakit.kernel.language.vm;

import com.telenav.kivakit.interfaces.io.Flushable;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.kernel.messaging.Message;

import java.io.PrintStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.kernel.language.vm.Console.OutputType.ERROR;
import static com.telenav.kivakit.kernel.language.vm.Console.OutputType.NORMAL;

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
        output.stream().print(Message.format(text, arguments));
    }

    public void printLine(OutputType output, String text, Object... arguments)
    {
        output.stream().println(Message.format(text, arguments));
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
