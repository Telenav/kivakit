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

import com.telenav.kivakit.core.io.Nio;
import com.telenav.kivakit.core.language.object.ObjectFormatter;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.lexakai.DiagramModule;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

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
public class ModuleResource
{
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
                        var filepath = StringPath.stringPath(javaPath);
                        var folder = StringPath.stringPath(location.get());
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
                    Nio.filesystem(listener, uri);
                    var _package = PackageReference.packageReference(StringPath.stringPath(uri));
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

    private Time created;

    private Time lastModified;

    private final PackageReference packageReference;

    private Bytes size;

    private final URI uri;

    protected ModuleResource(PackageReference packageReference, URI uri)
    {
        this.packageReference = packageReference;
        this.uri = uri;
        try
        {
            var path = Path.of(uri);
            size = Bytes.bytes(Files.size(path));
            lastModified = Time.epochMilliseconds(Files.getLastModifiedTime(path).toMillis());
            created = Time.epochMilliseconds(Files.readAttributes(path, BasicFileAttributes.class).creationTime().toMillis());
        }
        catch (IOException ignored)
        {
        }
    }

    public Time created()
    {
        return created;
    }

    @KivaKitIncludeProperty
    public Path fileNameAsJavaPath()
    {
        return Path.of(fileName());
    }

    public Path javaPath()
    {
        return StringPath.stringPath(uri).asJavaPath();
    }

    @KivaKitIncludeProperty
    public Time lastModified()
    {
        return lastModified;
    }

    @KivaKitIncludeProperty
    public PackageReference packageReference()
    {
        return packageReference;
    }

    @KivaKitIncludeProperty
    public Bytes size()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public URI uri()
    {
        return uri;
    }

    private String fileName()
    {
        return StringPath.stringPath(uri).last();
    }
}
