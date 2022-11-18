package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.compression.archive.ZipArchive;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.packaging.PackageReference.packageReference;
import static com.telenav.kivakit.core.path.StringPath.stringPath;
import static com.telenav.kivakit.filesystem.File.file;
import static com.telenav.kivakit.resource.ResourcePath.resourcePath;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.READ;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;

/**
 * Holds the resources found in a JAR, identified by a URI.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ClasspathJar extends ClasspathResourceFolder
{
    private static final Map<URI, ClasspathJar> jars = new HashMap<>();

    /**
     * Returns the classpath JAR for the given URI
     *
     * @param listener The listener to call with any problems
     * @param uri The URI to the JAR on the classpath
     * @return The classpath JAR
     */
    public static synchronized ClasspathJar classpathJar(Listener listener, URI uri)
    {
        var jar = jars.get(uri);
        if (jar == null)
        {
            var file = file(listener, uri);
            if (file != null)
            {
                try (var archive = zipArchive(listener, file, READ))
                {
                    if (archive != null)
                    {
                        jars.put(uri, new ClasspathJar(uri, archive));
                    }
                    else
                    {
                        listener.problem("Invalid JAR archive: $", file);
                    }
                }
            }
            else
            {
                listener.problem("URI is not a jar file: $", uri);
            }
        }

        return jar;
    }

    /** The URI to this JAR */
    private final URI uri;

    @Override
    public ResourcePath path()
    {
        return resourcePath(stringPath(uri));
    }

    private ClasspathJar(URI uri, ZipArchive archive)
    {
        this.uri = uri;
        for (var entry : archive)
        {
            add(new ClasspathResource()
                    .parent(this)
                    .packageReference(packageReference(entry.path()))
                    .created(entry.createdAt())
                    .lastModified(entry.lastModified())
                    .size(entry.sizeInBytes())
                    .uri(uri));
        }
    }
}
