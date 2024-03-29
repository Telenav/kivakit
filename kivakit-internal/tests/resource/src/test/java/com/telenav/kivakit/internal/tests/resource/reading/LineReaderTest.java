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

package com.telenav.kivakit.internal.tests.resource.reading;

import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static java.lang.Integer.parseInt;

public class LineReaderTest extends UnitTest
{
    @Test
    public void test()
    {
        var i = 1;
        for (String line : packageResource("test.txt").reader().readLines())
        {
            ensureEqual(i++, parseInt(line));
        }
    }
}
