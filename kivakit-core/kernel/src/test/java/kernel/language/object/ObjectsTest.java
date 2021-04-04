////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.object;

import com.telenav.kivakit.core.kernel.language.objects.Hash;
import com.telenav.kivakit.core.kernel.language.objects.Objects;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotEqual;

public class ObjectsTest
{
    @Test
    public void testEquals()
    {
        ensure(Objects.equal(1, 1));
        ensure(Objects.equal(1, 1));
        ensureFalse(Objects.equal(1, 1.0));
        ensure(Objects.equal(null, null));
        ensureFalse(Objects.equal(1, null));
        ensureFalse(Objects.equal(null, 1));
        ensureFalse(Objects.equal(1, ""));
    }

    @Test
    public void testHashCode()
    {
        ensureEqual(Hash.many(1, 2, 3), Hash.many(1, 2, 3));
        ensureNotEqual(Hash.many(1, 2, 3), Hash.many(3, 2, 1));
    }

    @Test
    public void testIsPrimitiveWrapper()
    {
        ensureFalse(Objects.isPrimitiveWrapper(ClassesTest.class));
        ensure(Objects.isPrimitiveWrapper(7));
    }
}
