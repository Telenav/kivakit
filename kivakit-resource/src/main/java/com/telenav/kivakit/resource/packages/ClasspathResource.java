package com.telenav.kivakit.resource.packages;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramModule;
import com.telenav.kivakit.core.language.packaging.PackageReference;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import io.github.classgraph.Resource;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.UNSTABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.language.Try.tryCatch;
import static com.telenav.kivakit.core.language.packaging.PackageReference.parsePackageReference;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Paths.pathWithoutOptionalSuffix;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;
import static com.telenav.kivakit.resource.ResourcePath.parseResourcePath;
import static java.nio.file.Files.readAttributes;

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
 * @see ClasspathResourceFolder
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@UmlClassDiagram(diagram = DiagramModule.class)
@TypeQuality(stability = UNSTABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ClasspathResource implements ResourcePathed
{
    public static ClasspathResource classpathResource(Listener listener, Resource resource)
    {
        return tryCatch(listener, () ->
                {
                    // Get the resource path,
                    var path = resource.getPath();

                    // turn that into a package reference,
                    var packagePath = pathWithoutOptionalSuffix(path, '/');
                    var reference = parsePackageReference(listener, packagePath);

                    // get the classpath element as a Java file,
                    var element = resource.getClasspathElementFile();

                    // and the resource file inside that element,
                    var file = new File(element, path);

                    // then get the created and last modified time (note that the ClasspathResourceFolder for this
                    // resource has already been created and the appropriate filesystem has been mounted),
                    var attributes = readAttributes(Path.of(resource.getURI()), BasicFileAttributes.class);
                    var createdAt = epochMilliseconds(attributes.creationTime().toMillis());
                    var lastModified = epochMilliseconds(attributes.lastModifiedTime().toMillis());

                    return new ClasspathResource()
                            .packageReference(reference)
                            .created(createdAt)
                            .lastModified(lastModified)
                            .size(bytes(resource.getLength()))
                            .uri(resource.getURI());
                },
                "Invalid resource: $", resource);
    }

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

    /**
     * Returns the time at which the resource was created
     */
    public Time created()
    {
        return created;
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
     * Returns the file name of this resource
     */
    @Override
    public FileName fileName()
    {
        return resourcePath().fileName();
    }

    @Override
    public int hashCode()
    {
        return uri.hashCode();
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
     * Returns the parent folder containing this classpath resource
     */
    public ClasspathResourceFolder parent()
    {
        return parent;
    }

    @Override
    @FormatProperty
    public ResourcePath path()
    {
        return resourcePath();
    }

    /**
     * Returns a {@link ResourcePath} for this resource
     */
    public ResourcePath resourcePath()
    {
        return parseResourcePath(throwingListener(), uri());
    }

    /**
     * Returns the size of the resource in bytes
     */
    @FormatProperty
    public Bytes size()
    {
        return size;
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

    ClasspathResource created(Time created)
    {
        this.created = created;
        return this;
    }

    ClasspathResource lastModified(Time lastModified)
    {
        this.lastModified = lastModified;
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
}
