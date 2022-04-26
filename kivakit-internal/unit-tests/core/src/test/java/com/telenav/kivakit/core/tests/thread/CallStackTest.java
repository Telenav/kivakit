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

package com.telenav.kivakit.core.tests.thread;

import com.telenav.kivakit.core.messaging.context.CallStack;
import com.telenav.kivakit.core.messaging.context.CallStack.Matching;import com.telenav.kivakit.core.test.support.CoreUnitTest;
import org.junit.Assert;
import org.junit.Test;

import static com.telenav.kivakit.core.messaging.context.CallStack.Proximity.IMMEDIATE;

public class CallStackTest extends CoreUnitTest
{
    public interface TestInterface
    {
        Class<?> testSubClass();
    }

    public static class Nested implements TestInterface
    {
        public Class<?> testExact()
        {
            return CallStack.callerOf(IMMEDIATE, Matching.EXACT, Nested.class).typeClass();
        }

        @Override
        public Class<?> testSubClass()
        {
            return CallStack.callerOf(IMMEDIATE, Matching.SUBCLASS, TestInterface.class).typeClass();
        }
    }

    @Test
    public void test()
    {
        Assert.assertEquals(CallStackTest.class, new Nested().testSubClass());
        Assert.assertEquals(CallStackTest.class, new Nested().testExact());
    }
}
