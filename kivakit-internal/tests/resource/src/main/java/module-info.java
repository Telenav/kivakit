import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.kivakit.resource.spi.ResourceResolver;

/**
 * This module-info does nothing, but must be present or the maven-compile-plugin will refuse to compile the tests
 * (which contains the version that is actually used).
 */
open module kivakit.internal.tests.resource
{
    uses ResourceResolver;
    uses ResourceFolderResolver;

    requires kivakit.core;
    requires kivakit.internal.testing;
    requires kivakit.resource;
}
