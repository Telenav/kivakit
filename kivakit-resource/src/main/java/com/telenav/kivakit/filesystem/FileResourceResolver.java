package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.filesystem.File.file;
import static com.telenav.kivakit.filesystem.FilePath.filePath;
import static com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader.fileSystem;

/**
 * Resolves {@link ResourceIdentifier}s that are file paths into file {@link Resource}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class FileResourceResolver implements ResourceResolver
{
    @Override
    public boolean accepts(@NotNull ResourceIdentifier identifier)
    {
        if (identifier.identifier().matches("^(http|https|classpath):.*"))
        {
            return false;
        }
        return fileSystem(this, filePath(identifier.identifier())) != null;
    }

    @Override
    public Resource resolve(@NotNull ResourceIdentifier identifier)
    {
        return file(identifier.identifier());
    }
}
