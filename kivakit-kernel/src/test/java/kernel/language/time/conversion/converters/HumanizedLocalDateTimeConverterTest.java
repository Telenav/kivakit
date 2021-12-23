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
import com.telenav.kivakit.kernel.language.time.conversion.converters.HumanizedLocalDateTimeConverter;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.kernel.language.time.Meridiem.PM;

public class HumanizedLocalDateTimeConverterTest
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Test
    public void convert()
    {
        var converter = new HumanizedLocalDateTimeConverter(LOGGER);

        var now = LocalTime.now();

        var expected = now.startOfDay().withHourOfMeridiem(6, PM).withMinuteOfHour(15);
        ensureEqual(converter.convert("Today 6.15pm"), expected);
        ensureEqual(converter.unconvert(expected), "Today 6.15PM");
    }
}