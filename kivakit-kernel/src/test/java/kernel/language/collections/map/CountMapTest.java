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

package kernel.language.collections.map;

import com.telenav.kivakit.kernel.language.collections.map.count.CountMap;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

public class CountMapTest
{
    @Test
    public void test()
    {
        var count1 = new CountMap<String>();
        count1.increment("foo");
        ensureEqual(1, count1.count("foo").asInt());
        count1.increment("foo");
        ensureEqual(2, count1.count("foo").asInt());
        count1.increment("bar");
        ensureEqual(1, count1.count("bar").asInt());
        ensureEqual(3L, count1.total());

        var count2 = new CountMap<String>();
        count2.increment("foo");
        ensureEqual(1, count2.count("foo").asInt());
        count2.increment("foo");
        count1.increment("bar");
        ensureEqual(2L, count2.total());
        ensureEqual(2, count2.count("foo").asInt());
        ensureEqual(4L, count1.total());

        var count3 = new CountMap<String>();
        count3.mergeIn(count1);
        count3.mergeIn(count2);
        count1.increment("bar");
        count1.increment("baz");
        ensureEqual(1, count1.count("baz").asInt());
        ensureEqual(4, count3.count("foo").asInt());
        ensureEqual(2, count2.count("foo").asInt());
        ensureEqual(2, count1.count("foo").asInt());
        ensureEqual(3, count1.count("bar").asInt());
        ensureEqual(1, count1.count("baz").asInt());
        ensureEqual(6L, count1.total());
        ensureEqual(2L, count2.total());
        ensureEqual(6L, count3.total());

        var count4 = new CountMap<String>();
        count4.mergeIn(count3);
        count4.mergeIn(count3);
        ensureEqual(12L, count4.total());
        ensureEqual(8, count4.count("foo").asInt());
        ensureEqual(4, count3.count("foo").asInt());
        ensureEqual(2, count2.count("foo").asInt());
        ensureEqual(2, count1.count("foo").asInt());
        ensureEqual(3, count1.count("bar").asInt());
        ensureEqual(1, count1.count("baz").asInt());
        ensureEqual(6L, count1.total());
    }
}
