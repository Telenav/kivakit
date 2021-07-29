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

package kernel.formatters;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.logging.LogEntry;
import com.telenav.kivakit.kernel.logging.LoggerCodeContext;
import com.telenav.kivakit.kernel.logging.loggers.LogServiceLogger;
import com.telenav.kivakit.kernel.logging.logs.text.formatters.UtcDateTimeLogFormatter;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * Created by bogdantnv on 12/07/16.
 */
public class IsoTimeLogFormatterTest
{
    @Test
    public void test()
    {
        final LoggerCodeContext context = new LoggerCodeContext("Here");
        final Information message = new Information("This is a test");
        message.created(Time.milliseconds(1468309671357L));

        final var logger = new LogServiceLogger();
        final var entry = new LogEntry(logger, context, Thread.currentThread(), message);

        final String actual = new UtcDateTimeLogFormatter().format(entry, MessageFormatter.Format.WITH_EXCEPTION);
        final var expected = "2016-07-12T07:47:51.357_UTC Here Information: This is a test";
        ensureEqual(expected, actual);
    }
}
