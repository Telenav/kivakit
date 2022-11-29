package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.filesystem.Folder;
import io.github.classgraph.Resource;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.path.StringPath.parseStringPath;

/**
 * A package or folder on the classpath containing resources
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ClasspathResourceFolder
{
    /** Map from classpath element URI to {@link ClasspathResourceFolder} */
    private static final Map<URI, ClasspathResourceFolder> folders = new HashMap<>();

    /** A list of the resources in this folder */
    private final ObjectList<ClasspathResource> resources = list();

    /** The filesystem where the folder exists */
    private FileSystem filesystem;

    /**
     * Returns a {@link ClasspathResourceFolder} for the given URI, or null if the URI does not represent a JAR or
     * {@link Folder} on the classpath.
     *
     * @param listener The listener to call with any problems
     * @param resource The classpath resource folder
     * @return The resource folder
     */
    public static synchronized ClasspathResourceFolder classpathResourceFolder(Listener listener, Resource resource)
    {
        // Get the URI to this resource's classpath element,
        var uri = resource.getClasspathElementURI();

        try
        {
            // the path to the resource within that element,
            var path = parseStringPath(listener, resource.getPath(), "/");

            // and return the corresponding folder.
            var folder = folders.get(uri);
            if (folder == null)
            {
                folder = new ClasspathResourceFolder();
                folder.uri = uri;
                folder.path = path;
                if (uri.getPath().endsWith(".jar"))
                {
                    uri = URI.create("jar:" + uri);
                    try
                    {
                        folder.filesystem = FileSystems.newFileSystem(uri, new HashMap<>());
                    }
                    catch (FileSystemAlreadyExistsException ignored)
                    {
                        folder.filesystem = FileSystems.getFileSystem(uri);
                    }
                }
                folders.put(uri, folder);
            }
            return folder;
        }
        catch (Exception e)
        {
            listener.problem("Invalid resource folder for: $", resource);
        }
        return null;
    }

    private URI uri;

    private StringPath path;

    public StringPath path()
    {
        return path;
    }

    public URI uri()
    {
        return uri;
    }

    /**
     * The set of resources in this folder
     */
    public final ObjectList<ClasspathResource> resources()
    {
        return resources;
    }

    /**
     * Adds the given resource to this folder model
     *
     * @param resource The resource to add
     */
    final void add(ClasspathResource resource)
    {
        resources.add(resource);
    }
}
