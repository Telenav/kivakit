////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression.archive;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.interfaces.code.Callback;
import com.telenav.kivakit.core.kernel.interfaces.code.CheckedCode;
import com.telenav.kivakit.core.kernel.interfaces.io.ByteSized;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.progress.reporters.Progress;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.language.values.version.VersionedObject;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceArchive;
import com.telenav.kivakit.core.serialization.core.SerializationSession;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.resource.compression.archive.ZipArchive.Mode.READ;
import static com.telenav.kivakit.core.serialization.core.SerializationSession.Type.RESOURCE;

/**
 * A wrapper around the JDK zip filesystem that makes it easier to use. A {@link ZipArchive} can be created with {@link
 * #open(File, ProgressReporter, Mode)}, which returns the open zip archive or null if the operation fails. Adds the
 * ability to save and load objects into zip entries using a {@link SerializationSession}. A zip archive contains
 * entries wrapped by {@link ZipEntry} and a {@link ZipArchive} is {@link Iterable} to make it easy to enumerate zip
 * entries:
 * <pre>
 * var archive = new ZipArchive("test.zip");
 * for (var entry : archive)
 * {
 *     System.out.println(entry);
 * }
 * </pre>
 *
 * <p><b>Adding Files</b></p>
 *
 * <p>
 * Files can be added to the archive with {@link #add(List)}. To do this, the zip file must be opened in {@link
 * Mode#WRITE}.
 * </p>
 *
 * <p><b>Saving</b></p>
 *
 * <ul>
 *     <li>{@link #save(String, Resource)} - Saves the given resources into the named zip entry</li>
 *     <li>{@link #save(SerializationSession, String, VersionedObject)} - Writes the object to the given entry</li>
 *     <li>{@link #saveEntry(String, Callback)} - Saves to the named entry calling the callback to do the work</li>
 * </ul>
 *
 * <p><b>Loading</b></p>
 *
 * <ul>
 *     <li>{@link #load(SerializationSession, String)} - Loads the object from the named entry using the given serializer </li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see ZipEntry
 * @see ProgressReporter
 * @see SerializationSession
 * @see Resource
 */
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@UmlRelation(label = "stores", referent = ZipEntry.class)
@UmlRelation(label = "opens for access", referent = ZipArchive.Mode.class)
@UmlExcludeSuperTypes({ AutoCloseable.class, Iterable.class })
@LexakaiJavadoc(complete = true)
public final class ZipArchive implements Iterable<ZipEntry>, AutoCloseable, ByteSized
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return True if the resource is a zip archive
     */
    public static boolean is(final File file)
    {
        if (file.isRemote())
        {
            LOGGER.warning("Cannot read remote zip archives: $", file);
        }
        else
        {
            if (file.exists())
            {
                final var zip = open(file, ProgressReporter.NULL, READ);
                if (zip != null)
                {
                    zip.close();
                    return true;
                }
            }
        }
        return false;
    }

    public static ZipArchive open(final File file, final ProgressReporter reporter, final Mode mode)
    {
        if (file.isRemote())
        {
            LOGGER.warning("Cannot read remote zip archives: $", file);
        }
        else
        {
            final var filesystem = filesystem(file, mode);
            if (filesystem != null)
            {
                final var zip = new ZipArchive(filesystem, reporter, file);
                if (mode == ZipArchive.Mode.READ)
                {
                    reporter.steps(zip.bytes());
                }
                return zip;
            }
        }
        return null;
    }

    /**
     * The access mode for a zip archive. Zip archives can be read or written to, but not both.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramResourceArchive.class)
    @LexakaiJavadoc(complete = true)
    public enum Mode
    {
        READ,
        WRITE
    }

    private FileSystem filesystem;

    private final File file;

    private final ProgressReporter reporter;

    public ZipArchive(final FileSystem filesystem, final ProgressReporter reporter, final File file)
    {
        this.reporter = reporter;
        assert file != null;
        assert filesystem != null;

        this.file = file.materialized(Progress.create(LOGGER));
        this.filesystem = filesystem;
    }

    /**
     * Adds the given list of files to this archive, calling the progress reporter as the operation proceeds
     */
    public void add(final List<File> files)
    {
        for (final var file : files)
        {
            save(file.fileName().name(), file);
        }
    }

    @Override
    public Bytes bytes()
    {
        final var bytes = new MutableCount();
        for (final var entry : entries(Pattern.compile(".*")))
        {
            bytes.plus(entry.bytes());
        }
        return Bytes.bytes(bytes.asLong());
    }

    @Override
    public synchronized void close()
    {
        IO.close(filesystem);
        filesystem = null;
    }

    /**
     * @return The entries that match the given pattern
     */
    public List<ZipEntry> entries(final Pattern compile)
    {
        final var entries = new ArrayList<ZipEntry>();
        for (final var entry : this)
        {
            if (compile.matcher(entry.path().fileName().name()).matches())
            {
                entries.add(entry);
            }
        }
        return entries;
    }

    /**
     * If the archive resource is a file, uses {@link ZipFile} to access the zip entry directly. Otherwise, resorts to
     * scanning the entire input stream for the entry.
     *
     * @return The entry, if any, for the given name
     */
    public synchronized ZipEntry entry(final String pathname)
    {
        final var path = CheckedCode.of(() -> filesystem.getPath(pathname)).orNull();
        if (path != null)
        {
            return new ZipEntry(this, path);
        }
        LOGGER.warning("Couldn't find zip entry '$'", pathname);
        return null;
    }

    /**
     * @return The zip file for this archive, if any
     */
    public File file()
    {
        return file;
    }

    /**
     * @return The zip entries in this archive
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<ZipEntry> iterator()
    {
        final var files = CheckedCode.of(() -> Files.walk(filesystem.getPath("/"))).orNull();
        return files == null ? null : files
                .filter(path -> !Files.isDirectory(path))
                .map(path -> new ZipEntry(this, path))
                .iterator();
    }

    /**
     * @return The versioned object loaded from the given archive entry using the given serialization
     */
    public synchronized <T> VersionedObject<T> load(final SerializationSession session, final String entryName)
    {
        try
        {
            final var entry = entry(entryName);
            if (entry != null)
            {
                try (final var input = entry.openForReading(reporter))
                {
                    if (input != null)
                    {
                        session.open(RESOURCE, KivaKit.get().version(), input);
                        return session.read();
                    }
                }
                session.close();
            }
        }
        catch (final Exception e)
        {
            fail(e, "Unable to load object '$' from $", entryName, this);
        }
        return null;
    }

    /**
     * @return The archive resource
     */
    public Resource resource()
    {
        return file;
    }

    /**
     * Saves the given resource under the given entry name. The caller must close the archive when done saving objects.
     *
     * @param entryName The entry name
     * @param resource The resource to save
     */
    public void save(final String entryName, final Resource resource)
    {
        saveEntry(entryName, output ->
        {
            final var input = resource.openForReading(reporter);
            IO.copy(input, output);
            IO.close(input);
        });
    }

    /**
     * Saves the given object to the zip archive under the given entry name using the given serialization
     */
    public <T> void save(final SerializationSession session,
                         final String entryName,
                         final VersionedObject<T> object)
    {
        saveEntry(entryName, output ->
        {
            try
            {
                session.open(RESOURCE, KivaKit.get().version(), output);
                session.write(object);
            }
            finally
            {
                session.flush();
            }
        });
    }

    /**
     * Saves to the given archive entry, calling the callback with the output stream to write to
     */
    public void saveEntry(final String entryName, final Callback<OutputStream> onWrite)
    {
        try
        {
            final var entry = entry(entryName);
            if (entry != null)
            {
                try (final var output = entry.openForWriting())
                {
                    onWrite.onCallback(output);
                }
                entry.close();
            }
        }
        catch (final IOException e)
        {
            fail("Unable to save object to zip entry '$'", entryName);
        }
    }

    @Override
    public String toString()
    {
        return file.path().toString();
    }

    private static FileSystem filesystem(final File file, final Mode mode)
    {
        final var fileUri = file.asJavaFile().toURI();
        final var uri = URI.create("jar:" + fileUri);
        switch (mode)
        {
            case WRITE:
            {
                final var environment = new VariableMap<String>();
                environment.put("create", "true");
                return CheckedCode.of(() -> FileSystems.newFileSystem(uri, environment)).orNull();
            }

            case READ:
            {
                if (file.exists())
                {
                    final var filesystem = CheckedCode.of(() -> FileSystems.getFileSystem(uri)).orNull();
                    if (filesystem != null)
                    {
                        return filesystem;
                    }
                    return CheckedCode.of(() -> FileSystems.newFileSystem(uri, new VariableMap<>())).orNull();
                }
                return null;
            }

            default:
                return null;
        }
    }
}
