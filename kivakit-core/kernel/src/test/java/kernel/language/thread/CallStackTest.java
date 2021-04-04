////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.thread;

import com.telenav.kivakit.core.kernel.language.threading.context.CallStack;
import com.telenav.kivakit.core.kernel.language.threading.context.CallStack.Matching;
import org.junit.Assert;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.language.threading.context.CallStack.Proximity.IMMEDIATE;

public class CallStackTest
{
    public interface TestInterface
    {
        Class<?> testSubClass();
    }

    public static class Nested implements TestInterface
    {
        public Class<?> testExact()
        {
            return CallStack.callerOf(IMMEDIATE, Matching.EXACT, Nested.class).type();
        }

        @Override
        public Class<?> testSubClass()
        {
            return CallStack.callerOf(IMMEDIATE, Matching.SUBCLASS, TestInterface.class).type();
        }
    }

    @Test
    public void test()
    {
        Assert.assertEquals(CallStackTest.class, new Nested().testSubClass());
        Assert.assertEquals(CallStackTest.class, new Nested().testExact());
    }
}
