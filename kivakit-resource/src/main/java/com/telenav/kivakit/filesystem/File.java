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

import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.Paths;
import com.telenav.kivakit.core.string.Strip;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.local.LocalFile;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceResolver;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.collections.set.ObjectSet.objectSet;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.messaging.Listener.none;
import static com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader.fileSystem;

/**
 * File abstraction that adds integrates files with the KivaKit resource mini-framework and adds a variety of useful
 * methods. File system service providers for this abstraction are in <i>com.telenav.kivakit.filesystem.local</i> (the
 * local filesystem provider) and in the <i>kivakit-filesystems</i> module.
 *
 * <p>
 * Files can be created with several static factory methods, including:
 *
 * <ul>
 *     <li>{@link #parseFile(Listener listener, String)}</li>
 *     <li>{@link #file(Listener, URI)}</li>
 *     <li>{@link #file(FilePath)}</li>
 *     <li>{@link #file(java.io.File)}</li>
 * </ul>
 *
 * <p>
 * Static methods that produce switch and argument builders are available, and files add the following groups of
 * methods to the base methods provided by {@link BaseWritableResource}.
 * </p>
 *
 * <p><b>Path Methods</b></p>
 *
 * <ul>
 *     <li>{@link #absolute()}</li>
 *     <li>{@link #normalized()}</li>
 *     <li>{@link #messageSource()}</li>
 *     <li>{@link #relativeTo(Folder)}</li>
 *     <li>{@link #root()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)}</li>
 *     <li>{@link #renameTo(Resource)}</li>
 *     <li>{@link #safeCopyFrom(Resource, CopyMode, ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #ensureReadable()}</li>
 *     <li>{@link #ensureWritable()}</li>
 *     <li>{@link #isFile()}</li>
 *     <li>{@link #isFolder()}</li>
 *     <li>{@link #isNewerThan(File)}</li>
 *     <li>{@link #isOlderThan(File)}</li>
 * </ul>
 *
 * <p><b>Conversion Methods</b></p>
 *
 * <ul>
 *     <li>{@link #asFolder()}</li>
 *     <li>{@link #asJavaFile()}</li>
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
 *     <li>{@link #withoutKnownExtensions()}</li>
 *     <li>{@link #withoutOverwriting()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SameParameterValue")
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@LexakaiJavadoc(complete = true)
public class File extends BaseWritableResource implements FileSystemObject
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static PosixFilePermission[] ACCESS_ALL = { PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_READ,
            PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_READ,
            PosixFilePermission.OTHERS_WRITE, PosixFilePermission.OTHERS_EXECUTE };

    public static final PosixFilePermission[] ACCESS_775 = { PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_READ,
            PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_READ,
            PosixFilePermission.OTHERS_EXECUTE };

    private static long temporaryFileNumber = System.currentTimeMillis();

    public static File file(Listener listener, URI uri)
    {
        // Ensure our many preconditions
        if (!uri.isAbsolute())
        {
            Ensure.illegalArgument("URI is not absolute");
        }
        if (uri.isOpaque())
        {
            Ensure.illegalArgument("URI is not hierarchical");
        }
        var scheme = uri.getScheme();
        if (!"file".equalsIgnoreCase(scheme))
        {
            Ensure.illegalArgument("URI scheme is not \"file\"");
        }
        if (uri.getAuthority() != null)
        {
            Ensure.illegalArgument("URI has an authority component");
        }
        if (uri.getFragment() != null)
        {
            Ensure.illegalArgument("URI has a fragment component");
        }
        if (uri.getQuery() != null)
        {
            Ensure.illegalArgument("URI has a query component");
        }
        var path = uri.getPath();
        if ("".equals(path))
        {
            Ensure.illegalArgument("URI path component is empty");
        }
        path = path.replaceFirst("^/", "");

        var filesystem = fileSystem(listener, FilePath.parseFilePath(listener, path));

        var service = ensureNotNull(filesystem)
                .fileService(FilePath.parseFilePath(listener, path));

        return new File(service);
    }

    public static File file(java.io.File file)
    {
        return parseFile(Listener.throwing(), file.getAbsolutePath());
    }

    public static File file(FilePath path)
    {
        var filesystem = fileSystem(Listener.throwing(), path);
        return new File(ensureNotNull(filesystem).fileService(path));
    }

    public static ArgumentParser.Builder<File> fileArgumentParser(Listener listener, String description)
    {
        return ArgumentParser.builder(File.class)
                .converter(new File.Converter(LOGGER))
                .description(description);
    }

    public static ArgumentParser.Builder<FileList> fileListArgumentParser(Listener listener,
                                                                          String description,
                                                                          Extension extension)
    {
        return ArgumentParser.builder(FileList.class)
                .converter(new FileList.Converter(LOGGER, extension))
                .description(description);
    }

    public static SwitchParser.Builder<FileList> fileListSwitchParser(Listener listener,
                                                                      String name,
                                                                      String description,
                                                                      Extension extension)
    {
        return SwitchParser.builder(FileList.class)
                .name(name)
                .converter(new FileList.Converter(LOGGER, extension))
                .description(description);
    }

    public static SwitchParser.Builder<FilePath> filePathSwitchParser(Listener listener,
                                                                      String name,
                                                                      String description)
    {
        return SwitchParser.builder(FilePath.class)
                .name(name)
                .converter(new FilePath.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<File> fileSwitchParser(Listener listener,
                                                              String name,
                                                              String description)
    {
        return SwitchParser.builder(File.class)
                .name(name)
                .converter(new File.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<File> inputFileSwitchParser(Listener listener)
    {
        return fileSwitchParser(listener, "input", "The input file to process");
    }

    public static SwitchParser.Builder<File> outputFile(Listener listener)
    {
        return fileSwitchParser(listener, "output", "The output file to target");
    }

    public static File parseFile(Listener listener, String path, VariableMap<String> variables)
    {
        return parseFile(listener, variables.expand(path));
    }

    public static File parseFile(Listener listener, String path)
    {
        // If there is a KivaKit scheme, like "s3", "hdfs" or "java",
        var scheme = Paths.head(path, ":");
        if (scheme != null)
        {
            // parse the rest of the path into a FilePath,
            var filePath = FilePath.parseFilePath(listener, Paths.tail(path, ":"));

            // then prepend the KivaKit scheme to the list of schemes in the parsed FilePath,
            var schemes = filePath.schemes().copy().prepend(scheme);

            // and create the file.
            return File.file(filePath.withSchemes(schemes));
        }

        return File.file(FilePath.parseFilePath(listener, path));
    }

    /**
     * Converts to and from {@link File} objects
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<File>
    {
        public Converter(Listener listener)
        {
            super(listener, File::parseFile);
        }
    }

    /**
     * Resolves {@link ResourceIdentifier}s that are file paths into file {@link Resource}s.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramResourceService.class)
    @LexakaiJavadoc(complete = true)
    public static class Resolver implements ResourceResolver
    {
        @Override
        public boolean accepts(ResourceIdentifier identifier)
        {
            if (identifier.identifier().matches("^(http|https|classpath):.*"))
            {
                return false;
            }
            return fileSystem(none(), FilePath.parseFilePath(this, identifier.identifier())) != null;
        }

        @Override
        public Resource resolve(ResourceIdentifier identifier)
        {
            return File.parseFile(this, identifier.identifier());
        }
    }

    @UmlAggregation(label = "delegates to")
    private final FileService service;

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    File(FileService file)
    {
        service = file;
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    private File(File that)
    {
        super(that);
        service = that.service;
    }

    /**
     * @return This file with an absolute path
     */
    public File absolute()
    {
        return File.file(path().absolute());
    }

    @Override
    public Duration age()
    {
        return created().elapsedSince();
    }

    /**
     * @return This file as a folder
     */
    public Folder asFolder()
    {
        return new Folder(service.folderService());
    }

    /**
     * @return This file as a {@link java.io.File}
     */
    public java.io.File asJavaFile()
    {
        return service.asJavaFile();
    }

    @Override
    public ObjectSet<Can> can()
    {
        return objectSet(Can.RENAME);
    }

    /**
     * Change the access permissions of this file
     *
     * @param permissions The permissions to apply
     * @return True if permissions were successfully changed
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(PosixFilePermission... permissions)
    {
        return service.chmod(permissions);
    }

    @Override
    public Time created()
    {
        return service.created();
    }

    /**
     * @return True if this file was deleted
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
            chmod(PosixFilePermission.OTHERS_WRITE);
            fatal("Resource " + this + " is not writable");
        }
        return this;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof File)
        {
            var that = (File) object;
            return path().equals(that.path());
        }
        return false;
    }

    /**
     * @return True if this file exists
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

    /**
     * @return True if this is, in fact, a file and not a folder
     */
    public boolean isFile()
    {
        return service.isFile();
    }

    /**
     * @return True if this is actually a folder instead of a file
     */
    public boolean isFolder()
    {
        return service.isFolder();
    }

    /**
     * @return True if this file is newer than the given file
     */
    public boolean isNewerThan(File that)
    {
        return service.lastModified().isAfter(that.service.lastModified());
    }

    public boolean isNewerThan(Duration duration)
    {
        return age().isLessThan(duration);
    }

    /**
     * @return True if this file exists and has at least one byte in it
     */
    public boolean isNonEmpty()
    {
        return exists() && isLargerThan(Bytes._0);
    }

    /**
     * @return True if this file is older than the given file
     */
    public boolean isOlderThan(File that)
    {
        return service.lastModified().isBefore(that.service.lastModified());
    }

    public boolean isOlderThan(Duration duration)
    {
        return age().isGreaterThan(duration);
    }

    /**
     * @return True if this file can be read
     */
    @Override
    public boolean isReadable()
    {
        return service.isReadable();
    }

    /**
     * @return True if this file is not on the local host
     */
    @Override
    public boolean isRemote()
    {
        return service.isRemote();
    }

    /**
     * @return True if this file can be written to
     */
    @Override
    public Boolean isWritable()
    {
        return service.isWritable();
    }

    /**
     * @return The last time of modification of this file
     */
    @Override
    public Time lastModified()
    {
        var lastModified = service.lastModified();
        trace("Last modified time of $ is $", this, lastModified);
        return lastModified;
    }

    /**
     * Sets the last modified timestamp on this file
     */
    @Override
    public boolean lastModified(Time modified)
    {
        return service.lastModified(modified);
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
    public File materialized(ProgressReporter reporter)
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
     * @return This file with a normalized path
     * @see ResourcePath#normalized()
     */
    public File normalized()
    {
        return File.file(service.path().normalized());
    }

    @Override
    public InputStream onOpenForReading()
    {
        return service.onOpenForReading();
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return service.onOpenForWriting();
    }

    /**
     * @return The parent folder that contains this file
     */
    @Override
    public Folder parent()
    {
        return new Folder(service.parentService());
    }

    /**
     * @return The path to this file
     */
    @Override
    public FilePath path()
    {
        return service.path();
    }

    @Override
    public File print(String text)
    {
        return (File) super.print(text);
    }

    @Override
    public File println(String text)
    {
        return (File) super.println(text);
    }

    @Override
    public <R extends Resource, F extends ResourceFolder<?>> R relativeTo(final F folder)
    {
        return File.file(service.relativePath(folder.service()).withoutTrailingSlash());
    }

    /**
     * Renames this file to the given file. Both files must be on the same filesystem for this method to succeed.
     *
     * @return True if this file was renamed to the given file
     */
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(Resource that)
    {
        trace("Rename $ to $", this, that);
        return service.renameTo(((File) that).service);
    }

    /**
     * @return The root folder containing this file
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
     */
    public void safeCopyFrom(Resource resource, CopyMode mode, ProgressReporter reporter)
    {
        resource.safeCopyTo(this, mode, reporter);
    }

    /**
     * @return The size of this file
     */
    @Override
    public Bytes sizeInBytes()
    {
        return service.sizeInBytes();
    }

    @Override
    public String toString()
    {
        return service.toString();
    }

    /**
     * @return This file with the given basename (but the same path and extension)
     */
    public File withBaseName(String name)
    {
        var file = File.file(path().parent().withChild(name));
        if (extension() != null)
        {
            return file.withExtension(extension());
        }
        return file;
    }

    /**
     * @return This file with the given {@link Charset}
     */
    public File withCharset(Charset charset)
    {
        var file = new File(this);
        file.charset(charset);
        return file;
    }

    /**
     * @return This file with the given compression / decompression codec
     */
    public File withCodec(Codec codec)
    {
        var file = new File(this);
        file.codec(codec);
        return file;
    }

    /**
     * @return This file with the given extension
     */
    public File withExtension(Extension extension)
    {
        return parseFile(this, path().toString() + extension);
    }

    /**
     * @return This file without any extensions at all, taking into account compound extensions like ".tar.gz"
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
     * @return This file with no extension
     */
    public File withoutExtension()
    {
        var extension = extension();
        if (extension != null)
        {
            var withoutExtension = Paths.withoutOptionalSuffix(path().toString(), '.');
            return parseFile(this, withoutExtension);
        }
        return this;
    }

    /**
     * @return This file with any known extensions removed
     */
    @UmlExcludeMember
    public File withoutKnownExtensions()
    {
        var file = this;
        boolean removedOne;
        do
        {
            removedOne = false;
            for (var extension : Extension.known())
            {
                if (file.fileName().endsWith(extension))
                {
                    file = parseFile(this, Strip.ending(path().toString(), extension.toString()));
                    removedOne = true;
                }
            }
        }
        while (removedOne);
        return file;
    }

    /**
     * @return A file that can be written to without overwriting data
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

    File temporary(Extension extension)
    {
        return Folder.kivakitTemporary().file("temp-" + temporaryFileNumber++ + extension);
    }
}
