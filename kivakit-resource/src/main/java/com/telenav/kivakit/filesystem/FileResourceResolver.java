package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader.fileSystem;

/**
 * Resolves {@link ResourceIdentifier}s that are file paths into file {@link Resource}s.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@CodeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class FileResourceResolver implements ResourceResolver
{
    @Override
    public boolean accepts(@NotNull ResourceIdentifier identifier)
    {
        if (identifier.identifier().matches("^(http|https|classpath):.*"))
        {
            return false;
        }
        return fileSystem(this, parseFilePath(this, identifier.identifier())) != null;
    }

    @Override
    public Resource resolve(@NotNull ResourceIdentifier identifier)
    {
        return File.parseFile(this, identifier.identifier());
    }
}
