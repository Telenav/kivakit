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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Formats log entries into flexible delimited columns.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("DuplicatedCode")
@UmlClassDiagram(diagram = DiagramLogs.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class NarrowLogFormatter extends BaseColumnarFormatter
{
    /** The code context column */
    private final Column contextColumn = new Column(12, 12, ColumnLayout.CLIP_RIGHT);

    /** The message column */
    private final Column messageColumn = new Column(128, 128, ColumnLayout.WRAP);

    /** The message type column */
    private final Column typeColumn = new Column(4, 4, ColumnLayout.CLIP_RIGHT);

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(LogEntry entry, MessageFormat... formats)
    {
        // Create line output,
        var line = new LineOutput();

        // add each column and its content,
        line.addColumnText(contextColumn, entry.context().typeName());
        line.addColumnText(typeColumn, entry.messageType());
        line.addColumnText(messageColumn, entry.formattedMessage(formats));

        // and return the formatted line
        return line.format();
    }
}
