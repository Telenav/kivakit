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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.os.Console.OutputStream.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputStream.NORMAL;

/**
 * A text log that logs to the console. Severe log entries are logged to stderr, others to stdout. The formatter can be
 * specified from the command line as "formatter=columnar" or "formatter=unformatted". See {@link LogServiceLogger} for
 * details.
 *
 * @author jonathanl (shibo)
 * @see Console
 * @see LogEntry
 * @see BaseTextLog
 * @see LogServiceLogger
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramLogs.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ConsoleLog extends BaseTextLog
{
    /** The console to log to */
    private final Console console = new Console();

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
    @SuppressWarnings("AccessStaticViaInstance")
    public synchronized void onLog(LogEntry entry)
    {
        var outputType = entry.isSevere() ? ERROR : NORMAL;
        console.println(outputType, formatted(entry));
    }
}
