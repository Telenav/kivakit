////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.modules;

import com.telenav.kivakit.core.kernel.language.modules.Modules;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import org.junit.Test;

import java.nio.file.Path;
import java.util.HashSet;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ConstantConditions")
public class ModulesTest
{
    @Test
    public void testAllNestedResources()
    {
        final var a = PackagePath.parsePackagePath(getClass(), "resources.a");
        final var resources = Modules.allNestedResources(a);
        ensureEqual(resources.size(), 2);
        final var filenames = new HashSet<Path>();
        filenames.add(resources.get(0).fileNameAsJavaPath());
        filenames.add(resources.get(1).fileNameAsJavaPath());
        ensure(filenames.contains(Path.of("a.txt")));
        ensure(filenames.contains(Path.of("b.txt")));
        ensure(resources.get(0).size().isGreaterThan(Bytes._0));
        ensure(resources.get(1).size().isGreaterThan(Bytes._0));
    }

    @Test
    public void testNestedResources()
    {
        final var a = PackagePath.parsePackagePath(getClass(), "resources.a");
        final var resources = Modules.nestedResources(a, resource -> resource.fileNameAsJavaPath().equals(Path.of("b.txt")));
        ensureEqual(resources.size(), 1);
        ensureEqual(resources.get(0).fileNameAsJavaPath(), Path.of("b.txt"));
        ensure(resources.get(0).size().isGreaterThan(Bytes._0));
    }

    @Test
    public void testResource()
    {
        final var _package = PackagePath.packagePath(getClass());
        final var path = _package.withChild("resources/a/a.txt");
        final var resource = Modules.resource(path);
        ensureNotNull(resource);
        ensureEqual(resource.fileNameAsJavaPath(), Path.of("a.txt"));
        ensureEqual(resource.packagePath(), _package.withChild("resources").withChild("a"));
        ensure(resource.size().isGreaterThan(Bytes._0));
    }

    @Test
    public void testResources()
    {
        final var a = PackagePath.parsePackagePath(getClass(), "resources.a");
        final var resources = Modules.resources(a);
        ensureEqual(resources.size(), 1);
        ensureEqual(resources.get(0).fileNameAsJavaPath(), Path.of("a.txt"));
        ensure(resources.get(0).size().isGreaterThan(Bytes._0));
    }
}
