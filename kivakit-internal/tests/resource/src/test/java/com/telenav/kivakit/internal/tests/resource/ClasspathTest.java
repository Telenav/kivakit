package com.telenav.kivakit.internal.tests.resource;

import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Test;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.resource.packages.Classpath.classpath;

/**
 * Test resource globbing, similar to UNIX glob matching
 *
 * @author jonathanl (shibo)
 */
public class ClasspathTest extends UnitTest implements
        PackageTrait,
        JavaTrait
{
    @Test
    public void test()
    {
        var expected = set("ResourceTest.properties", "a.txt", "b.txt");
        var resources = classpath().resourcesIn(this, packageForThis().reference());
        ensureEqual(expected.size(), resources.size());
        for (var at : expected)
        {
            ensure(resources.matching(it -> it.fileName().name().equals(at)).size() == 1);
        }
    }
}
