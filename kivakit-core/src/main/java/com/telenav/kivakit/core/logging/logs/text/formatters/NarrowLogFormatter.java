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

import com.telenav.kivakit.core.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Formats log entries into flexible delimited columns.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("DuplicatedCode")
@UmlClassDiagram(diagram = DiagramLogs.class)
public class NarrowLogFormatter extends BaseColumnarFormatter
{
    public static final NarrowLogFormatter INSTANCE = new NarrowLogFormatter();

    private final Column contextColumn = new Column(12, 12, Layout.CLIP_RIGHT);

    private final Column messageColumn = new Column(128, 128, Layout.WRAP);

    private final Column typeColumn = new Column(4, 4, Layout.CLIP_RIGHT);

    public String format(LogEntry entry, Formatter.Format format)
    {
        // Create line output
        var line = new LineOutput();

        // Add each column and its content
        line.add(contextColumn, entry.context().typeName());
        line.add(typeColumn, entry.messageType());
        line.add(messageColumn, entry.formattedMessage(format));

        // Return the formatted line
        return line.format();
    }
}
