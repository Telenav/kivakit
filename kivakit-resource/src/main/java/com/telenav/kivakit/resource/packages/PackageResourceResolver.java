package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;

/**
 * Resolves {@link ResourceIdentifier}s of the form "classpath:/a/b/resource.txt" into {@link Resource}s by creating a
 * {@link PackageResource} for the identifier.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class PackageResourceResolver implements ResourceResolver
{
    public static final String SCHEME = "classpath:";

    @Override
    public boolean accepts(@NotNull ResourceIdentifier identifier)
    {
        return identifier.identifier().startsWith(SCHEME);
    }

    @Override
    public Resource resolve(@NotNull ResourceIdentifier identifier)
    {
        var filepath = parseFilePath(this, stripLeading(identifier.identifier(), SCHEME));
        var parent = filepath.parent();
        if (parent != null)
        {
            var packagePath = packagePath(parent);
            return PackageResource.packageResource(throwingListener(), packagePath, filepath.fileName());
        }
        problem("Could not resolve resource: $", identifier);
        return null;
    }
}
