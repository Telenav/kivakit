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

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.BaseLog;
import com.telenav.kivakit.core.logging.logs.text.formatters.BaseColumnarFormatter;
import com.telenav.kivakit.core.string.Formatter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

@UmlClassDiagram(diagram = DiagramLogs.class)
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
        @SuppressWarnings("SpellCheckingInspection")
        UNFORMATTED,
    }

    private Format format = Format.FORMATTED;

    private LogFormatter formatter = BaseColumnarFormatter.get();

    @Override
    @UmlExcludeMember
    @MustBeInvokedByOverriders
    public void configure(VariableMap<String> properties)
    {
        var formatter = properties.get("formatter");
        if (formatter != null)
        {
            format = Format.valueOf(formatter.toUpperCase());
        }
    }

    public void formatter(LogFormatter formatter)
    {
        this.formatter = formatter;
    }

    protected String formatted(LogEntry entry)
    {
        switch (format)
        {
            case UNFORMATTED:
                return entry.message().formatted(Formatter.Format.WITH_EXCEPTION);

            case FORMATTED:
                return format(entry, Formatter.Format.WITH_EXCEPTION);

            default:
                fail("Unsupported format: $", format);
                return null;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private String format(LogEntry entry, Formatter.Format format)
    {
        return entry.format(formatter, format);
    }
}
