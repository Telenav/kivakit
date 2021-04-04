package com.telenav.kivakit.core.kernel.language.vm;

import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.io.StringReader;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;

/**
 * @author jonathanl (shibo)
 */
public class Processes
{
    public static String captureOutput(final Process process)
    {
        final var in = process.getInputStream();
        try
        {
            return new StringReader(in).readString(ProgressReporter.NULL);
        }
        finally
        {
            IO.close(in);
        }
    }

    public static void copyStandardErrorToConsole(final Process process)
    {
        final var input = process.getErrorStream();
        IO.copy(input, System.out, IO.CopyStyle.UNBUFFERED);
        IO.flush(System.out);
    }

    public static void copyStandardOutToConsole(final Process process)
    {
        final var input = process.getInputStream();
        IO.copy(input, System.out, IO.CopyStyle.UNBUFFERED);
        IO.flush(System.out);
    }

    public static void waitFor(final Process process)
    {
        try
        {
            process.waitFor();
        }
        catch (final InterruptedException ignored)
        {
        }
    }
}

