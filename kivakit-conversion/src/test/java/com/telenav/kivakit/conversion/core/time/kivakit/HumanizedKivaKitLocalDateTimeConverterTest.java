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

package com.telenav.kivakit.conversion.core.time.kivakit;

import com.telenav.kivakit.conversion.core.time.kivakit.HumanizedKivaKitLocalDateTimeConverter;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Hour.hourOfDay;
import static com.telenav.kivakit.core.time.LocalTime.now;
import static com.telenav.kivakit.core.time.Meridiem.PM;
import static com.telenav.kivakit.core.time.Minute.minute;

public class HumanizedKivaKitLocalDateTimeConverterTest extends CoreUnitTest
{
    @Test
    public void convert()
    {
        var converter = new HumanizedKivaKitLocalDateTimeConverter(this);

        var now = now();

        var expected = now.startOfDay()
                .withHourOfDay(hourOfDay(6, PM))
                .withMinute(minute(15));

        ensureEqual(converter.convert("Today 6.15pm"), expected);
        ensureEqual(converter.unconvert(expected), "Today 6.15PM");
    }
}
