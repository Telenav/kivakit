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

package com.telenav.kivakit.core.kernel.logging.logs.text;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLoggingLogs;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Map;

@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
public class ConsoleLog extends BaseTextLog
{
    public enum Format
    {
        COLUMNAR,
        UNFORMATTED
    }

    private Format formatter = Format.COLUMNAR;

    @Override
    @UmlExcludeMember
    public void configure(final Map<String, String> properties)
    {
        final var formatter = properties.get("formatter");
        if (formatter != null)
        {
            this.formatter = Format.valueOf(formatter.toUpperCase());
        }
    }

    @Override
    @UmlExcludeMember
    public void flush(final Duration maximumWaitTime)
    {
        super.flush(maximumWaitTime);
        System.out.flush();
        System.err.flush();
    }

    @Override
    @UmlExcludeMember
    public String name()
    {
        return "Console";
    }

    @Override
    @UmlExcludeMember
    public synchronized void onLog(final LogEntry entry)
    {
        final var out = entry.isSevere() ? System.err : System.out;
        switch (formatter)
        {
            case UNFORMATTED:
                out.println(entry.message().formatted(MessageFormatter.Format.WITH_EXCEPTION));
                break;

            case COLUMNAR:
                final var formatted = format(entry, MessageFormatter.Format.WITH_EXCEPTION);
                out.println(formatted);
                break;
        }
    }
}
