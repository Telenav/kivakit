package com.telenav.kivakit.mixins;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AttributedMixinTest
{
    static class A implements AttributesMixin<String, String>
    {
    }

    static class B implements AttributesMixin<String, String>
    {
    }

    @Test
    public void test()
    {
        var a = new A();
        var b = new B();

        a.attribute("name", "This is object A");
        b.attribute("name", "This is object B");

        assertEquals("This is object A", a.attribute("name"));
        assertEquals("This is object B", b.attribute("name"));
    }
}
