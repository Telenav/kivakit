package com.telenav.kivakit.internal.tests.resource;

import com.telenav.kivakit.test.UnitTest;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.resource.packages.PackageTrait;
import org.junit.Test;

import static com.telenav.kivakit.resource.ResourceGlob.match;
import static com.telenav.kivakit.resource.Extension.TXT;

/**
 * Test resource globbing, similar to UNIX glob matching
 *
 * @author jonathanl (shibo)
 */
public class ResourceGlobTest extends UnitTest implements PackageTrait, JavaTrait
{
    @Test
    public void test()
    {
        var here = thisPackage();
        for (var at : here.resources(match(TXT)))
        {
            println("at = $", at);
        }

    }
}
