package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramModule;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.net.URI;
import java.nio.file.Path;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;

/**
 * A resource on the classpath having the following attributes:
 *
 * <p><b>Attributes</b></p>
 *
 * <ul>
 *     <li>{@link #uri()} - The {@link URI} to the resource</li>
 *     <li>{@link #packageReference()} - The package where the resource resides</li>
 *     <li>{@link #javaPath()} - The full package path to the resource</li>
 *     <li>{@link #lastModified()} - The time of the last modification to the resource</li>
 *     <li>{@link #size()} - The size of the resource</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Classpath
 * @see ClasspathJar
 */
@SuppressWarnings("unused") @UmlClassDiagram(diagram = DiagramModule.class)
@CodeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ClasspathResource implements ResourcePathed
{
    /** The time this module resource was created */
    private Time created;

    /** The time this module resource was last modified */
    private Time lastModified;

    /** The size of the resource */
    private Bytes size;

    /** The resource URI */
    private URI uri;

    /** The package */
    private PackageReference packageReference;

    /** The parent containing this resource */
    private ClasspathResourceFolder parent;

    @Override
    public int hashCode()
    {
        return uri.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ClasspathResource that)
        {
            return uri.equals(that.uri);
        }
        return false;
    }

    /**
     * Returns the time at which the resource was created
     */
    public Time created()
    {
        return created;
    }

    /**
     * Returns the parent folder containing this classpath resource
     */
    public ClasspathResourceFolder parent()
    {
        return parent;
    }

    /**
     * Returns the Java {@link Path} to the resource
     */
    public Path javaPath()
    {
        return resourcePath().asJavaPath();
    }

    /**
     * Returns the time at which the resource was last modified
     */
    @FormatProperty
    public Time lastModified()
    {
        return lastModified;
    }

    /**
     * Returns a reference to the package where the resource resides
     */
    @FormatProperty
    public PackageReference packageReference()
    {
        return packageReference;
    }

    /**
     * Returns the size of the resource in bytes
     */
    @FormatProperty
    public Bytes size()
    {
        return size;
    }

    /**
     * Returns a {@link ResourcePath} for this resource
     */
    public ResourcePath resourcePath()
    {
        return parseResourcePath(throwingListener(), uri().getPath());
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    /**
     * Returns the URI of the resource
     */
    @Override
    public URI uri()
    {
        return uri;
    }

    /**
     * Returns the file name of this resource
     */
    @Override
    public FileName fileName()
    {
        return resourcePath().fileName();
    }

    @Override
    public ResourcePath path()
    {
        return resourcePath();
    }

    ClasspathResource created(Time created)
    {
        this.created = created;
        return this;
    }

    ClasspathResource packageReference(PackageReference packageReference)
    {
        this.packageReference = packageReference;
        return this;
    }

    ClasspathResource parent(ClasspathResourceFolder parent)
    {
        this.parent = parent;
        return this;
    }

    ClasspathResource size(Bytes size)
    {
        this.size = size;
        return this;
    }

    ClasspathResource uri(URI uri)
    {
        this.uri = uri;
        return this;
    }

    ClasspathResource lastModified(Time lastModified)
    {
        this.lastModified = lastModified;
        return this;
    }
}
