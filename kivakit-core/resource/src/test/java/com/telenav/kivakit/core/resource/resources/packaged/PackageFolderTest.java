////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.packaged;

import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

/**
 * @author jonathanl (shibo)
 */
public class PackageFolderTest extends UnitTest
{
    @Test
    public void testResources()
    {
        if (!isQuickTest())
        {
            final var folder = new Package(PackagePath.packagePath(getClass()));
            int textFiles = 0;
            for (final var ignored : folder.resources(Extension.TXT::matches))
            {
                textFiles++;
            }
            ensureEqual(textFiles, 2);
        }
    }
}
