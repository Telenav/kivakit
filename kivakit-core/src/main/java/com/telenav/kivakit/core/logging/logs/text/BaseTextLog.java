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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.BaseLog;
import com.telenav.kivakit.core.logging.logs.text.formatters.BaseColumnarFormatter;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.MessageFormat.WITH_EXCEPTION;

/**
 * Base class for text logs
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogs.class)
@UmlRelation(label = "formats entries with", referent = LogFormatter.class)
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseTextLog extends BaseLog
{
    /**
     * The type of formatting to perform on log entries
     */
    public enum Formatting
    {
        FORMATTED,
        @SuppressWarnings("SpellCheckingInspection")
        UNFORMATTED,
    }

    /** The formatting to use */
    private Formatting format = Formatting.FORMATTED;

    /** The formatter to use */
    private LogFormatter formatter = BaseColumnarFormatter.get();

    /**
     * Configures this log with the given properties
     *
     * @param properties A property map specific to the type of log
     */
    @Override
    @UmlExcludeMember
    @MustBeInvokedByOverriders
    public void configure(VariableMap<String> properties)
    {
        var formatter = properties.get("formatter");
        if (formatter != null)
        {
            format = Formatting.valueOf(formatter.toUpperCase());
        }
    }

    /**
     * Sets the log formatter to use
     */
    public void formatter(LogFormatter formatter)
    {
        this.formatter = formatter;
    }

    /**
     * Returns formatted text for the log entry
     */
    protected String formatted(LogEntry entry)
    {
        switch (format)
        {
            case UNFORMATTED:
                return entry.message().formatted(WITH_EXCEPTION);

            case FORMATTED:
                return format(entry, WITH_EXCEPTION);

            default:
                fail("Unsupported format: $", format);
                return null;
        }
    }

    /**
     * Formats the given log entry using the given format (with, or without an exception)
     */
    @SuppressWarnings("SameParameterValue")
    private String format(LogEntry entry, MessageFormat format)
    {
        return entry.format(formatter, format);
    }
}
