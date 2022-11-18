package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourcePath;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.packaging.PackageReference.packageReference;
import static com.telenav.kivakit.filesystem.Folder.folder;

/**
 * A folder on the classpath containing {@link ClasspathResource}s.
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ClasspathFolder extends ClasspathResourceFolder
{
    private static final Map<URI, ClasspathFolder> folders = new HashMap<>();

    /**
     * Returns the {@link ClasspathFolder} for the given classpath {@link URI}
     */
    public static synchronized ClasspathFolder classpathFolder(Listener ignored, URI uri)
    {
        var folder = folders.get(uri);
        if (folder == null)
        {
            folders.put(uri, new ClasspathFolder(folder(uri)));
        }
        return folder;
    }

    /** The underlying folder */
    private final Folder folder;

    @Override
    public ResourcePath path()
    {
        return folder.path();
    }

    private ClasspathFolder(Folder folder)
    {
        this.folder = folder;

        for (var resource : folder.resources())
        {
            if (!resource.endsWith(".class"))
            {
                add(new ClasspathResource()
                        .parent(this)
                        .packageReference(packageReference(resource.path()))
                        .created(resource.createdAt())
                        .lastModified(resource.lastModified())
                        .size(resource.sizeInBytes())
                        .uri(resource.uri()));
            }
        }
    }
}
