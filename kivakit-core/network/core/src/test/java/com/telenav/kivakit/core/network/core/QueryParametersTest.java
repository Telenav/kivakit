////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class QueryParametersTest extends UnitTest
{
    @Test
    public void testEquals()
    {
        final var q1 = new QueryParameters("d=4&a=1&c=3&b=2");
        final var q2 = new QueryParameters("a=1&b=2&c=3&d=4");
        ensureEqual(q1, q2);
    }

    @Test
    public void testMap()
    {
        final var variables = new VariableMap<String>();
        variables.put("d", "4");
        variables.put("a", "1");
        variables.put("c", "3");
        variables.put("b", "2");
        final var parameters = new QueryParameters(variables);
        ensureEqual(4, parameters.asMap().size());
        ensureEqual("a=1&b=2&c=3&d=4", parameters.toString());
        final var q1 = new QueryParameters("d=4&a=1&c=3&b=2");
        ensureEqual(q1, parameters);
    }

    @Test
    public void testParseString()
    {
        final var parameters = new QueryParameters("x=1&y=2");
        final var variables = parameters.asMap();
        ensureEqual(2, variables.size());
        ensureEqual("1", variables.get("x"));
        ensureEqual("2", variables.get("y"));
    }

    @Test
    public void testToString()
    {
        final var parameters = new QueryParameters("y=2&x=1");
        ensureEqual("x=1&y=2", parameters.toString());
        final var p2 = new QueryParameters("d=4&a=1&c=3&b=2");
        ensureEqual("a=1&b=2&c=3&d=4", p2.toString());
    }
}
