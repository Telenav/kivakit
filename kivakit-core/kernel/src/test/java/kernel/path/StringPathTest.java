////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.path;

import com.telenav.kivakit.core.kernel.language.paths.StringPath;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

@SuppressWarnings("SameParameterValue")
public class StringPathTest
{
    @Test
    public void testParse()
    {
        // Relative paths
        ensureEqual("", slashPath("").toString());
        ensureEqual("a", slashPath("a").toString());
        ensureEqual("a/b", slashPath("a/b").toString());
        ensureEqual("a/b", dotPath("a.b").toString());
        ensureEqual("a.b", dotPath("a.b").withSeparator(".").toString());
        ensureEqual("a.b", slashPath("a/b").withSeparator(".").toString());

        // Absolute paths
        ensureEqual("/a/b", absoluteSlashPath("/a/b").toString());
        ensureEqual(":a:b", StringPath.parseStringPath(":a:b", ":", ":").withSeparator(":").toString());
    }

    private StringPath absoluteSlashPath(final String path)
    {
        return StringPath.parseStringPath(path, "/", "/");
    }

    private StringPath dotPath(final String path)
    {
        return StringPath.parseStringPath(path, "\\.");
    }

    private StringPath slashPath(final String path)
    {
        return StringPath.parseStringPath(path, "/");
    }
}
