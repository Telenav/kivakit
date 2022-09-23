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

package com.telenav.kivakit.core.logging.logs.text.formatters;

import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Formats log entries into flexible delimited columns.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("DuplicatedCode")
@UmlClassDiagram(diagram = DiagramLogs.class)
public class WideLogFormatter extends BaseColumnarFormatter
{
    public static final WideLogFormatter INSTANCE = new WideLogFormatter();

    private enum TimeType
    {
        ELAPSED,
        ABSOLUTE_TIME
    }

    private static final TimeType timeType = TimeType.ELAPSED;

    private final Column contextColumn = new Column(30, 30, Layout.CLIP_RIGHT);

    private final Column durationColumn = new Column(6, 10, Layout.CLIP_RIGHT);

    private final Column messageColumn = new Column(150, 150, Layout.WRAP);

    private final Column sequenceNumberColumn = new Column(6, 10, Layout.CLIP_RIGHT);

    private final Time start = Time.now();

    private final Column threadColumn = new Column(16, 24, Layout.CLIP_LEFT);

    private final Column timeColumn = new Column(21, 21, Layout.CLIP_RIGHT);

    private final Column typeColumn = new Column(12, 20, Layout.CLIP_RIGHT);

    public String format(LogEntry entry, Formatter.Format format)
    {
        // Create line output
        var line = new LineOutput();

        // Add each column and its content
        var message = entry.message();
        line.add(sequenceNumberColumn, String.valueOf(entry.sequenceNumber()));
        if (timeType == TimeType.ELAPSED)
        {
            line.add(durationColumn, start.elapsedSince().asString(StringFormattable.Format.USER_LABEL));
        }
        else
        {
            line.add(timeColumn, message.created().utc().toString());
        }
        line.add(threadColumn, entry.threadName());
        line.add(contextColumn, entry.context().typeName());
        line.add(typeColumn, entry.messageType());
        line.add(messageColumn, entry.formattedMessage(format));

        // Return the formatted line
        return line.format();
    }
}
