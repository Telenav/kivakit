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

package com.telenav.kivakit.core.logging.loggers;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.logging.LogEntry;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerCodeContext;
import com.telenav.kivakit.core.project.lexakai.DiagramLogging;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.time.LengthOfTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A logger that does nothing.
 * <p>
 * The following code can be used to efficiently shut off all logging:
 * </p>
 * <pre>
 * LogFactory.factory(NullLogger::new)
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogging.class)
public class NullLogger implements Logger
{
    @Override
    public void addFilter(Filter<LogEntry> filter)
    {
    }

    @Override
    public LoggerCodeContext codeContext()
    {
        return new LoggerCodeContext();
    }

    @Override
    public List<Filter<LogEntry>> filters()
    {
        return List.of();
    }

    @Override
    public void flush(LengthOfTime maximumWaitTime)
    {
    }

    @Override
    public void log(Message message)
    {
    }

    @Override
    public void log(LoggerCodeContext context, Thread thread, Message message)
    {
    }

    @Override
    public Time startTime()
    {
        return Time.now();
    }
}
