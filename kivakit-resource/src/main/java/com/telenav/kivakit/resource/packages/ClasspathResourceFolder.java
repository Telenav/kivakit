package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.filesystem.Folder;
import io.github.classgraph.Resource;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.language.Hash.hashMany;
import static com.telenav.kivakit.core.os.Console.console;
import static com.telenav.kivakit.core.path.StringPath.parseStringPath;

/**
 * A package or folder on the classpath containing resources
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ClasspathResourceFolder
{
    /** Map from classpath element URI to {@link ClasspathResourceFolder} */
    private static final Map<String, ClasspathResourceFolder> folders = new HashMap<>();

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
            var packagePath = PackagePath.packagePath(path.withoutLast());
            var key = uri + "::" + packagePath;
            var folder = folders.get(key);
            if (folder == null)
            {
                folder = new ClasspathResourceFolder();
                folder.classpathRoot = uri;
                folder.packagePath = packagePath;
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
                folders.put(key, folder);
            }
            return folder;
        }
        catch (Exception e)
        {
            listener.problem("Invalid resource folder for: $", resource);
        }
        return null;
    }

    /** A list of the resources in this folder */
    private final ObjectList<ClasspathResource> resources = list();

    /** The filesystem where the folder exists */
    private FileSystem filesystem;

    /** The URI to the classpath root */
    private URI classpathRoot;

    /** The package path within the classpath root */
    @FormatProperty
    private PackagePath packagePath;

    /**
     * Returns the URI of the classpath root containing the given package
     */
    public URI classpathRoot()
    {
        return classpathRoot;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof ClasspathResourceFolder that)
        {
            return classpathRoot.equals(that.classpathRoot)
                && packagePath.equals(that.packagePath);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashMany(classpathRoot, packagePath);
    }

    /**
     * Returns the package path to this folder within the classpath root
     */
    public PackagePath packagePath()
    {
        return packagePath;
    }

    /**
     * Returns this classpath folder as a package reference
     */
    public PackageReference packageReference()
    {
        return PackageReference.packageReference(packagePath());
    }

    /**
     * The set of resources in this folder
     */
    public final ObjectList<ClasspathResource> resources()
    {
        return resources;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
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
