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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.BaseLog;
import com.telenav.kivakit.core.logging.logs.text.formatters.SimpleLogFormatter;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Arrays.arrayContains;
import static com.telenav.kivakit.core.messaging.MessageFormat.FORMATTED;
import static com.telenav.kivakit.core.messaging.MessageFormat.UNFORMATTED;

/**
 * Base class for text logs
 *
 * <p><b>Logging</b></p>
 *
 * <p>
 * More details about logging are available in <a
 * href="../../../../../../../../../kivakit-core/documentation/logging.md">kivakit-core</a>.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@UmlClassDiagram(diagram = DiagramLogs.class)
@UmlRelation(label = "formats entries with", referent = LogFormatter.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class BaseTextLog extends BaseLog
{
    /** The formatting to use */
    private MessageFormat[] formats = { FORMATTED };

    /** The formatter to use */
    private LogFormatter formatter = LogFormatter.logFormatter();

    /** Simple formatter to use when not providing formatting */
    private final LogFormatter simpleFormatter = new SimpleLogFormatter();

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
            formats = new MessageFormat[] { MessageFormat.valueOf(formatter.toUpperCase()) };
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
        return switch (formattedOrNot())
            {
                case FORMATTED -> entry.format(formatter, formattedOrNot());
                case UNFORMATTED -> entry.format(simpleFormatter, formattedOrNot());
            };
    }

    /**
     * Returns either FORMATTED OR UNFORMATTED
     */
    @NotNull
    private MessageFormat formattedOrNot()
    {
        return arrayContains(formats, FORMATTED)
            ? FORMATTED
            : UNFORMATTED;
    }
}
