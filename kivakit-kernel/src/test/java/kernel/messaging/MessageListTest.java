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

package kernel.messaging;

import com.telenav.kivakit.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.kernel.messaging.messages.MessageFormatter;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class MessageListTest
{
    @Test
    public void test()
    {
        final var list = new MessageList(new AnythingMatcher<>());
        final Repeater repeater = new BaseRepeater(getClass());
        final var count = new MutableCount();
        list.listenTo(repeater);
        repeater.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(1L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(2L, count.asLong());
        ensureEqual(2, list.size());
        ensureEqual("Test", list.get(0).formatted(MessageFormatter.Format.WITH_EXCEPTION));
    }
}
