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

import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.core.os.Console;
import com.telenav.kivakit.core.lexakai.DiagramLogs;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.core.os.Console.OutputType.ERROR;
import static com.telenav.kivakit.core.os.Console.OutputType.NORMAL;

/**
 * A text log that logs to the console. The formatter can be specified from the command line as "formatter=columnar" or
 * "formatter=unformatted". See {@link LogServiceLogger} for details.
 *
 * @author jonathanl (shibo)
 * @see BaseTextLog
 * @see LogServiceLogger
 */
@UmlClassDiagram(diagram = DiagramLogs.class)
@LexakaiJavadoc(complete = true)
public class ConsoleLog extends BaseTextLog
{
    private final Console console = new Console();

    @Override
    @UmlExcludeMember
    public void flush(LengthOfTime maximumWaitTime)
    {
        super.flush(maximumWaitTime);
        console.flush(maximumWaitTime);
    }

    @Override
    @UmlExcludeMember
    public String name()
    {
        return "Console";
    }

    @Override
    @UmlExcludeMember
    public synchronized void onLog(LogEntry entry)
    {
        console.println(entry.isSevere() ? ERROR : NORMAL, formatted(entry));
    }
}
