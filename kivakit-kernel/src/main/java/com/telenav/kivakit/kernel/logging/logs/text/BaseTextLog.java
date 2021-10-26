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

package com.telenav.kivakit.kernel.logging.logs.text;

import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.kernel.logging.logs.text.formatters.ColumnarLogFormatter;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLoggingLogs;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
@UmlRelation(label = "formats entries with", referent = LogFormatter.class)
public abstract class BaseTextLog extends BaseLog
{
    /**
     * The type of formatting to perform on log entries
     */
    @LexakaiJavadoc(complete = true)
    public enum Format
    {
        FORMATTED,
        UNFORMATTED,
    }

    private Format format = Format.FORMATTED;

    private LogFormatter formatter = ColumnarLogFormatter.DEFAULT;

    @Override
    @UmlExcludeMember
    @MustBeInvokedByOverriders
    public void configure(final VariableMap<String> properties)
    {
        final var formatter = properties.get("formatter");
        if (formatter != null)
        {
            this.format = Format.valueOf(formatter.toUpperCase());
        }
    }

    public void formatter(final LogFormatter formatter)
    {
        this.formatter = formatter;
    }

    protected String formatted(LogEntry entry)
    {
        switch (format)
        {
            case UNFORMATTED:
                return entry.message().formatted(MessageFormatter.Format.WITH_EXCEPTION);

            case FORMATTED:
                return format(entry, MessageFormatter.Format.WITH_EXCEPTION);

            default:
                return fail("Unsupported format: $", format);
        }
    }

    private String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return entry.format(formatter, format);
    }
}
