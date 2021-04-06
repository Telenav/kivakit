////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

