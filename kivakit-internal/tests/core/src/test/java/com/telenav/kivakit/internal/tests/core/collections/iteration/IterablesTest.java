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

package com.telenav.kivakit.internal.tests.core.collections.iteration;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.collections.iteration.Iterables;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test {@link Iterables}
 *
 * @author matthieun
 */
public class IterablesTest extends CoreUnitTest
{
    @Test
    public void testSize()
    {
        final List<String> listString = new ArrayList<>();

        for (var i = 0; i < 3; i++)
        {
            listString.add("a");
        }

        final Iterable<String> stringList = new Iterable<>()
        {
            private static final int MAX = 5;

            @Override
            public @NotNull Iterator<String> iterator()
            {
                return new BaseIterator<>()
                {
                    private int counter;

                    @Override
                    protected String onNext()
                    {
                        if (counter < MAX)
                        {
                            counter++;
                            return "b";
                        }
                        return null;
                    }
                };
            }
        };

        ensureEqual(3, Iterables.size(listString));
        ensureEqual(5, Iterables.size(stringList));
    }
}
