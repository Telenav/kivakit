package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourcePath;

import java.net.URI;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.resource.packages.ClasspathFolder.classpathFolder;
import static com.telenav.kivakit.resource.packages.ClasspathJar.classpathJar;

/**
 * A package or folder on the classpath containing resources
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class ClasspathResourceFolder
{
    private final ObjectList<ClasspathResource> resources = list();

    /**
     * Returns a {@link ClasspathResourceFolder} for the given URI, or null if the URI does not represent a JAR or
     * {@link Folder} on the classpath.
     *
     * @param listener The listener to call with any problems
     * @param uri The URI of the resource folder
     * @return The resource folder
     */
    public static ClasspathResourceFolder classpathResourceFolder(Listener listener, URI uri)
    {
        switch (uri.getScheme())
        {
            case "file" -> classpathFolder(listener, uri);

            case "jar" -> classpathJar(listener, uri);

            default -> listener.problem("Unrecognized resource scheme: $", uri);
        }
        return null;
    }

    public abstract ResourcePath path();

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
    protected final void add(ClasspathResource resource)
    {
        resources.add(resource);
    }
}
