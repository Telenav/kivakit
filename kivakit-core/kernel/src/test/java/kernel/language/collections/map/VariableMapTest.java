////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.collections.map;

import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class VariableMapTest
{
    @Test
    public void test()
    {
        final var map = new VariableMap<String>();
        map.add("a", "2");
        map.add("b", "two");
        ensureEqual("2 = two", map.expanded("${a} = ${b}"));
        ensureEqual("a 2 = b two", map.expanded("a ${a} = b ${b}"));
    }
}
