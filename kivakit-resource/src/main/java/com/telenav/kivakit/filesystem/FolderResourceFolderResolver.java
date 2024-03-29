package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.filesystem.Folder.parseFolder;

/**
 * Resolves valid folder paths into {@link ResourceFolder}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FolderResourceFolderResolver implements ResourceFolderResolver
{
    @Override
    public boolean accepts(@NotNull ResourceFolderIdentifier identifier)
    {
        return parseFolder(this, identifier.identifier()) != null;
    }

    @Override
    public Folder resolve(@NotNull ResourceFolderIdentifier identifier)
    {
        return parseFolder(this, identifier.identifier());
    }
}
