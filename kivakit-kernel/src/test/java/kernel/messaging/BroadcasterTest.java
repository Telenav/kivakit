////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.messaging;

import com.telenav.kivakit.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.kernel.messaging.broadcasters.Multicaster;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BroadcasterTest
{
    @Test
    public void test()
    {
        final var broadcaster = new Multicaster("test", getClass());
        final var count = new MutableCount();
        broadcaster.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        broadcaster.information("Test");
        ensureEqual(1L, count.asLong());
        broadcaster.information("Test");
        ensureEqual(2L, count.asLong());
    }
}
