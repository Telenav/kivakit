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

package com.telenav.kivakit.kernel.logging.logs.text;

import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.logs.BaseLog;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.logging.logs.text.formatters.ColumnarFormatter;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLoggingLogs;

@UmlClassDiagram(diagram = DiagramLoggingLogs.class)
@UmlRelation(label = "formats entries with", referent = LogEntryFormatter.class)
public abstract class BaseTextLog extends BaseLog
{
    private LogEntryFormatter formatter = ColumnarFormatter.DEFAULT;

    public void formatter(final LogEntryFormatter formatter)
    {
        this.formatter = formatter;
    }

    protected String format(final LogEntry entry, final MessageFormatter.Format format)
    {
        return entry.format(formatter, format);
    }
}