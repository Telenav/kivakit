package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Strip.stripLeading;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.resource.packages.Package.packageForPath;
import static com.telenav.kivakit.resource.packages.PackagePath.packagePath;

/**
 * Resolves package resource identifiers that are of the form "classpath:/a/b/c" into {@link ResourceFolder}s (in the
 * form of {@link Package}s).
 *
 * @author jonathanl (shibo)
 * @see Resource#resolveResource(Listener, String)
 * @see Resource#resolveResource(Listener, ResourceIdentifier)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public class PackageResourceFolderResolver implements ResourceFolderResolver
{
    public static final String SCHEME = "classpath:";

    @Override
    public boolean accepts(@NotNull ResourceFolderIdentifier identifier)
    {
        return identifier.identifier().startsWith(SCHEME);
    }

    @Override
    public Package resolve(@NotNull ResourceFolderIdentifier identifier)
    {
        var filepath = parseFilePath(this, stripLeading(identifier.identifier(), SCHEME));
        return packageForPath(throwingListener(), packagePath(filepath));
    }
}
