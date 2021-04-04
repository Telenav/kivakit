////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.name.Name;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class NameTest
{
    @Test
    public void test()
    {
        final Name bob = new Name("Bob");
        ensureEqual(new Name("Bob"), bob);
        ensureEqual("Bob", bob.name());
        ensureEqual(new Name("bob"), bob.lowerCase());
        ensureEqual("Bob", bob.toString());
    }
}
