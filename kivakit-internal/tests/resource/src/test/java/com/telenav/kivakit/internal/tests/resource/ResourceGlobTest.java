package com.telenav.kivakit.internal.tests.resource;

import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.resource.packages.PackageTrait;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Ignore;
import org.junit.Test;

import static com.telenav.kivakit.resource.Extension.TXT;

/**
 * Test resource globbing, similar to UNIX glob matching
 *
 * @author jonathanl (shibo)
 */
@Ignore
public class ResourceGlobTest extends UnitTest implements PackageTrait, JavaTrait
{
    @Test
    public void test()
    {
        var here = packageForThis();
        for (var at : here.resources(TXT::matches))
        {
            println("at = $", at);
        }
    }
}
