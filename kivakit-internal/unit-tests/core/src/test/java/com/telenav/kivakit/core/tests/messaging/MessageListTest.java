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

package com.telenav.kivakit.core.tests.messaging;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.string.Formatter;import com.telenav.kivakit.core.test.support.CoreUnitTest;
import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import org.junit.Test;

public class MessageListTest extends CoreUnitTest
{
    @Test
    public void test()
    {
        var list = new MessageList(Matcher.matchAll());
        Repeater repeater = new BaseRepeater(getClass());
        var count = new MutableCount();
        list.listenTo(repeater);
        repeater.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(1L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(2L, count.asLong());
        ensureEqual(2, list.size());
        ensureEqual("Test", list.get(0).formatted(Formatter.Format.WITH_EXCEPTION));
    }
}
