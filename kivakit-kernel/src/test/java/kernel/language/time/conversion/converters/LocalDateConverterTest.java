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

package kernel.language.time.conversion.converters;

import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.time.conversion.converters.LocalDateConverter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class LocalDateConverterTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Test
    public void convert()
    {
        var converter = new LocalDateConverter(LOGGER, LocalTime.utcTimeZone());

        var time = converter.convert("2011.07.06");

        // Using https://www.epochconverter.com/ midnight of that date UTC time
        // was converted to milliseconds.
        ensure(time != null);
        assert time != null;
        ensureEqual(ChronoUnit.MILLIS.between(Instant.EPOCH, time.startOfDay().asInstant()), 1309910400000L);
    }
}
