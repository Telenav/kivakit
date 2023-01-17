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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.MessageException;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.local.LocalFile;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.string.Paths.pathHead;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.filesystem.Folders.kivakitTemporaryFolder;
import static com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader.fileSystem;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.GROUP_READ;
import static java.nio.file.attribute.PosixFilePermission.GROUP_WRITE;
import static java.nio.file.attribute.PosixFilePermission.OTHERS_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OTHERS_READ;
import static java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

/**
 * File abstraction that adds integrates files with the KivaKit resource mini-framework and adds a variety of useful
 * methods. File system service providers for this abstraction are in <i>com.telenav.kivakit.filesystem.local</i> (the
 * local filesystem provider) and in the <i>kivakit-filesystems</i> module.
 *
 * <p><b>Creation</b></p>
 *
 * <p>
 * Files can be created with several static factory methods, including:
 * </p>
 *
 * <ul>
 *     <li>{@link #parseFile(Listener listener, String)}</li>
 *     <li>{@link #parseFile(Listener, String, VariableMap)}</li>
 *     <li>{@link #file(Listener, URI)}</li>
 *     <li>{@link #file(Listener, FilePath)}</li>
 *     <li>{@link #file(Listener, java.io.File)}</li>
 * </ul>
 *
 * <p><b>Path-Related Methods</b></p>
 *
 * <ul>
 *     <li>{@link #absolute()}</li>
 *     <li>{@link #normalized()}</li>
 *     <li>{@link #messageSource()}</li>
 *     <li>{@link #relativeTo(ResourceFolder)}</li>
 *     <li>{@link #root()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #age()}</li>
 *     <li>{@link #createdAt()}</li>
 *     <li>{@link #lastModified(Time)}</li>
 *     <li>{@link #lastModified()}</li>
 *     <li>{@link #parent()}</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #root()}</li>
 *     <li>{@link #sizeInBytes()}</li>
 * </ul>
 *
 * <p><b>Materialization</b></p>
 *
 * <ul>
 *     <li>{@link #dematerialize()}</li>
 *     <li>{@link #isMaterializable()}</li>
 *     <li>{@link #materialized(ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)}</li>
 *     <li>{@link #delete()}</li>
 *     <li>{@link #saveText(String)}</li>
 *     <li>{@link #renameTo(Resource)}</li>
 *     <li>{@link #safeCopyFrom(Resource, WriteMode, ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #can()}</li>
 *     <li>{@link #can(Action)}</li>
 *     <li>{@link #ensureExists()}</li>
 *     <li>{@link #ensureReadable()}</li>
 *     <li>{@link #ensureWritable()}</li>
 *     <li>{@link #exists()}</li>
 *     <li>{@link #isFile()}</li>
 *     <li>{@link #isFolder()}</li>
 *     <li>{@link #isNewerThan(Duration)}</li>
 *     <li>{@link #isNewerThan(File)}</li>
 *     <li>{@link #isOlderThan(Duration)}</li>
 *     <li>{@link #isOlderThan(File)}</li>
 *     <li>{@link #isOlderThan(Resource)}</li>
 *     <li>{@link #isReadable()}</li>
 *     <li>{@link #isRemote()}</li>
 *     <li>{@link #isWritable()}</li>
 * </ul>
 *
 * <p><b>Conversion Methods</b></p>
 *
 * <ul>
 *     <li>{@link #asFolder()}</li>
 *     <li>{@link #asJavaFile()}</li>
 *     <li>{@link #asJavaPath()}</li>
 *     <li>{@link #asWritable()}</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <ul>
 *     <li>{@link #withBaseName(String)}</li>
 *     <li>{@link #withCharset(Charset)}</li>
 *     <li>{@link #withCodec(Codec)}</li>
 *     <li>{@link #withExtension(Extension)}</li>
 *     <li>{@link #withoutExtension()}</li>
 *     <li>{@link #withoutCompoundExtension()}</li>
 *     <li>{@link #withoutAllKnownExtensions()}</li>
 *     <li>{@link #withoutOverwriting()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SameParameterValue", "unused" })
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTED)
public class File extends BaseWritableResource implements FileSystemObject
{
    public static PosixFilePermission[] ACCESS_ALL = { OWNER_READ,
        OWNER_WRITE, OWNER_EXECUTE, GROUP_READ,
        GROUP_WRITE, GROUP_EXECUTE, OTHERS_READ,
        OTHERS_WRITE, OTHERS_EXECUTE };

    public static final PosixFilePermission[] ACCESS_775 = { OWNER_READ,
        OWNER_WRITE, OWNER_EXECUTE, GROUP_READ,
        GROUP_WRITE, GROUP_EXECUTE, OTHERS_READ,
        OTHERS_EXECUTE };

    private static long temporaryFileNumber = currentTimeMillis();

    /**
     * Returns a file for the given URI
     *
     * @param listener The listener to call with any problems
     */
    public static File file(@NotNull Listener listener, @NotNull URI uri)
    {
        // Ensure our many preconditions
        if (!uri.isAbsolute())
        {
            listener.problem("URI is not absolute");
            return null;
        }
        if (uri.isOpaque())
        {
            listener.problem("URI is not hierarchical");
            return null;
        }
        var scheme = uri.getScheme();
        if (!"file".equalsIgnoreCase(scheme))
        {
            listener.problem("URI scheme is not \"file\"");
            return null;
        }
        if (uri.getAuthority() != null)
        {
            listener.problem("URI has an authority component");
            return null;
        }
        if (uri.getFragment() != null)
        {
            listener.problem("URI has a fragment component");
            return null;
        }
        if (uri.getQuery() != null)
        {
            listener.problem("URI has a query component");
            return null;
        }
        var path = uri.getPath();
        if ("".equals(path))
        {
            listener.problem("URI path component is empty");
            return null;
        }

        var filepath = parseFilePath(listener, path);
        var filesystem = fileSystem(listener, filepath);
        var service = ensureNotNull(filesystem).fileService(filepath);

        return new File(service);
    }

    /**
     * Returns a file for the given Java file
     *
     * @param listener The listener to call with any problems
     */
    public static File file(@NotNull Listener listener, @NotNull java.io.File file)
    {
        return parseFile(listener, file.getAbsolutePath());
    }

    /**
     * Returns a file for the given path
     *
     * @param listener The listener to call with any problems
     * @param path The path
     */
    public static File file(@NotNull Listener listener, @NotNull FilePath path)
    {
        var filesystem = fileSystem(listener, path);
        return new File(ensureNotNull(filesystem).fileService(path));
    }

    /**
     * Returns a file for the given path
     *
     * @param path The path
     * @return The file
     * @throws MessageException Thrown if the given path cannot be parsed
     */
    public static File file(String path)
    {
        return parseFile(throwingListener(), path);
    }

    /**
     * Parses a path into a {@link File}, interpolating variables from the given variable map
     *
     * @param listener The listener to call with any problems
     * @param path The path to parse
     * @param variables The variables to expand
     * @return The file
     */
    public static File parseFile(@NotNull Listener listener,
                                 @NotNull String path,
                                 @NotNull VariableMap<String> variables)
    {
        return parseFile(listener, variables.expand(path));
    }

    /**
     * Parses a path into a {@link File}
     *
     * @param listener The listener to call with any problems
     * @param path The path to parse
     * @return The file
     */
    public static File parseFile(@NotNull Listener listener, @NotNull String path)
    {
        // If there is a KivaKit scheme, like "s3", "hdfs" or "java",
        var scheme = pathHead(path, ":");
        if (scheme != null)
        {
            // parse the rest of the path into a FilePath,
            var filePath = parseFilePath(listener, Paths.pathTail(path, ":"));

            // then prepend the KivaKit scheme to the list of schemes in the parsed FilePath,
            var schemes = filePath.schemes().copy();
            schemes.prepend(scheme);

            // and create the file.
            return file(listener, filePath.withSchemes(schemes));
        }

        return file(listener, parseFilePath(listener, path));
    }

    /**
     * Returns a temporary file in the {@link Folders#kivakitTemporaryFolder()} folder with the given extension
     *
     * @param extension The extension
     * @return The temporary file
     */
    public static File temporaryFile(@NotNull Extension extension)
    {
        return kivakitTemporaryFolder().file("temp-" + temporaryFileNumber++ + extension);
    }

    @UmlAggregation(label = "delegates to")
    private final FileService service;

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    File(@NotNull FileService file)
    {
        service = listenTo(file);
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    private File(@NotNull File that)
    {
        super(that);
        service = listenTo(that.service);
    }

    /**
     * Returns this file with an absolute path
     */
    public File absolute()
    {
        return file(this, path().asAbsolute());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration age()
    {
        return createdAt().elapsedSince();
    }

    /**
     * Returns this file as a folder
     */
    public Folder asFolder()
    {
        return new Folder(service.folderService());
    }

    /**
     * Returns this file as a {@link java.io.File}
     */
    @Override
    public java.io.File asJavaFile()
    {
        return service.asJavaFile();
    }

    public java.nio.file.Path asJavaPath()
    {
        return asJavaFile().toPath();
    }

    @Override
    public ObjectSet<Action> can()
    {
        return set(Action.DELETE, Action.RENAME);
    }

    /**
     * Change the access permissions of this file
     *
     * @param permissions The permissions to apply
     * @return True if permissions were successfully changed
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(@NotNull PosixFilePermission... permissions)
    {
        return service.chmod(permissions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time createdAt()
    {
        return service.createdAt();
    }

    /**
     * Returns true if this file was deleted
     */
    @Override
    public boolean delete()
    {
        trace("Deleting $", this);
        return service.delete();
    }

    /**
     * Removes any local copy of this file
     */
    @Override
    public void dematerialize()
    {
        trace("De-materializing $", this);
        service.dematerialize();
    }

    /**
     * Reports a validation failure through {@link Ensure#fail()} if this file is not readable. By default, failure
     * throws an exception.
     */
    public File ensureReadable()
    {
        ensure(isReadable());
        return this;
    }

    /**
     * Reports a validation failure through {@link Ensure#fail()} if this file is not writable. By default, failure
     * throws an exception.
     */
    public File ensureWritable()
    {
        service.parentService().mkdirs();
        if (!service.parentService().exists())
        {
            fatal("Parent folder of " + this + " does not exist");
        }
        if (!isWritable())
        {
            chmod(OTHERS_WRITE);
            fatal("Resource " + this + " is not writable");
        }
        return this;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof File that)
        {
            return path().equals(that.path());
        }
        return false;
    }

    /**
     * Returns true if this file exists
     */
    @Override
    public boolean exists()
    {
        return service.exists();
    }

    @Override
    public int hashCode()
    {
        return path().hashCode();
    }

    @Override
    public ResourceIdentifier identifier()
    {
        return new ResourceIdentifier(path().toString());
    }

    /**
     * Returns true if this is, in fact, a file and not a folder
     */
    public boolean isFile()
    {
        return service.isFile();
    }

    /**
     * Returns true if this is actually a folder instead of a file
     */
    public boolean isFolder()
    {
        return service.isFolder();
    }

    /**
     * Returns true if this file is newer than the given file
     */
    public boolean isNewerThan(@NotNull File that)
    {
        return service.lastModified().isAfter(that.service.lastModified());
    }

    public boolean isNewerThan(@NotNull Duration duration)
    {
        return age().isLessThan(duration);
    }

    /**
     * Returns true if this file exists and has at least one byte in it
     */
    public boolean isNonEmpty()
    {
        return exists() && isLargerThan(Bytes._0);
    }

    /**
     * Returns true if this file is older than the given file
     */
    public boolean isOlderThan(@NotNull File that)
    {
        return service.lastModified().isBefore(that.service.lastModified());
    }

    public boolean isOlderThan(@NotNull Duration duration)
    {
        return age().isGreaterThan(duration);
    }

    /**
     * Returns true if this file can be read
     */
    @Override
    public boolean isReadable()
    {
        return service.isReadable();
    }

    /**
     * Returns true if this file is not on the local host
     */
    @Override
    public boolean isRemote()
    {
        return service.isRemote();
    }

    /**
     * Returns true if this file can be written to
     */
    @Override
    public Boolean isWritable()
    {
        return service.isWritable();
    }

    /**
     * Sets the last modified timestamp on this file
     */
    @Override
    public boolean lastModified(@NotNull Time modified)
    {
        return service.lastModified(modified);
    }

    /**
     * Returns the last time of modification of this file
     */
    @Override
    public Time lastModified()
    {
        var lastModified = service.lastModified();
        trace("Last modified time of $ is $", this, lastModified);
        return lastModified;
    }

    /**
     * Materializes this file: if the file is remote, it is copied to the local filesystem and that file is returned.
     * Materialization is necessary because some files, such as zip and JAR archives cannot be accessed as resources
     * since they are <i>random access</i>. It is necessary to materialize such files before accessing them. Another
     * reason to materialize a file is to improve performance if a file is to be read more than once.
     *
     * @return This file, or if it is remote, a file on the local filesystem that has the contents of this file
     */
    @Override
    public File materialized(@NotNull ProgressReporter reporter)
    {
        if (service.isRemote())
        {
            trace("Materializing $", this);
            var resource = service.materialized(reporter);
            if (resource instanceof File)
            {
                return (File) resource;
            }
            if (resource instanceof LocalFile)
            {
                return new File((LocalFile) resource);
            }
            return fatal("Materialized resource must be either a File or a LocalFile");
        }
        else
        {
            return this;
        }
    }

    /**
     * Returns this file with a normalized path
     *
     * @see ResourcePath#normalized()
     */
    public File normalized()
    {
        return file(this, service.path().normalized());
    }

    @Override
    public InputStream onOpenForReading()
    {
        return service.onOpenForReading();
    }

    @Override
    public OutputStream onOpenForWriting(WriteMode mode)
    {
        return service.onOpenForWriting(mode);
    }

    /**
     * Returns the parent folder that contains this file
     */
    @Override
    public Folder parent()
    {
        return new Folder(service.parentService());
    }

    /**
     * Returns the path to this file
     */
    @Override
    public FilePath path()
    {
        return service.path();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File relativeTo(@NotNull ResourceFolder<?> folder)
    {
        var service = ((Folder) folder).service();
        return file(this, this.service.relativePath(service).withoutTrailingSlash());
    }

    /**
     * Renames this file to the given file. Both files must be on the same filesystem for this method to succeed.
     * <p>
     * Returns true if this file was renamed to the given file
     */
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(@NotNull Resource that)
    {
        trace("Rename $ to $", this, that);
        return service.renameTo(((File) that).service);
    }

    /**
     * Returns the root folder containing this file
     */
    public Folder root()
    {
        return new Folder(service.root());
    }

    /**
     * Safely copies to this virtual file from the given resource. Copies this readable resource to the given file
     * safely (ensuring that a corrupted copy of the file never exists). This is done by first copying to a temporary
     * file in the same folder. If the copy operation is successful, the destination file is then removed and the
     * temporary file is renamed to the destination file's name.
     *
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    public void safeCopyFrom(@NotNull Resource resource,
                             @NotNull WriteMode mode,
                             @NotNull ProgressReporter reporter)
    {
        resource.safeCopyTo(this, mode, reporter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return service.sizeInBytes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return service.toString();
    }

    /**
     * Returns this file with the given basename (but the same path and extension)
     */
    public File withBaseName(@NotNull String name)
    {
        var file = file(this, path().parent().withChild(name));
        if (extension() != null)
        {
            return file.withExtension(extension());
        }
        return file;
    }

    /**
     * Returns this file with the given {@link Charset}
     */
    public File withCharset(@NotNull Charset charset)
    {
        var file = new File(this);
        file.charset(charset);
        return file;
    }

    /**
     * Returns this file with the given compression / decompression codec
     */
    public File withCodec(@NotNull Codec codec)
    {
        var file = new File(this);
        file.codec(codec);
        return file;
    }

    /**
     * Returns this file with the given extension
     */
    public File withExtension(@NotNull Extension extension)
    {
        return parseFile(this, path().toString() + extension);
    }

    /**
     * Returns this file with any known extensions removed
     */
    @UmlExcludeMember
    public File withoutAllKnownExtensions()
    {
        var file = this;
        boolean removedOne;
        do
        {
            removedOne = false;
            for (var extension : Extension.allExtensions())
            {
                if (file.fileName().endsWith(extension))
                {
                    file = parseFile(this, Strip.stripEnding(path().toString(), extension.toString()));
                    removedOne = true;
                }
            }
        }
        while (removedOne);
        return file;
    }

    /**
     * Returns this file without any extensions at all, taking into account compound extensions like ".tar.gz"
     */
    @UmlExcludeMember
    public File withoutCompoundExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            var pathString = path().toString();
            var dot = -1;
            for (var index = pathString.length() - 1; index > 0; index--)
            {
                var c = pathString.charAt(index);
                if (c == '.')
                {
                    dot = index;
                }
                if (c == java.io.File.separatorChar)
                {
                    break;
                }
            }
            if (dot > 0)
            {
                return parseFile(this, pathString.substring(0, dot));
            }
        }
        return this;
    }

    /**
     * Returns this file with no extension
     */
    public File withoutExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            var withoutExtension = Paths.pathWithoutOptionalSuffix(path().toString(), '.');
            return parseFile(this, withoutExtension);
        }
        return this;
    }

    /**
     * Returns a file that can be written to without overwriting data
     */
    public File withoutOverwriting()
    {
        var count = 1;
        var file = this;
        while (file.exists())
        {
            file = parseFile(this, withoutExtension() + "-" + count + extension());
            count++;
        }
        return file;
    }
}
