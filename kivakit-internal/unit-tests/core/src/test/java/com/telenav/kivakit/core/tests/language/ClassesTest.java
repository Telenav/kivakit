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

package com.telenav.kivakit.core.tests.language;

import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.language.primitive.Primitives;import com.telenav.kivakit.core.test.support.CoreUnitTest;
import org.junit.Test;

public class ClassesTest extends CoreUnitTest
{
    @Test
    public void testForName()
    {
        ensureEqual(Integer.class, Classes.forName("java.lang.Integer"));
    }

    @Test
    public void testIsPrimitive()
    {
        ensure(!Primitives.isPrimitive(ClassesTest.class));
        ensure(Primitives.isPrimitive(Integer.TYPE));
    }

    @Test
    public void testSimpleName()
    {
        ensureEqual("ClassesTest", Classes.simpleName(getClass()));
        final Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                ensureEqual("ClassesTest.1", Classes.simpleName(getClass()));
            }
        };
        runnable.run();
    }
}
