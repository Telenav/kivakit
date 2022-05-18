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

package com.telenav.kivakit.internal.tests.core.language.primitive;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.internal.test.support.CoreUnitTest;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class IntsTest extends CoreUnitTest
{
    @Test
    public void testParse()
    {
        for (int i = -100_000; i < 100_000; i++)
        {
            ensureEqual(Ints.parseFast("" + i, -1), i);
        }
        ensureEqual(Ints.parseFast(null, -1), -1);
        ensureEqual(Ints.parseFast("", -1), -1);
        ensureEqual(Ints.parseFast("99", -1), 99);
        ensureEqual(Ints.parseFast("99", -1), 99);
        ensureEqual(Ints.parseFast("abc", -1), -1);
    }
}
