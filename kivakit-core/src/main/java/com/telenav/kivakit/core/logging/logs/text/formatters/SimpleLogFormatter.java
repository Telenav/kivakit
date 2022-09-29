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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.text.LogFormatter;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A simple log formatter that does nothing fancy
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogs.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class SimpleLogFormatter implements LogFormatter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String format(LogEntry entry, MessageFormat... formats)
    {
        return entry.message().created().asUtc()
                + " " + entry.context()
                + " " + Classes.simpleName(entry.message().getClass())
                + " " + entry.threadName() + ": " + entry.formattedMessage(formats);
    }
}
