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

package com.telenav.kivakit.core.language.primitive;

import com.telenav.kivakit.core.ensure.Ensure;import com.telenav.kivakit.core.test.CoreUnitTest;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class DoublesTest extends CoreUnitTest
{
    @Test
    public void testParse()
    {
        Ensure.ensureEqual(3.1415, Doubles.fastParse("3.1415", 1_0000));
        Ensure.ensureEqual(31.415, Doubles.fastParse("31.415", 1_000));
    }
}
