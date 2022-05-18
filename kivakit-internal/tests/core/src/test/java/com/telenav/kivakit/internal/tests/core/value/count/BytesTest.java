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
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.level.Percent;
import org.junit.Test;

public class BytesTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        ensureEqual(0L, Bytes._0.asBytes());
        ensureEqual(10L, Bytes.bytes(10).asBytes());
        ensureEqual(Bytes.kilobytes(1), Bytes.bytes(1024));
        ensureEqual(Bytes.megabytes(1), Bytes.bytes(1024 * 1024));
        ensureEqual(Bytes.gigabytes(1), Bytes.bytes(1024 * 1024 * 1024));
        ensureEqual(Bytes.terabytes(1), Bytes.bytes(1024L * 1024L * 1024L * 1024L));
        ensureEqual(Bytes.kilobytes(0.5), Bytes.bytes(1024 / 2));
        ensureEqual(Bytes.megabytes(0.5), Bytes.bytes((1024 * 1024) / 2));
        ensureEqual(Bytes.gigabytes(0.5), Bytes.bytes((1024 * 1024 * 1024) / 2));
        ensureEqual(Bytes.terabytes(0.5), Bytes.bytes((1024L * 1024L * 1024L * 1024L) / 2));
        ensureEqual(Bytes.bytes(0), Bytes.kilobytes(1).percent(Percent._0));
        ensureEqual(Bytes.bytes(512), Bytes.kilobytes(1).percent(Percent._50));
        ensureEqual(Bytes.bytes(1024), Bytes.kilobytes(1).percent(Percent._100));
        ensureEqual(1024L, Bytes.kilobytes(1).asBytes());
        ensureEqual(1024.0, Bytes.megabytes(1).asKilobytes());
        ensureEqual(1024.0, Bytes.gigabytes(1).asMegabytes());
        ensureEqual(1024.0, Bytes.terabytes(1).asGigabytes());
    }
}
