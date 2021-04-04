////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.object;

import com.telenav.kivakit.core.kernel.language.types.Classes;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ClassesTest
{
    @Test
    public void testForName()
    {
        ensureEqual(Integer.class, Classes.forName("java.lang.Integer"));
    }

    @Test
    public void testIsPrimitive()
    {
        ensure(!Classes.isPrimitive(ClassesTest.class));
        ensure(Classes.isPrimitive(Integer.TYPE));
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
