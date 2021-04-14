////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.logs.file;

import com.telenav.kivakit.core.kernel.language.io.ByteSizedOutput;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.logging.LogEntry;
import com.telenav.kivakit.core.kernel.logging.logs.text.BaseTextLog;
import com.telenav.kivakit.logs.file.project.lexakai.diagrams.DiagramLogsFile;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.io.OutputStream;
import java.io.PrintWriter;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.kernel.messaging.messages.MessageFormatter.Format.WITH_EXCEPTION;

/**
 * Base class for rollover text logs such as {@link FileLog}. Accepts a {@link #maximumLogSize(Bytes)} and a {@link
 * #rollover(Rollover)} period and logs messages until either of these limits are reached.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLogsFile.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseRolloverTextLog extends BaseTextLog
{
    /**
     * Rollover period
     */
    @UmlClassDiagram(diagram = DiagramLogsFile.class)
    @LexakaiJavadoc(complete = true)
    public enum Rollover
    {
        NONE,
        DAILY,
        HOURLY
    }

    @UmlAggregation
    private Rollover rollover = Rollover.NONE;

    @UmlAggregation(label = "maximum size")
    private Bytes maximumLogSize;

    @UmlAggregation(label = "start time")
    private Time started = Time.now();

    @UmlAggregation(label = "rollover time")
    private Time rolloverAt = nextRollover();

    private PrintWriter out;

    private ByteSizedOutput outputSize;

    protected BaseRolloverTextLog()
    {
        maximumLogSize(Bytes.megabytes(50));
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            flush(Duration.ONE_MINUTE);
            out.close();
        }));
    }

    @Override
    public void closeOutput()
    {
        try
        {
            flush(Duration.seconds(10));
            out().flush();
            out().close();
        }
        catch (final Exception ignored)
        {
        }
    }

    @Override
    public void flush(final Duration maximumWaitTime)
    {
        super.flush(maximumWaitTime);
        out.flush();
    }

    public void maximumLogSize(final Bytes maximumSize)
    {
        maximumLogSize = maximumSize;
    }

    @Override
    public final synchronized void onLog(final LogEntry entry)
    {
        final var timeToRollOver = Time.now().isAfter(rolloverAt);
        final var sizeToRollOver = outputSize != null && outputSize.bytes().isGreaterThan(maximumLogSize);
        if (timeToRollOver || sizeToRollOver)
        {
            try
            {
                closeOutput();
            }
            finally
            {
                if (timeToRollOver)
                {
                    started = rolloverAt;
                    rolloverAt = nextRollover();
                }
                else
                {
                    started = Time.now();
                }
                out = null;
            }
        }

        final var formatted = format(entry, WITH_EXCEPTION);
        out().println(formatted);
        out().flush();
    }

    public void rollover(final Rollover rollover)
    {
        this.rollover = rollover;
    }

    protected abstract OutputStream newOutputStream();

    protected Time nextRollover()
    {
        switch (rollover)
        {
            case NONE:
                return Time.MAXIMUM;
            case DAILY:
                return Time.now().localTime().startOfTomorrow();
            case HOURLY:
                return Time.now().localTime().startOfNextHour();
            default:
                return unsupported();
        }
    }

    protected Time started()
    {
        return started;
    }

    private PrintWriter newPrintWriter()
    {
        outputSize = new ByteSizedOutput(newOutputStream());
        return new PrintWriter(outputSize);
    }

    private PrintWriter out()
    {
        if (out == null)
        {
            out = newPrintWriter();
        }
        return out;
    }
}
