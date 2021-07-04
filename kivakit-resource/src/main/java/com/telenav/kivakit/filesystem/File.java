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
import com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader;
import com.telenav.kivakit.filesystem.local.LocalFile;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.data.validation.ensure.reporters.ValidationFailure;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.language.strings.Paths;
import com.telenav.kivakit.kernel.language.strings.Strip;
import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
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

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.fail;

/**
 * File abstraction that adds integrates files with the KivaKit resource mini-framework and adds a variety of useful
 * methods. File system service providers for this abstraction are in <i>com.telenav.kivakit.filesystem.local</i> (the
 * local filesystem provider) and in the <i>kivakit-filesystems</i> module.
 *
 * <p>
 * Files can be created with several static factory methods, including:
 *
 * <ul>
 *     <li>{@link #parse(String)}</li>
 *     <li>{@link #of(URI)}</li>
 *     <li>{@link #of(FilePath)}</li>
 *     <li>{@link #of(java.io.File)}</li>
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
 *     <li>{@link #parentBroadcaster()}</li>
 *     <li>{@link #relativeTo(Folder)}</li>
 *     <li>{@link #root()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)}</li>
 *     <li>{@link #renameTo(File)}</li>
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
 *     <li>{@link #expanded(VariableMap)}</li>
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

    public static ArgumentParser.Builder<File> fileArgumentParser(final String description)
    {
        return ArgumentParser.builder(File.class)
                .converter(new File.Converter(LOGGER))
                .description(description);
    }

    public static ArgumentParser.Builder<FileList> fileListArgumentParser(final String description,
                                                                          final Extension extension)
    {
        return ArgumentParser.builder(FileList.class)
                .converter(new FileList.Converter(LOGGER, extension))
                .description(description);
    }

    public static SwitchParser.Builder<FileList> fileListSwitchParser(final String name,
                                                                      final String description,
                                                                      final Extension extension)
    {
        return SwitchParser.builder(FileList.class)
                .name(name)
                .converter(new FileList.Converter(LOGGER, extension))
                .description(description);
    }

    public static SwitchParser.Builder<FilePath> filePathSwitchParser(final String name, final String description)
    {
        return SwitchParser.builder(FilePath.class)
                .name(name)
                .converter(new FilePath.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<File> fileSwitchParser(final String name, final String description)
    {
        return SwitchParser.builder(File.class)
                .name(name)
                .converter(new File.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<File> inputFileSwitchParser()
    {
        return fileSwitchParser("input", "The input file to process");
    }

    public static File of(final URI uri)
    {
        // Ensure our many preconditions
        if (!uri.isAbsolute())
        {
            fail("URI is not absolute");
        }
        if (uri.isOpaque())
        {
            fail("URI is not hierarchical");
        }
        final var scheme = uri.getScheme();
        if (!"file".equalsIgnoreCase(scheme))
        {
            fail("URI scheme is not \"file\"");
        }
        if (uri.getAuthority() != null)
        {
            fail("URI has an authority component");
        }
        if (uri.getFragment() != null)
        {
            fail("URI has a fragment component");
        }
        if (uri.getQuery() != null)
        {
            fail("URI has a query component");
        }
        var path = uri.getPath();
        if ("".equals(path))
        {
            fail("URI path component is empty");
        }
        path = path.replaceFirst("^/", "");
        return new File(FileSystemServiceLoader.fileSystem(FilePath.parseFilePath(path)).fileService(FilePath.parseFilePath(path)));
    }

    public static File of(final java.io.File file)
    {
        return parse(file.getAbsolutePath());
    }

    public static File of(final FilePath path)
    {
        return new File(FileSystemServiceLoader.fileSystem(path).fileService(path));
    }

    public static SwitchParser.Builder<File> outputFile()
    {
        return fileSwitchParser("output", "The output file to target");
    }

    public static File parse(final String path)
    {
        return File.of(FilePath.parseFilePath(path));
    }

    public static synchronized File temporary(final Extension extension)
    {
        return Folder.kivakitTemporary().file("temp-" + temporaryFileNumber++ + extension);
    }

    /**
     * Converts to and from {@link File} objects
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<File>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected File onToValue(final String value)
        {
            return File.parse(value);
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
        public boolean accepts(final ResourceIdentifier identifier)
        {
            if (identifier.identifier().startsWith("classpath:"))
            {
                return false;
            }
            return FileSystemServiceLoader.fileSystem(FilePath.parseFilePath(identifier.identifier())) != null;
        }

        @Override
        public Resource resolve(final ResourceIdentifier identifier)
        {
            return File.parse(identifier.identifier());
        }
    }

    @UmlAggregation(label = "delegates to")
    private final FileService service;

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    File(final FileService file)
    {
        service = file;
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    private File(final File that)
    {
        super(that);
        service = that.service;
    }

    /**
     * @return This file with an absolute path
     */
    public File absolute()
    {
        return File.of(path().absolute());
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

    /**
     * Change the access permissions of this file
     *
     * @param permissions The permissions to apply
     * @return True if permissions were successfully changed
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return service.chmod(permissions);
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
     * Reports a validation failure through {@link Ensure#fail()} if this file is not readable. By default failure
     * throws a {@link ValidationFailure} exception.
     */
    public File ensureReadable()
    {
        ensure(isReadable());
        return this;
    }

    /**
     * Reports a validation failure through {@link Ensure#fail()} if this file is not writable. By default failure
     * throws a {@link ValidationFailure} exception.
     */
    public File ensureWritable()
    {
        service.parent().mkdirs();
        if (!service.parent().exists())
        {
            fail("Parent folder of " + this + " does not exist");
        }
        if (!isWritable())
        {
            chmod(PosixFilePermission.OTHERS_WRITE);
            fail("Resource " + this + " is not writable");
        }
        return this;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof File)
        {
            final var that = (File) object;
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

    /**
     * Expands the path of this file with the given variables. For example:
     * <pre>
     * File.parse("data/${project-version}/data.txt")
     *     .withVariables(properties())
     * </pre>
     * Might resolve to a file with the path <i>data/1.0.7/data.txt</i>.
     *
     * @return This file with the given variables interpolated into the path.
     */
    @UmlExcludeMember
    public File expanded(final VariableMap<?> variables)
    {
        return File.parse(variables.expand(toString()));
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
    public boolean isNewerThan(final File that)
    {
        return service.isNewerThan(that.service);
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
    public boolean isOlderThan(final File that)
    {
        return service.isOlderThan(that.service);
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
        final var lastModified = service.lastModified();
        trace("Last modified time of $ is $", this, lastModified);
        return lastModified;
    }

    /**
     * Sets the last modified timestamp on this file
     */
    @Override
    public boolean lastModified(final Time modified)
    {
        return service.lastModified(modified);
    }

    /**
     * Materializes this file: if it file is remote, it is copied to the local filesystem and that file is returned.
     * Materialization is necessary because some files, such as zip and JAR archives cannot be accessed as resources
     * since they are <i>random access</i>. It is necessary to materialize such files before accessing them. Another
     * reason to materialize a file is to improve performance if a file is to be read more than once.
     *
     * @return This file, or if it is remote, a file on the local filesystem that has the contents of this file
     */
    @Override
    public File materialized(final ProgressReporter reporter)
    {
        if (service.isRemote())
        {
            trace("Materializing $", this);
            final var resource = service.materialized(reporter);
            if (resource instanceof File)
            {
                return (File) resource;
            }
            if (resource instanceof LocalFile)
            {
                return new File((LocalFile) resource);
            }
            return fail("Materialized resource must be either a File or a LocalFile");
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
        return File.of(service.path().normalized());
    }

    @Override
    public InputStream onOpenForReading()
    {
        return service.openForReading();
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return service.openForWriting();
    }

    /**
     * @return The parent folder that contains this file
     */
    public Folder parent()
    {
        return new Folder(service.parent());
    }

    /**
     * @return The path to this file
     */
    @Override
    public FilePath path()
    {
        return service.path();
    }

    /**
     * @return This file with a path relative to the given folder
     */
    public File relativeTo(final Folder folder)
    {
        return File.of(service.relativePath(folder.service()));
    }

    /**
     * Renames this file to the given file. Both files must be on the same filesystem for this method to succeed.
     *
     * @return True if this file was renamed to the given file
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(final File that)
    {
        trace("Rename $ to $", this, that);
        return service.renameTo(that.service);
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
    public void safeCopyFrom(final Resource resource, final CopyMode mode, final ProgressReporter reporter)
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
    public File withBaseName(final String name)
    {
        final var file = File.of(path().parent().withChild(name));
        if (extension() != null)
        {
            return file.withExtension(extension());
        }
        return file;
    }

    /**
     * @return This file with the given {@link Charset}
     */
    public File withCharset(final Charset charset)
    {
        final var file = new File(this);
        file.charset(charset);
        return file;
    }

    /**
     * @return This file with the given compression / decompression codec
     */
    public File withCodec(final Codec codec)
    {
        final var file = new File(this);
        file.codec(codec);
        return file;
    }

    /**
     * @return This file with the given extension
     */
    public File withExtension(final Extension extension)
    {
        return File.parse(path().toString() + extension);
    }

    /**
     * @return This file without any extensions at all, taking into account compound extensions like ".tar.gz"
     */
    @UmlExcludeMember
    public File withoutCompoundExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            final var pathString = path().toString();
            var dot = -1;
            for (var index = pathString.length() - 1; index > 0; index--)
            {
                final var c = pathString.charAt(index);
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
                return File.parse(pathString.substring(0, dot));
            }
        }
        return this;
    }

    /**
     * @return This file with no extension
     */
    public File withoutExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            final var withoutExtension = Paths.withoutOptionalSuffix(path().toString(), '.');
            return File.parse(withoutExtension);
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
            for (final var extension : Extension.known())
            {
                if (file.fileName().endsWith(extension))
                {
                    file = File.parse(Strip.ending(path().toString(), extension.toString()));
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
            file = File.parse(withoutExtension() + "-" + count + extension());
            count++;
        }
        return file;
    }
}
