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

package com.telenav.kivakit.kernel.language.modules;

import com.telenav.kivakit.kernel.language.paths.Nio;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.strings.formatting.ObjectFormatter;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageModules;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.lang.module.ModuleReference;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A resource in a module having the following attributes. {@link ModuleResource}s can be found with the methods in
 * {@link Modules}.
 * <ul>
 *     <li>{@link #uri()} - The {@link URI} to the resource</li>
 *     <li>{@link #packagePath()} - The package where the resource resides</li>
 *     <li>{@link #javaPath()} - The full package path to the resource</li>
 *     <li>{@link #fileNameAsJavaPath()} - The filename of the resource</li>
 *     <li>{@link #lastModified()} - The time of the last modification to the resource</li>
 *     <li>{@link #size()} - The size of the resource</li>
 * </ul>
 *
 * <p><b>NOTE</b></p>
 *
 * <p>
 * <i>This class is in the kivakit-core-kernel project and so it is not a resource in the sense of resources that
 * implement the Resource interface in the kivakit-core-resource project (which depends on the kernel).</i>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Modules
 */
@UmlClassDiagram(diagram = DiagramLanguageModules.class)
public class ModuleResource
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static ModuleResource moduleResource(final ModuleReference reference, final URI uri)
    {
        final var location = reference.location();
        if (location.isPresent())
        {
            final var scheme = uri.getScheme();

            switch (scheme)
            {
                case "file":
                {
                    final var javaPath = Path.of(uri);
                    if (!Files.isDirectory(javaPath))
                    {
                        final var filepath = StringPath.stringPath(javaPath);
                        final var folder = StringPath.stringPath(location.get());
                        final var testFolder = folder.withoutLast().withChild("test-classes");
                        if (filepath.startsWith(folder))
                        {
                            final var relativePath = filepath.withoutPrefix(folder);
                            final var _package = PackagePath.packagePath(relativePath.withoutLast());
                            return new ModuleResource(_package, uri);
                        }
                        if (filepath.startsWith(testFolder))
                        {
                            final var relativePath = filepath.withoutPrefix(testFolder);
                            final var _package = PackagePath.packagePath(relativePath.withoutLast());
                            return new ModuleResource(_package, uri);
                        }
                    }
                    break;
                }

                case "jar":
                case "zip":
                {
                    Nio.filesystem(LOGGER, uri);
                    final var _package = PackagePath.packagePath(StringPath.stringPath(uri));
                    return new ModuleResource(_package, uri);
                }

                case "jrt":
                    break;

                default:
                    LOGGER.warning("Unknown scheme $", scheme);
                    break;
            }
        }
        return null;
    }

    private final PackagePath _package;

    private final URI uri;

    private Bytes size;

    private Time lastModified;

    protected ModuleResource(final PackagePath _package, final URI uri)
    {
        this._package = _package;
        this.uri = uri;
        try
        {
            final var path = Path.of(uri);
            size = Bytes.bytes(Files.size(path));
            lastModified = Time.milliseconds(Files.getLastModifiedTime(path).toMillis());
        }
        catch (final IOException ignored)
        {
        }
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
    public PackagePath packagePath()
    {
        return _package;
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
