////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.language.module;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramModule;
import com.telenav.kivakit.core.io.Nio;
import com.telenav.kivakit.core.language.reflection.property.IncludeProperty;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.path.StringPath.stringPath;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;

/**
 * A resource in a module having the following attributes. {@link ModuleResource}s can be found with the methods in
 * {@link Modules}.
 * <ul>
 *     <li>{@link #uri()} - The {@link URI} to the resource</li>
 *     <li>{@link #packageReference()} - The package where the resource resides</li>
 *     <li>{@link #javaPath()} - The full package path to the resource</li>
 *     <li>{@link #fileNameAsJavaPath()} - The filename of the resource</li>
 *     <li>{@link #lastModified()} - The time of the last modification to the resource</li>
 *     <li>{@link #size()} - The size of the resource</li>
 * </ul>
 *
 * <p><b>NOTE</b></p>
 *
 * <p>
 * <i>This class is in the kivakit-core project and so it is not a resource in the sense of resources that
 * implement the Resource interface in the kivakit-resource project (which depends on core).</i>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Modules
 */
@UmlClassDiagram(diagram = DiagramModule.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class ModuleResource
{
    /**
     * Finds a module resource for a {@link ModuleReference} and URI.
     *
     * @param listener The listener to receive any problems
     * @param reference The module reference
     * @param uri The URI where the module resides
     * @return The module resource
     */
    public static ModuleResource moduleResource(Listener listener, ModuleReference reference, URI uri)
    {
        var location = reference.location();
        if (location.isPresent())
        {
            var scheme = uri.getScheme();

            switch (scheme)
            {
                case "file":
                {
                    var javaPath = Path.of(uri);
                    if (!Files.isDirectory(javaPath))
                    {
                        var filepath = stringPath(javaPath);
                        var folder = stringPath(location.get());
                        var testFolder = folder.withoutLast().withChild("test-classes");
                        if (filepath.startsWith(folder))
                        {
                            var relativePath = filepath.withoutPrefix(folder);
                            var _package = PackageReference.packageReference(relativePath.withoutLast());
                            return new ModuleResource(_package, uri);
                        }
                        if (filepath.startsWith(testFolder))
                        {
                            var relativePath = filepath.withoutPrefix(testFolder);
                            var _package = PackageReference.packageReference(relativePath.withoutLast());
                            return new ModuleResource(_package, uri);
                        }
                    }
                    break;
                }

                case "jar":
                case "zip":
                {
                    //noinspection resource
                    Nio.filesystem(listener, uri);
                    var _package = PackageReference.packageReference(stringPath(uri));
                    return new ModuleResource(_package, uri);
                }

                case "jrt":
                    break;

                default:
                    listener.warning("Unknown scheme $", scheme);
                    break;
            }
        }
        return null;
    }

    /** The time this module resource was created */
    private Time created;

    /** The time this module resource was last modified */
    private Time lastModified;

    /** A reference to the package where the resource resides */
    private final PackageReference packageReference;

    /** The size of the resource */
    private Bytes size;

    /** The resource URI */
    private final URI uri;

    protected ModuleResource(PackageReference packageReference, URI uri)
    {
        this.packageReference = packageReference;
        this.uri = uri;
        try
        {
            var path = Path.of(uri);
            size = bytes(Files.size(path));
            lastModified = epochMilliseconds(Files.getLastModifiedTime(path).toMillis());
            created = epochMilliseconds(Files.readAttributes(path, BasicFileAttributes.class).creationTime().toMillis());
        }
        catch (IOException ignored)
        {
        }
    }

    /**
     * Returns the time at which the resource was created
     */
    public Time created()
    {
        return created;
    }

    /**
     * Returns the resource filename as a Java {@link Path}
     */
    @IncludeProperty
    public Path fileNameAsJavaPath()
    {
        return Path.of(fileName());
    }

    /**
     * Returns the Java {@link Path} to the resource
     */
    public Path javaPath()
    {
        return stringPath(uri).asJavaPath();
    }

    /**
     * Returns the time at which the resource was last modified
     */
    @IncludeProperty
    public Time lastModified()
    {
        return lastModified;
    }

    /**
     * Returns a reference to the package where the resource resides
     */
    @IncludeProperty
    public PackageReference packageReference()
    {
        return packageReference;
    }

    /**
     * Returns the size of the resource in bytes
     */
    @IncludeProperty
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
    public URI uri()
    {
        return uri;
    }

    private String fileName()
    {
        return stringPath(uri).last();
    }
}
