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

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.commandline.ArgumentParser;
import com.telenav.kivakit.core.commandline.SwitchParser;
import com.telenav.kivakit.core.filesystem.loader.FileSystemServiceLoader;
import com.telenav.kivakit.core.filesystem.local.LocalFile;
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.strings.PathStrings;
import com.telenav.kivakit.core.kernel.language.strings.Strip;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceIdentifier;
import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.core.resource.spi.ResourceResolver;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * File abstraction that adds type safety and various helpful methods. Support for various file systems is implemented
 * in kivakit-filesystems.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
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

    public static ArgumentParser.Builder<File> fileArgument(final String description)
    {
        return ArgumentParser.builder(File.class)
                .converter(new File.Converter(LOGGER))
                .description(description);
    }

    public static ArgumentParser.Builder<FileList> fileArgumentList(final String description, final Extension extension)
    {
        return ArgumentParser.builder(FileList.class)
                .converter(new FileList.Converter(LOGGER, extension))
                .description(description);
    }

    public static SwitchParser.Builder<FileList> fileList(final String name,
                                                          final String description,
                                                          final Extension extension)
    {
        return SwitchParser.builder(FileList.class)
                .name(name)
                .converter(new FileList.Converter(LOGGER, extension))
                .description(description);
    }

    public static SwitchParser.Builder<FilePath> filePathSwitch(final String name, final String description)
    {
        return SwitchParser.builder(FilePath.class)
                .name(name)
                .converter(new FilePath.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<File> fileSwitch(final String name, final String description)
    {
        return SwitchParser.builder(File.class)
                .name(name)
                .converter(new File.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<File> inputFile()
    {
        return fileSwitch("input", "The input file to process");
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
        return fileSwitch("output", "The output file to target");
    }

    public static File parse(final String path)
    {
        return File.of(FilePath.parseFilePath(path));
    }

    public static synchronized File temporary(final Extension extension)
    {
        return Folder.kivakitTemporaryFolder().file("temp-" + temporaryFileNumber++ + extension);
    }

    public static class Converter extends BaseStringConverter<File>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected File onConvertToObject(final String value)
        {
            return File.parse(value);
        }
    }

    @UmlClassDiagram(diagram = DiagramResourceService.class)
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

    public File absolute()
    {
        return File.of(path().absolute());
    }

    public Folder asFolder()
    {
        return new Folder(service.folderService());
    }

    public java.io.File asJavaFile()
    {
        return service.asJavaFile();
    }

    @Override
    public Bytes bytes()
    {
        return service.bytes();
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return service.chmod(permissions);
    }

    @Override
    public boolean delete()
    {
        trace("Deleting $", this);
        return service.delete();
    }

    @Override
    public void dematerialize()
    {
        trace("Dematerializing $", this);
        service.dematerialize();
    }

    public void ensureReadable()
    {
        ensure(isReadable());
    }

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

    public boolean isArchive()
    {
        return fileName().isArchive();
    }

    public boolean isExecutable()
    {
        return fileName().isExecutable();
    }

    public boolean isFile()
    {
        return service.isFile();
    }

    public boolean isFolder()
    {
        return service.isFolder();
    }

    public boolean isNewerThan(final File that)
    {
        return service.isNewerThan(that.service);
    }

    public boolean isNonEmpty()
    {
        return exists() && isLargerThan(Bytes._0);
    }

    public boolean isOlderThan(final File that)
    {
        return service.isOlderThan(that.service);
    }

    @Override
    public boolean isReadable()
    {
        return service.isReadable();
    }

    @Override
    public boolean isRemote()
    {
        return service.isRemote();
    }

    @Override
    public boolean isWritable()
    {
        return service.isWritable();
    }

    @Override
    public Time lastModified()
    {
        final var lastModified = service.lastModified();
        trace("Last modified time of $ is $", this, lastModified);
        return lastModified;
    }

    @Override
    public boolean lastModified(final Time modified)
    {
        return service.lastModified(modified);
    }

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

    public Folder parent()
    {
        return new Folder(service.parent());
    }

    @Override
    public FilePath path()
    {
        return service.path();
    }

    public File relativeTo(final Folder folder)
    {
        return File.of(service.relativePath(folder.virtualFolder()));
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(final File that)
    {
        trace("Rename $ to $", this, that);
        return service.renameTo(that.service);
    }

    @UmlExcludeMember
    public FileSystemObjectService resolve()
    {
        return service;
    }

    public Folder root()
    {
        return new Folder(service.rootFolderService());
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

    @Override
    public String toString()
    {
        return service.toString();
    }

    public File withBaseName(final String name)
    {
        final var file = File.of(path().parent().withChild(name));
        if (extension() != null)
        {
            return file.withExtension(extension());
        }
        return file;
    }

    public File withCharset(final Charset charset)
    {
        final var file = new File(this);
        file.charset(charset);
        return file;
    }

    public File withCodec(final Codec codec)
    {
        final var file = new File(this);
        file.codec(codec);
        return file;
    }

    public File withExtension(final Extension extension)
    {
        return File.parse(path().toString() + extension);
    }

    public File withFileName(final FileName name)
    {
        return parent().file(name);
    }

    @UmlExcludeMember
    public File withVariables(final VariableMap<?> variables)
    {
        return File.parse(variables.expanded(toString()));
    }

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

    public File withoutExtension()
    {
        final var extension = extension();
        if (extension != null)
        {
            final var withoutExtension = PathStrings.withoutOptionalSuffix(path().toString(), '.');
            return File.parse(withoutExtension);
        }
        return this;
    }

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
