////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.identifier.ObjectIdentifier;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class ObjectIdentifierTest
{
    @Test
    public void test()
    {
        final var oid = new ObjectIdentifier<>("foo");
        ensureEqual(new ObjectIdentifier<>("foo"), oid);
        ensureEqual("foo".hashCode(), oid.hashCode());
        ensureEqual("foo", oid.object());
    }
}
