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

package com.telenav.kivakit.internal.tests.core.value.count;

import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.value.count.Bytes._0;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.core.value.count.Bytes.gigabytes;
import static com.telenav.kivakit.core.value.count.Bytes.kilobytes;
import static com.telenav.kivakit.core.value.count.Bytes.megabytes;
import static com.telenav.kivakit.core.value.count.Bytes.terabytes;

public class BytesTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensureEqual(0L, _0.asBytes());
        ensureEqual(10L, bytes(10).asBytes());
        ensureEqual(kilobytes(1), bytes(1024));
        ensureEqual(megabytes(1), bytes(1024 * 1024));
        ensureEqual(gigabytes(1), bytes(1024 * 1024 * 1024));
        ensureEqual(terabytes(1), bytes(1024L * 1024L * 1024L * 1024L));
        ensureEqual(kilobytes(0.5), bytes(1024 / 2));
        ensureEqual(megabytes(0.5), bytes((1024 * 1024) / 2));
        ensureEqual(gigabytes(0.5), bytes((1024 * 1024 * 1024) / 2));
        ensureEqual(terabytes(0.5), bytes((1024L * 1024L * 1024L * 1024L) / 2));
        ensureEqual(bytes(0), kilobytes(1).percent(Percent._0));
        ensureEqual(bytes(512), kilobytes(1).percent(Percent._50));
        ensureEqual(bytes(1024), kilobytes(1).percent(Percent._100));
        ensureEqual(1024L, kilobytes(1).asBytes());
        ensureEqual(1024.0, megabytes(1).asKilobytes());
        ensureEqual(1024.0, gigabytes(1).asMegabytes());
        ensureEqual(1024.0, terabytes(1).asGigabytes());
    }
}
