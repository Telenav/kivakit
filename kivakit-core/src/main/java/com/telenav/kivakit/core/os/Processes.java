////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.os;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.io.ProgressiveStringReader;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;

/**
 * Utility methods that work with Java {@link Process} objects.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Processes
{
    /**
     * Captures the output of a process
     *
     * @param listener The listener to call with any problems
     * @param process The process
     * @return The output of the given process as a string
     */
    public static String captureOutput(Listener listener, Process process)
    {
        var in = process.getInputStream();
        try
        {
            return new ProgressiveStringReader(listener, in).readString(ProgressReporter.nullProgressReporter());
        }
        finally
        {
            IO.close(listener, in);
        }
    }

    /**
     * Redirects the output of the given process to the console
     */
    public static void redirectStandardErrorToConsole(Listener listener, Process process)
    {
        var input = process.getErrorStream();
        IO.copy(listener, input, System.err, IO.CopyStyle.UNBUFFERED);
        IO.flush(listener, System.err);
    }

    /**
     * Redirects the output of the given process to the console
     */
    public static void redirectStandardOutToConsole(Listener listener, Process process)
    {
        var input = process.getInputStream();
        IO.copy(listener, input, System.out, IO.CopyStyle.UNBUFFERED);
        IO.flush(listener, System.out);
    }

    /**
     * Waits for the given process to terminate
     */
    public static void waitForTermination(Process process)
    {
        try
        {
            process.waitFor();
        }
        catch (InterruptedException ignored)
        {
        }
    }
}
