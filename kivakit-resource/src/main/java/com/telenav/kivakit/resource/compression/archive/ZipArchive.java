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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.io.Nio;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.core.progress.reporters.ProgressiveOutputStream;
import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.count.MutableCount;
import com.telenav.kivakit.core.version.VersionedObject;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.interfaces.io.Closeable;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceArchive;
import com.telenav.kivakit.resource.serialization.ObjectReader;
import com.telenav.kivakit.resource.serialization.ObjectWriter;
import com.telenav.kivakit.resource.serialization.SerializableObject;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_ENUM_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.READ;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.TYPE;
import static com.telenav.kivakit.resource.serialization.ObjectMetadata.VERSION;

/**
 * A wrapper around the JDK zip filesystem that makes it easier to use. A {@link ZipArchive} can be created with
 * {@link #zipArchive(Listener, File, AccessMode)}, which returns the open zip archive or null if the operation fails.
 * Adds the ability to save and load objects into zip entries using an {@link ObjectWriter}, and {@link ObjectReader},
 * respectively. A zip archive contains entries wrapped by {@link ZipEntry} and a {@link ZipArchive} is {@link Iterable}
 * to make it easy to enumerate zip entries:
 * <pre>
 * var archive = new ZipArchive("test.zip");
 * for (var entry : archive)
 * {
 *     System.out.println(entry);
 * }
 * </pre>
 *
 * <p><b>Opening Zip Files</b></p>
 *
 * <ul>
 *     <li>{@link #isZipArchive(Listener, File)}</li>
 *     <li>{@link #zipArchive(Listener, File, AccessMode)}</li>
 * </ul>
 *
 * <p><b>Reading Entries</b></p>
 *
 * <ul>
 *     <li>{@link #close()}</li>
 *     <li>{@link #entries(Pattern)}</li>
 *     <li>{@link #entry(String)}</li>
 *     <li>{@link #iterator()}</li>
 *     <li>{@link #loadVersionedObject(ObjectReader, String)} - Loads the object from the named entry using the given object reader</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #file()}</li>
 *     <li>{@link #resource()}</li>
 *     <li>{@link #sizeInBytes()}</li>
 * </ul>
 *
 * <p><b>Adding Files</b></p>
 *
 * <p>
 * Files can be added to the archive with {@link #add(Collection, ProgressReporter)}. To do this, the zip file must be
 * opened in {@link AccessMode#WRITE}.
 * </p>
 *
 * <ul>
 *     <li>{@link #add(Collection)}</li>
 *     <li>{@link #add(Collection, ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Saving Entries</b></p>
 *
 * <ul>
 *     <li>{@link #save(String, Resource)} - Saves the given resources into the named zip entry</li>
 *     <li>{@link #save(ObjectWriter, String, VersionedObject)} - Writes the object to the given entry</li>
 *     <li>{@link #saveEntry(String, Callback)} - Saves to the named entry calling the callback to do the work</li>
 *     <li>{@link #close()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see ZipEntry
 * @see ProgressReporter
 * @see ObjectReader
 * @see ObjectWriter
 * @see Resource
 */
@UmlClassDiagram(diagram = DiagramResourceArchive.class)
@UmlRelation(label = "stores", referent = ZipEntry.class)
@UmlRelation(label = "opens for access", referent = ZipArchive.AccessMode.class)
@UmlExcludeSuperTypes({ AutoCloseable.class, Iterable.class })
@ApiQuality(stability = STABLE_DEFAULT_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public final class ZipArchive extends BaseRepeater implements
        Iterable<ZipEntry>,
        Closeable,
        ByteSized
{
    /**
     * Returns true if the resource is a zip archive
     */
    public static boolean isZipArchive(Listener listener, File file)
    {
        if (file.isRemote())
        {
            listener.warning("Cannot read remote zip archives: $", file);
        }
        else
        {
            if (file.exists())
            {
                var zip = zipArchive(listener, file, READ);
                if (zip != null)
                {
                    zip.close();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a {@link ZipArchive} object for the given file and access mode
     *
     * @param listener The listener to call with any problems
     * @param file The zip file
     * @param mode The access mode
     * @return The {@link ZipArchive} object
     */
    public static ZipArchive zipArchive(Listener listener, File file, AccessMode mode)
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
                return new ZipArchive(filesystem, file);
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
    @ApiQuality(stability = STABLE_ENUM_EXTENSIBLE,
                testing = TESTING_NOT_NEEDED,
                documentation = FULLY_DOCUMENTED)
    public enum AccessMode
    {
        READ,
        WRITE
    }

    /** The underlying zip file */
    private final File zipFile;

    /** The Java zip filesystem */
    private FileSystem filesystem;

    /**
     * @param filesystem The zip filesystem
     * @param zipFile The zip file to access
     */
    private ZipArchive(FileSystem filesystem, File zipFile)
    {
        ensureNotNull(filesystem);
        ensureNotNull(zipFile);

        this.zipFile = zipFile.materialized(BroadcastingProgressReporter.createProgressReporter(this));
        this.filesystem = filesystem;
    }

    /**
     * Adds the given files to this archive
     */
    public void add(Collection<File> files)
    {
        add(files, ProgressReporter.none());
    }

    /**
     * Adds the given list of files to this archive, calling the progress reporter as the operation proceeds
     *
     * @param files The files to add
     * @param reporter The progress reporter to call as each file is added
     */
    @SuppressWarnings("resource")
    public void add(Collection<File> files, ProgressReporter reporter)
    {
        for (var file : files)
        {
            save(file.fileName().name(), file);
            reporter.next();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void close()
    {
        Nio.close(this, filesystem);
        IO.close(this, filesystem);
        filesystem = null;
    }

    /**
     * Returns the entries that match the given pattern
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
        var path = UncheckedCode.unchecked(() -> filesystem.getPath(pathname)).orNull();
        if (path != null)
        {
            return new ZipEntry(filesystem, path);
        }
        warning("Couldn't find zip entry '$'", pathname);
        return null;
    }

    /**
     * Returns the zip file for this archive, if any
     */
    public File file()
    {
        return zipFile;
    }

    /**
     * Returns the zip entries in this archive
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<ZipEntry> iterator()
    {
        var files = UncheckedCode.unchecked(() -> Files.walk(filesystem.getPath("/"))).orNull();
        return files == null ? null : files
                .filter(path -> !Files.isDirectory(path))
                .map(path -> new ZipEntry(filesystem, path))
                .iterator();
    }

    /**
     * Returns the versioned object loaded from the given archive entry using the given serialization
     *
     * @param reader The object reader
     * @param entryName The zip file entry to read
     */
    @SuppressWarnings("resource")
    public synchronized <T> VersionedObject<T> loadVersionedObject(ObjectReader reader, String entryName)
    {
        try
        {
            var entry = entry(entryName);
            if (entry != null)
            {
                try (var input = new ProgressiveInputStream(entry.openForReading(reader.reporter()), reader.reporter()))
                {
                    return reader.read(input, StringPath.stringPath(entryName), TYPE, VERSION);
                }
            }
        }
        catch (Exception e)
        {
            fail(e, "Unable to load object '$' from $", entryName, this);
        }
        return null;
    }

    /**
     * Returns the archive resource
     */
    public Resource resource()
    {
        return zipFile;
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
            var input = resource.openForReading();
            IO.copy(this, input, output);
            IO.close(this, input);
        });
        return this;
    }

    /**
     * Saves the given object to the zip archive under the given entry name using the given serialization
     */
    public <T> void save(ObjectWriter writer, String entryName, VersionedObject<T> object)
    {
        saveEntry(entryName, output ->
        {
            try
            {
                writer.write(new ProgressiveOutputStream(output, writer.reporter()),
                        StringPath.stringPath(entryName), new SerializableObject<>(object), TYPE, VERSION);
            }
            finally
            {
                IO.flush(this, output);
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
                    onWrite.call(output);
                }
                entry.close();
            }
        }
        catch (IOException e)
        {
            fail("Unable to save object to zip entry '$'", entryName);
        }
    }

    /**
     * {@inheritDoc}
     */
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
        return zipFile.path().toString();
    }

    private static FileSystem filesystem(Listener listener, File file, AccessMode mode)
    {
        var fileUri = file.asJavaFile().toURI();
        var uri = URI.create("jar:" + fileUri);
        switch (mode)
        {
            case WRITE:
            {
                var environment = new VariableMap<String>();
                environment.put("create", "true");
                return UncheckedCode.unchecked(() -> Nio.filesystem(listener, uri, environment)).orNull();
            }

            case READ:
            {
                if (file.exists())
                {
                    var filesystem = UncheckedCode.unchecked(() -> FileSystems.getFileSystem(uri)).orNull();
                    if (filesystem != null)
                    {
                        return filesystem;
                    }
                    try
                    {
                        return FileSystems.newFileSystem(uri, new VariableMap<>());
                    }
                    catch (Exception e)
                    {
                        listener.problem(e, "Could not create zip filesystem: $", uri);
                    }
                }
                return null;
            }

            default:
                return null;
        }
    }
}
