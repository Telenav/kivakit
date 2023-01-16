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

package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.conversion.core.time.LocalDateTimeConverter.kivakitDateTimeConverter;
import static com.telenav.kivakit.core.time.TimeZones.utc;

public class KivaKitDateTimeConverterTest extends CoreUnitTest
{
    @Test
    public void testKivakitDateTimeConverter()
    {
        var converter = kivakitDateTimeConverter(this, utc());
        var timeString = "2023.01.11_11.00PM_UTC";
        var time = converter.convert(timeString);
        var text = converter.unconvert(time);
        ensureEqual(text, timeString);
    }
}
