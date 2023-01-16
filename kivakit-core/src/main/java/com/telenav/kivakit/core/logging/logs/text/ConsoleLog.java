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

package com.telenav.kivakit.core.logging.logs.text;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.os.Console.OutputType.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputType.NORMAL;
import static com.telenav.kivakit.core.os.Console.console;

/**
 * A text log that logs to the console. Severe log entries are logged to stderr, others to stdout. The formatter can be
 * specified from the command line as "formatter=columnar" or "formatter=unformatted". See {@link LogServiceLogger} for
 * details.
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Console
 * @see LogEntry
 * @see BaseTextLog
 * @see LogServiceLogger
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramLogs.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ConsoleLog extends BaseTextLog
{
    /** The console to log to */
    private final Console console = console();

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public void flush(Duration maximumWaitTime)
    {
        super.flush(maximumWaitTime);
        console.flush(maximumWaitTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public String name()
    {
        return "Console";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public synchronized void onLog(LogEntry entry)
    {
        var outputType = entry.isSevere() ? ERROR : NORMAL;
        console.println(outputType, formatted(entry));
    }
}
