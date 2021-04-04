////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library.xml;

import com.telenav.kivakit.core.kernel.language.values.count.Count;
import com.telenav.kivakit.core.resource.resources.string.StringResource;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class XmlReaderTest extends UnitTest
{
    @Test
    public void testAdvanceTo()
    {
        final var reader = reader();
        reader.advanceTo("nondepartment");
        ensureEqual("foo", reader.current().attribute("name"));
    }

    @Test
    public void testCdata()
    {
        final var reader = reader2();
        reader.next();
        reader.next();
        final var node = reader.current();
        ensureEqual("flow_data_src=INRIX;client=traffixplore", node.text());
    }

    @Test
    public void testChildren()
    {
        final var reader = reader();
        final var node = reader.next();
        ensureEqual("telenav", node.attribute("name"));
        final var children = node.children();
        ensureEqual("map", children.get(0).text());
        ensureEqual("server", children.get(2).text());
        ensureEqual("client", children.get(3).text());
    }

    @Test
    public void testChildrenMatching()
    {
        final var reader = reader();
        ensureEqual(Count._3, Count.count(reader.childrenMatching(value -> value.name().equals("department")).iterator()));
    }

    @Test
    public void testCurrent()
    {
        final var reader = reader();
        reader.advanceTo("nondepartment");
        ensureEqual("foo", reader.current().attribute("name"));
        ensureEqual("foo", reader.current().attribute("name"));
        ensureEqual("foo", reader.current().attribute("name"));
    }

    @Test
    public void testInvalidLazyRead()
    {
        try
        {
            final var reader = reader();
            final var node = reader.next();
            ensureEqual("telenav", node.attribute("name"));
            // This should cause node.getChildren() to throw an exception
            reader.next();
            final var children = node.children();
            ensureEqual("map", children.get(0).text());
            ensureEqual("server", children.get(2).text());
            ensureEqual("client", children.get(3).text());
            fail("should have thrown exceptions");
        }
        catch (final Exception e)
        {
            // Should throw exception
        }
    }

    @Test
    public void testIterator()
    {
        final var reader = reader();
        var elementCount = 0;
        while (reader.hasNext())
        {
            reader.next();
            elementCount++;
        }
        ensureEqual(5, elementCount);
    }

    @Test
    public void testNodesMatching()
    {
        final var reader = reader();
        ensureEqual(Count._3, Count.count(reader.nodesMatching(value -> value.name().equals("department")).iterator()));
    }

    private XmlReader reader()
    {
        final var testXml = new StringResource("<company name='telenav'>"
                + "<department>map</department><nondepartment name='foo'/><department>server</department>"
                + "<department>client</department></company>");
        return new XmlReader(testXml);
    }

    private XmlReader reader2()
    {
        final var testXml = new StringResource("<company name='telenav'>"
                + "<department><![CDATA[flow_data_src=INRIX;client=traffixplore]]></department></company>");
        return new XmlReader(testXml);
    }
}
