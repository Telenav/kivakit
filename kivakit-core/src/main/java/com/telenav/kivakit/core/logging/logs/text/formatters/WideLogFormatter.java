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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.logging.logs.text.formatters.BaseColumnarFormatter.ColumnLayout.*;
import static com.telenav.kivakit.core.logging.logs.text.formatters.WideLogFormatter.TimeType.ELAPSED;
import static com.telenav.kivakit.core.time.Time.now;

/**
 * Formats log entries into flexible delimited columns.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "DuplicatedCode", "unused" })
@UmlClassDiagram(diagram = DiagramLogs.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class WideLogFormatter extends BaseColumnarFormatter
{
    /**
     * What type of time to display
     */
    public enum TimeType
    {
        /** Show duration elapsed from start time */
        ELAPSED,

        /** Show the absolute time */
        ABSOLUTE_TIME
    }

    /** The type of time to show */
    private TimeType timeType = ELAPSED;

    /** The code context column, showing class names */
    private final Column contextColumn = new Column(30, 30, CLIP_RIGHT);

    /** The elapsed time column */
    private final Column elapsedColumn = new Column(6, 10, CLIP_RIGHT);

    /** The message text column */
    private final Column messageTextColumn = new Column(150, 150, WRAP);

    /** The sequence number column */
    private final Column sequenceNumberColumn = new Column(6, 10, CLIP_RIGHT);

    /** The time this formatter was created */
    private final Time start = now();

    /** The thread name column */
    private final Column threadColumn = new Column(16, 24, CLIP_LEFT);

    /** The absolute time column */
    private final Column timeColumn = new Column(21, 21, CLIP_RIGHT);

    /** The message type column */
    private final Column typeColumn = new Column(12, 20, CLIP_RIGHT);

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(LogEntry entry, MessageFormat... formats)
    {
        // Create line output
        var line = new LineOutput();

        // Add each column and its content
        var message = entry.message();
        line.addColumnText(sequenceNumberColumn, String.valueOf(entry.sequenceNumber()));
        if (timeType == ELAPSED)
        {
            line.addColumnText(elapsedColumn, start.elapsedSince().asString(StringFormattable.Format.USER_LABEL));
        }
        else
        {
            line.addColumnText(timeColumn, message.created().asUtc().toString());
        }
        line.addColumnText(threadColumn, entry.threadName());
        line.addColumnText(contextColumn, entry.context().typeName());
        line.addColumnText(typeColumn, entry.messageType());
        line.addColumnText(messageTextColumn, entry.formattedMessage(formats));

        // Return the formatted line
        return line.format();
    }

    /**
     * Sets the type of time to show, elapsed or absolute
     */
    public void timeType(TimeType timeType)
    {
        this.timeType = timeType;
    }
}
