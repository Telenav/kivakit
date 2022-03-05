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

package com.telenav.kivakit.resource.compression.archive;

import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.io.Nio;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.kivakit.core.version.VersionedObject;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.project.lexakai.DiagramResourceArchive;
import com.telenav.kivakit.serialization.core.SerializationSession;
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

import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.Mode.READ;

/**
 * A wrapper around the JDK zip filesystem that makes it easier to use. A {@link ZipArchive} can be created with {@link
 * #open(Listener, File, ProgressReporter, Mode)}, which returns the open zip archive or null if the operation fails.
 * Adds the ability to save and load objects into zip entries using a {@link SerializationSession}. A zip archive
 * contains entries wrapped by {@link ZipEntry} and a {@link ZipArchive} is {@link Iterable} to make it easy to
 * enumerate zip entries:
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
 * Files can be added to the archive with {@link #add(List, ProgressReporter)}. To do this, the zip file must be opened
 * in {@link Mode#WRITE}.
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
public final class ZipArchive implements
        Iterable<ZipEntry>,
        AutoCloseable,
        ByteSized
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * @return True if the resource is a zip archive
     */
    public static boolean is(Listener listener, File file)
    {
        if (file.isRemote())
        {
            LOGGER.warning("Cannot read remote zip archives: $", file);
        }
        else
        {
            if (file.exists())
            {
                var zip = open(listener, file, ProgressReporter.none(), READ);
                if (zip != null)
                {
                    zip.close();
                    return true;
                }
            }
        }
        return false;
    }

    public static ZipArchive open(Listener listener, File file, Mode mode)
    {
        return open(listener, file, ProgressReporter.none(), mode);
    }

    public static ZipArchive open(Listener listener, File file, ProgressReporter reporter, Mode mode)
    {
        if (file.isRemote())
        {
            listener.warning("Cannot read remote zip archives: $", file);
        }
        else
        {
            var filesystem = filesystem(listener, file, mode);
            if (filesystem != null)
            {
                var zip = new ZipArchive(filesystem, reporter, file);
                if (mode == ZipArchive.Mode.READ)
                {
                    reporter.steps(zip.sizeInBytes());
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

    private final File file;

    private FileSystem filesystem;

    private final ProgressReporter reporter;

    public ZipArchive(FileSystem filesystem, File file)
    {
        this(filesystem, ProgressReporter.none(), file);
    }

    public ZipArchive(FileSystem filesystem, ProgressReporter reporter, File file)
    {
        this.reporter = reporter;
        assert file != null;
        assert filesystem != null;

        this.file = file.materialized(BroadcastingProgressReporter.create(LOGGER));
        this.filesystem = filesystem;
    }

    public void add(List<File> files)
    {
        add(files, ProgressReporter.none());
    }

    /**
     * Adds the given list of files to this archive, calling the progress reporter as the operation proceeds
     */
    public void add(List<File> files, ProgressReporter reporter)
    {
        for (var file : files)
        {
            save(file.fileName().name(), file);
            reporter.next();
        }
    }

    @Override
    public synchronized void close()
    {
        Nio.close(filesystem);
        IO.close(filesystem);
        filesystem = null;
    }

    /**
     * @return The entries that match the given pattern
     */
    public List<ZipEntry> entries(Pattern compile)
    {
        var entries = new ArrayList<ZipEntry>();
        for (var entry : this)
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
    public synchronized ZipEntry entry(String pathname)
    {
        var path = UncheckedCode.of(() -> filesystem.getPath(pathname)).orNull();
        if (path != null)
        {
            return new ZipEntry(filesystem, path);
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
        var files = UncheckedCode.of(() -> Files.walk(filesystem.getPath("/"))).orNull();
        return files == null ? null : files
                .filter(path -> !Files.isDirectory(path))
                .map(path -> new ZipEntry(filesystem, path))
                .iterator();
    }

    /**
     * @return The versioned object loaded from the given archive entry using the given serialization
     */
    public synchronized <T> VersionedObject<T> load(SerializationSession session, String entryName)
    {
        try
        {
            var entry = entry(entryName);
            if (entry != null)
            {
                try (var input = entry.openForReading(reporter))
                {
                    if (input != null)
                    {
                        session.open(SerializationSession.Type.RESOURCE, KivaKit.get().projectVersion(), input);
                        return session.read();
                    }
                }
                session.close();
            }
        }
        catch (Exception e)
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
    public ZipArchive save(String entryName, Resource resource)
    {
        saveEntry(entryName, output ->
        {
            var input = resource.openForReading(reporter);
            IO.copy(input, output);
            IO.close(input);
        });
        return this;
    }

    /**
     * Saves the given object to the zip archive under the given entry name using the given serialization
     */
    public <T> void save(SerializationSession session,
                         String entryName,
                         VersionedObject<T> object)
    {
        saveEntry(entryName, output ->
        {
            try
            {
                session.open(SerializationSession.Type.RESOURCE, KivaKit.get().projectVersion(), output);
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
    public void saveEntry(String entryName, Callback<OutputStream> onWrite)
    {
        try
        {
            var entry = entry(entryName);
            if (entry != null)
            {
                try (var output = entry.openForWriting())
                {
                    onWrite.onCallback(output);
                }
                entry.close();
            }
        }
        catch (IOException e)
        {
            fail("Unable to save object to zip entry '$'", entryName);
        }
    }

    @Override
    public Bytes sizeInBytes()
    {
        var bytes = new MutableCount();
        for (var entry : entries(Pattern.compile(".*")))
        {
            bytes.plus(entry.sizeInBytes());
        }
        return Bytes.bytes(bytes.asLong());
    }

    @Override
    public String toString()
    {
        return file.path().toString();
    }

    private static FileSystem filesystem(Listener listener, File file, Mode mode)
    {
        var fileUri = file.asJavaFile().toURI();
        var uri = URI.create("jar:" + fileUri);
        switch (mode)
        {
            case WRITE:
            {
                var environment = new VariableMap<String>();
                environment.put("create", "true");
                return UncheckedCode.of(() -> Nio.filesystem(listener, uri, environment)).orNull();
            }

            case READ:
            {
                if (file.exists())
                {
                    var filesystem = UncheckedCode.of(() -> FileSystems.getFileSystem(uri)).orNull();
                    if (filesystem != null)
                    {
                        return filesystem;
                    }
                    return UncheckedCode.of(() -> FileSystems.newFileSystem(uri, new VariableMap<>())).orNull();
                }
                return null;
            }

            default:
                return null;
        }
    }
}
