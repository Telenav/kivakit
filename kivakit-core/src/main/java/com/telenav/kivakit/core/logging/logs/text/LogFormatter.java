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
import com.telenav.kivakit.core.internal.lexakai.DiagramLogs;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.logs.text.formatters.NarrowLogFormatter;
import com.telenav.kivakit.core.logging.logs.text.formatters.WideLogFormatter;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Something that formats log entries in a text log
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogs.class)
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public interface LogFormatter
{
    /**
     * Returns the log formatter specified by the KIVAKIT_LOG_FORMATTER property or environment variable
     */
    static LogFormatter formatter()
    {
        var formatter = JavaVirtualMachine.local().variables().get("KIVAKIT_LOG_FORMATTER");
        return "Wide".equalsIgnoreCase(formatter)
                ? new WideLogFormatter()
                : new NarrowLogFormatter();
    }

    /**
     * Formats a log entry in the given format
     *
     * @param entry The log entry to format
     * @param formats The format to use (formatted or un-formatted, and with or without an exception)
     * @return The formatted text
     */
    String format(LogEntry entry, MessageFormat... formats);
}
