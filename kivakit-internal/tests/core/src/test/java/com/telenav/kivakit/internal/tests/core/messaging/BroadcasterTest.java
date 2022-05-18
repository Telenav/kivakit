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

package com.telenav.kivakit.internal.tests.core.messaging;

import com.telenav.kivakit.core.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import com.telenav.kivakit.core.value.count.MutableCount;
import org.junit.Test;

import static com.telenav.kivakit.core.ensure.Ensure.ensureEqual;

public class BroadcasterTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var broadcaster = new Multicaster("test", getClass());
        var count = new MutableCount();
        broadcaster.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        broadcaster.information("Test");
        ensureEqual(1L, count.asLong());
        broadcaster.information("Test");
        ensureEqual(2L, count.asLong());
    }
}
