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
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourceFolder;
import com.telenav.kivakit.core.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFolder;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.kivakit.core.resource.spi.ResourceFolderResolver;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.telenav.kivakit.core.filesystem.Folder.Traversal.RECURSE;
import static com.telenav.kivakit.core.filesystem.Folder.Type.CLEAN_UP_ON_EXIT;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Folder abstraction that extends {@link FileSystemObject} and implements the {@link ResourceFolder} interface for
 * filesystems. Consumes a {@link FolderService} supplied by a filesystem service provider. Filesystem service providers
 * can be found in
 * <i>com.telenav.kivakit.core.filesystem.local</i> (the local filesystem provider) and in the
 * <i>kivakit-filesystems</i> module.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #parse(String)} - A folder for the given string</li>
 *     <li>{@link #of(URI)} - A folder for the given URI</li>
 *     <li>{@link #of(URL)} - A folder for the given URL</li>
 *     <li>{@link #of(java.io.File)} - A folder for the given Java file</li>
 *     <li>{@link #of(Path)} - A folder for the given NIO path</li>
 * </ul>
 *
 * <p><b>Locations</b></p>
 *
 * <ul>
 *     <li>{@link #current()} - The current working folder</li>
 *     <li>{@link #desktop()} - The desktop folder</li>
 *     <li>{@link #userHome()} - The user's home folder</li>
 *     <li>{@link #kivakitHome()} - The KivaKit home folder</li>
 *     <li>{@link #kivakitCacheFolder()} - The KivaKit cache folder, normally ~/.kivakit</li>
 *     <li>{@link #kivakitTemporaryFolder()} - Folder where temporary files can be created</li>
 *     <li>{@link #kivakitTestFolder(Class)} - Folder for test files for the given class</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - The filename of this folder</li>
 *     <li>{@link #disk()} - The disk where this folder exists, if any</li>
 *     <li>{@link #lastModified()} - The last time this folder was modified</li>
 *     <li>{@link #size()} - The total size of this folder</li>
 * </ul>
 *
 * <p><b>Contents</b></p>
 *
 * <ul>
 *     <li>{@link #files()} - The files in this folder</li>
 *     <li>{@link #files(Pattern)} - The files in this folder matching the given pattern</li>
 *     <li>{@link #files(Matcher)} - The matching files in this folder</li>
 *     <li>{@link #files(Matcher, Traversal)} - The matching files in this folder found with the given {@link Traversal} type</li>
 *     <li>{@link #folders()} - The folders in this folder</li>
 *     <li>{@link #folders(Matcher)} - The matching folders in this folder</li>
 *     <li>{@link #nestedFiles()} - All nested files in this folder</li>
 *     <li>{@link #nestedFiles(Matcher)} - All matching nested files in this folder</li>
 *     <li>{@link #nestedFolders(Matcher)} - All matching nested folders under this folder</li>
 *     <li>{@link #oldest()} - The oldest file in this folder</li>
 *     <li>{@link #oldest(Matcher)} - The oldest matching file in this folder</li>
 *     <li>{@link #temporaryFile(FileName)} - A temporary file in this folder with the given name</li>
 *     <li>{@link #temporaryFolder(FileName)} - A temporary sub-folder with the given name</li>
 *     <li>{@link #temporaryFile(FileName, Extension)} - A temporary file in this folder with the given name and extension</li>
 * </ul>
 *
 * <p><b>Hierarchy</b></p>
 *
 * <ul>
 *     <li>{@link #absolute()} - This folder with an absolute path</li>
 *     <li>{@link #path()} - The path to this folder</li>
 *     <li>{@link #parent()} - The parent folder, or null if there is none</li>
 *     <li>{@link #relativeTo(Folder)} - This folder with a path relative to the given folder</li>
 *     <li>{@link #relativePath(Folder)} - The relative of this path with respect to the given folder</li>
 *     <li>{@link #root()} - The root folder of this folder</li>
 *     <li>{@link #file(File)} - The given file relative to this folder</li>
 *     <li>{@link #file(String)} - The file with the given name in this folder</li>
 *     <li>{@link #file(FileName)} - The file with the given name in this folder</li>
 *     <li>{@link #file(FilePath)} - The file with the given relative path to this folder</li>
 *     <li>{@link #folder(Folder)} - The folder relative to this folder</li>
 *     <li>{@link #folder(String)} - The folder with the given name in this folder</li>
 *     <li>{@link #of(FileName)} - The folder in this folder with the given filename </li>
 *     <li>{@link #Folder(FilePath)} - The folder with the given relative path to this folder</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)} - Changes the access permissions of this folder</li>
 *     <li>{@link #chmodNested(PosixFilePermission...)} - Recursively changes the access permissions of this folder</li>
 *     <li>{@link #clearAll()} - Removes everything in this folder</li>
 *     <li>{@link #clearAllAndDelete()} - Removes everything in this folder and then deletes it</li>
 *     <li>{@link #copyTo(Folder, CopyMode, ProgressReporter)} - Copies this folder to the given folder</li>
 *     <li>{@link #copyTo(Folder, CopyMode, Matcher, ProgressReporter)} - Copies the matching files in this folder to the given folder</li>
 *     <li>{@link #delete()} - Deletes this folder if it is empty</li>
 *     <li>{@link #mkdirs()} - Creates this folder and any required parent folders</li>
 *     <li>{@link #renameTo(Folder)} - Renames this folder to the given folder</li>
 *     <li>{@link #lastModified(Time)} - Sets the last modified time of this folder</li>
 *     <li>{@link #scheduleCleanUpOnExit()} - Schedules this folder to be removed when the VM exits</li>
 * </ul>
 *
 * <p><b>Checks</b></p>
 *
 * <ul>
 *     <li>{@link #ensureExists()} - Ensures this folder exists or fails</li>
 *     <li>{@link #exists()} - True if this folder exists</li>
 *     <li>{@link #isFolder()} - True if this is a folder and not a file</li>
 *     <li>{@link #isEmpty()} - True if this folder is empty</li>
 *     <li>{@link #isRemote()} - True if this folder is on a remote filesystem</li>
 *     <li>{@link #isLocal()} - True if this folder is on the local filesystem</li>
 *     <li>{@link #isMaterialized()} - True if this folder exists locally</li>
 *     <li>{@link #isWritable()} - True if this folder can be written to</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asJavaFile()} - This folder as a {@link java.io.File}</li>
 *     <li>{@link #asUri()} - This folder as a URI</li>
 *     <li>{@link #asUrl()} - This folder as a URL</li>
 *     <li>{@link #absolute()} - This folder with an absolute path</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@LexakaiJavadoc(complete = true)
public class Folder implements FileSystemObject, Comparable<Folder>, ResourceFolder
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    // a flag to make sure the temporary folder will be create only once per process
    private static boolean temporaryForProcessInitialized;

    // Monitor for serializing the creation of temporary files
    private static final Monitor temporaryLock = new Monitor();

    private static final Monitor lock = new Monitor();

    public static SwitchParser.Builder<Folder> INPUT = folderSwitch("input-folder", "Input folder to process");

    // Note that this switch parser ensures that the folder exists
    public static final SwitchParser.Builder<Folder> OUTPUT = SwitchParser.builder(Folder.class).name("output-folder")
            .converter(new Folder.Converter(LOGGER, true)).description("Output folder to write to");

    public static Folder current()
    {
        try
        {
            return Folder.parse(new java.io.File(".").getCanonicalPath());
        }
        catch (final IOException e)
        {
            throw new Problem(e, "Can't get working folder").asException();
        }
    }

    public static Folder desktop()
    {
        return userHome().folder("Desktop");
    }

    public static ArgumentParser.Builder<Folder> folderArgument(final String description)
    {
        return ArgumentParser.builder(Folder.class)
                .converter(new Folder.Converter(LOGGER))
                .description(description);
    }

    public static ArgumentParser.Builder<FolderList> folderArgumentList(final String description)
    {
        return ArgumentParser.builder(FolderList.class)
                .converter(new FolderList.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<Folder> folderSwitch(final String name, final String description)
    {
        return SwitchParser.builder(Folder.class)
                .name(name)
                .converter(new Folder.Converter(LOGGER))
                .description(description);
    }

    public static SwitchParser.Builder<FolderList> folderSwitchList(final String name, final String description)
    {
        return SwitchParser.builder(FolderList.class)
                .name(name)
                .converter(new FolderList.Converter(LOGGER))
                .description(description);
    }

    public static boolean isFolder(final FilePath path)
    {
        return new java.io.File(path.join()).isDirectory();
    }

    public static Folder kivakitCacheFolder()
    {
        return Folder.of(KivaKit.get().cacheFolderPath()).mkdirs();
    }

    public static Folder kivakitHome()
    {
        final var home = KivaKit.get().homeFolderPath();
        if (home != null)
        {
            return Folder.of(home);
        }
        return fail("Cannot find KivaKit home folder");
    }

    public static Folder kivakitTemporaryFolder()
    {
        return kivakitCacheFolder().folder("temporary").mkdirs();
    }

    public static Folder kivakitTestFolder(final Class<?> type)
    {
        return kivakitTemporaryFolder().folder("test").folder(type.getSimpleName()).mkdirs();
    }

    public static Folder of(final FileName filename)
    {
        return parse(filename.name());
    }

    public static Folder of(final Path path)
    {
        return parse(path.toString());
    }

    public static Folder of(final java.io.File file)
    {
        return of(file.toPath());
    }

    public static Folder of(final URI uri)
    {
        return of(new java.io.File(uri));
    }

    public static Folder of(final URL url)
    {
        try
        {
            return of(url.toURI());
        }
        catch (final URISyntaxException e)
        {
            LOGGER.problem(e, "Unable to parse URL: $", url);
            return null;
        }
    }

    public static SwitchParser.Builder<Folder> outputFolderSwitch()
    {
        return folderSwitch("output", "Output folder");
    }

    public static Folder parse(final String path)
    {
        if (Strings.isEmpty(path))
        {
            return null;
        }
        final var filePath = FilePath.parseFilePath(path);
        return filePath == null ? null : new Folder(filePath);
    }

    /**
     * @return unique temporary folder per process
     */
    public static Folder temporaryForProcess(final Type type)
    {
        synchronized (temporaryLock)
        {
            final var name = "kivakit-process-" + OperatingSystem.get().processIdentifier();
            final var temporary = kivakitTemporaryFolder()
                    .folder("processes")
                    .folder(name)
                    .mkdirs();
            temporary.type = type;
            if (type == CLEAN_UP_ON_EXIT)
            {
                temporary.scheduleCleanUpOnExit();
            }
            if (!temporaryForProcessInitialized)
            {
                temporaryForProcessInitialized = true;

                // Clear existing folder if it exists
                temporary.clearAll();
                temporary.mkdirs();
            }
            return temporary;
        }
    }

    public static Folder userHome()
    {
        return Folder.parse(System.getProperty("user.home"));
    }

    /**
     * Type of traversal to perform
     */
    @LexakaiJavadoc(complete = true)
    public enum Traversal
    {
        RECURSE,
        FLAT
    }

    /**
     * Type of folder
     */
    @LexakaiJavadoc(complete = true)
    public enum Type
    {
        NORMAL,
        CLEAN_UP_ON_EXIT
    }

    /**
     * Resolves valid folder paths into {@link ResourceFolder}s.
     *
     * @author jonathanl (shibo)
     */
    @UmlClassDiagram(diagram = DiagramResourceService.class)
    @LexakaiJavadoc(complete = true)
    public static class Resolver implements ResourceFolderResolver
    {
        @Override
        public boolean accepts(final ResourceFolderIdentifier identifier)
        {
            return Folder.parse(identifier.identifier()) != null;
        }

        @Override
        public ResourceFolder resolve(final ResourceFolderIdentifier identifier)
        {
            return Folder.parse(identifier.identifier());
        }
    }

    /**
     * Converts to and from {@link Folder}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Folder>
    {
        private boolean ensureExists;

        public Converter(final Listener listener)
        {
            super(listener);
        }

        public Converter(final Listener listener, final boolean ensureExists)
        {
            super(listener);
            this.ensureExists = ensureExists;
        }

        @Override
        protected Folder onConvertToObject(final String value)
        {
            final var path = FilePath.parseFilePath(value);
            final var folder = new Folder(path);
            if (ensureExists)
            {
                folder.ensureExists();
            }
            return folder;
        }
    }

    @UmlAggregation(label = "delegates to")
    private transient FolderService service;

    private Type type = Type.NORMAL;

    private FilePath path;

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    public Folder()
    {
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    public Folder(final FolderService service)
    {
        ensureNotNull(service);

        this.service = service;
        path = service.path();

        assert path != null;
    }

    /**
     * <b>Not public API</b>
     */
    public Folder(final FilePath path)
    {
        this(FileSystemServiceLoader.fileSystem(path).folderService(path));
    }

    public Folder absolute()
    {
        return new Folder(path().absolute());
    }

    public java.io.File asJavaFile()
    {
        return new java.io.File(path().asString());
    }

    public URI asUri()
    {
        try
        {
            return new URI(folder().path().toString());
        }
        catch (final URISyntaxException e)
        {
            throw new IllegalStateException("Cannot convert " + this + " to URI", e);
        }
    }

    public URL asUrl()
    {
        try
        {
            return asUri().toURL();
        }
        catch (final MalformedURLException e)
        {
            return null;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return folder().chmod(permissions);
    }

    public void chmodNested(final PosixFilePermission... permissions)
    {
        nestedFolders(new All<>()).forEach(folder -> folder().chmod(permissions));
        nestedFiles(new All<>()).forEach(file -> file.chmod(permissions));
    }

    /**
     * Clears all files and folders in this folder
     */
    @SuppressWarnings("UnusedReturnValue")
    public synchronized Folder clearAll()
    {
        DEBUG.trace("Clearing $", this);
        try
        {
            if (exists())
            {
                for (final var file : files())
                {
                    file.delete();
                    if (file.exists())
                    {
                        LOGGER.warning("Unable to remove $", file);
                    }
                }
                for (final var folder : folders())
                {
                    folder.clearAll();
                    folder.delete();
                }
            }
        }
        catch (final Exception e)
        {
            LOGGER.warning("Unable to clear $", this);
        }
        return this;
    }

    /**
     * Clears this folder of files and folders, then deletes it
     */
    @SuppressWarnings("UnusedReturnValue")
    public Folder clearAllAndDelete()
    {
        DEBUG.trace("Clearing and deleting $", this);
        try
        {
            if (exists())
            {
                clearAll();
                delete();
            }
        }
        catch (final Exception e)
        {
            LOGGER.warning("Unable to deleteAll on $", this);
        }
        return this;
    }

    @Override
    public int compareTo(@NotNull final Folder that)
    {
        return name().compareTo(that.name());
    }

    /**
     * Copies all nested files matching the given matcher from this folder to the destination folder.
     */
    public void copyTo(final Folder destination,
                       final CopyMode mode,
                       final Matcher<File> matcher,
                       final ProgressReporter reporter)
    {
        final var start = Time.now();

        // Ensure the destination folder exists,
        LOGGER.information("Copying $ to $", this, destination);
        destination.ensureExists();

        // then for each nested file,
        for (final var file : nestedFiles(matcher))
        {
            // make relative target file,
            final var target = destination.file(file.relativeTo(this));

            // and if we can copy to it,
            if (mode.canCopy(file, target))
            {
                // then copy the file and update its last modified timestamp to the source timestamp
                file.copyTo(target.ensureWritable(), mode, reporter);
                target.lastModified(file.lastModified());
            }
        }
        LOGGER.information("Copy completed in $", start.elapsedSince());
    }

    /**
     * Copies all nested files from this folder to the destination folder
     */
    public void copyTo(final Folder destination, final CopyMode mode, final ProgressReporter reporter)
    {
        copyTo(destination, mode, new All<>(), reporter);
    }

    /**
     * Delete a folder if it is empty
     *
     * @return True if the folder was removed
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean delete()
    {
        DEBUG.trace("Deleting $", this);
        return folder().delete();
    }

    /**
     * @return The {@link Disk} where this folder exists
     */
    @UmlRelation(label = "exists on")
    public Disk disk()
    {
        return new Disk(folder().diskService());
    }

    /**
     * Creates this folder if it does not exist. If the folder cannot be created, an {@link IllegalStateException} is
     * thrown.
     */
    public Folder ensureExists()
    {
        DEBUG.trace("Ensuring that $ exists", this);
        synchronized (lock)
        {
            if (!exists())
            {
                mkdirs();
                if (!exists())
                {
                    throw new IllegalStateException("Unable to create folder " + this);
                }
            }
            return this;
        }
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Folder)
        {
            final var that = (Folder) object;
            return path().equals(that.path());
        }
        return false;
    }

    /**
     * @return True if this folder exists
     */
    public boolean exists()
    {
        return folder().exists();
    }

    public File file(final File file)
    {
        return file(file.path());
    }

    public File file(final FileName name)
    {
        return new File(folder().file(name));
    }

    public File file(FilePath child)
    {
        child = child.withoutRoot();

        // Get the filename from the path
        final var fileName = child.fileName();

        // and if the parent is empty
        final var parent = child.parent();
        if (parent.isEmpty())
        {
            // then it's a simple filename
            return new File(folder().file(fileName));
        }
        else
        {
            // otherwise append the parent path and filename to this folder
            return new File(folder().folder(new Folder(parent)).file(fileName));
        }
    }

    public File file(final String path)
    {
        return file(FilePath.parseFilePath(path));
    }

    public FileList files()
    {
        return files(new All<>());
    }

    public FileList files(final Matcher<File> matcher, final Traversal recurse)
    {
        return recurse == RECURSE ? nestedFiles(matcher) : files(matcher);
    }

    public FileList files(final Matcher<File> matcher)
    {
        final var files = new FileList();
        if (exists())
        {
            for (final FileService service : folder().files())
            {
                final var file = new File(service);
                if (matcher.matches(file))
                {
                    files.add(file);
                }
            }
        }
        DEBUG.trace("Files in $: $", this, files);
        return files;
    }

    public FileList files(final Pattern pattern)
    {
        return files(value -> pattern.matcher(value.fileName().name()).matches());
    }

    @Override
    public Folder folder(final String child)
    {
        if (child.equals("."))
        {
            return this;
        }
        final var childFolder = Folder.parse(child);
        return childFolder == null ? null : folder(childFolder);
    }

    public Folder folder(final FileName child)
    {
        if (child.name().equals("."))
        {
            return this;
        }
        return new Folder(folder().folder(child));
    }

    public Folder folder(final Folder child)
    {
        if (child.path().isEmpty() || child.path().asString().equals("."))
        {
            return this;
        }
        return new Folder(folder().folder(child));
    }

    public List<Folder> folders(final Matcher<Folder> matcher)
    {
        return folders().stream().filter(matcher::matches).collect(Collectors.toList());
    }

    public List<Folder> folders()
    {
        final List<Folder> folders = new ArrayList<>();
        if (service != null)
        {
            for (final FolderService folder : folder().folders())
            {
                folders.add(new Folder(folder));
            }
        }
        Collections.sort(folders);
        DEBUG.trace("Folders in $: $", this, folders);
        return folders;
    }

    @Override
    public int hashCode()
    {
        return path().hashCode();
    }

    public boolean isEmpty()
    {
        return exists() && files().isEmpty() && folders().isEmpty();
    }

    public boolean isFolder()
    {
        return folder().isFolder();
    }

    public boolean isLocal()
    {
        return !isRemote();
    }

    @Override
    public boolean isMaterialized()
    {
        return true;
    }

    public boolean isRemote()
    {
        final var scheme = path().scheme();
        return "hdfs".equalsIgnoreCase(scheme) || "s3".equalsIgnoreCase(scheme);
    }

    public boolean isWritable()
    {
        return folder().isWritable();
    }

    public Folder last()
    {
        return Folder.parse(path().last());
    }

    @Override
    public Time lastModified()
    {
        return folder().lastModified();
    }

    public Folder mkdirs()
    {
        if (!exists())
        {
            DEBUG.trace("Creating folder $", this);
            folder().mkdirs();
        }
        return this;
    }

    public FileName name()
    {
        return folder().fileName();
    }

    public FileList nestedFiles()
    {
        return nestedFiles(value -> true);
    }

    /**
     * @return Any matching files that are recursively contained in this folder
     */
    public FileList nestedFiles(final Matcher<File> matcher)
    {
        final var files = FileList.forServices(folder().nestedFiles(path -> matcher.matches(path.asFile())));
        DEBUG.trace("Nested files in $: $", this, files);
        return files;
    }

    /**
     * @return Any matching folders that are recursively contained in this folder
     */
    public FolderList nestedFolders(final Matcher<Folder> matcher)
    {
        final var folders = FolderList.forVirtual(folder().nestedFolders(path -> matcher.matches(new Folder(path))));
        DEBUG.trace("Nested folders in $: $", this, folders);
        return folders;
    }

    public File oldest()
    {
        return oldest(new All<>());
    }

    public File oldest(final Matcher<File> matcher)
    {
        File oldestFile = null;
        for (final var file : files())
        {
            if (matcher.matches(file))
            {
                if (oldestFile == null || file.isOlderThan(oldestFile))
                {
                    oldestFile = file;
                }
            }
        }
        return oldestFile;
    }

    public Folder parent()
    {
        return new Folder(path().absolute().parent());
    }

    public FilePath path()
    {
        return folder().path();
    }

    public FilePath relativePath(final Folder folder)
    {
        return absolute().folder().relativePath(folder.absolute().folder());
    }

    public Folder relativeTo(final Folder folder)
    {
        return new Folder(relativePath(folder));
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(final Folder that)
    {
        DEBUG.trace("Renaming $ to $", this, that);
        return folder().renameTo(that.folder());
    }

    @Override
    public Resource resource(final String name)
    {
        return file(name);
    }

    @Override
    public List<File> resources(final Matcher<? super Resource> matcher)
    {
        return files()
                .stream()
                .filter(matcher)
                .collect(Collectors.toList());
    }

    public Folder root()
    {
        return new Folder(path().root());
    }

    public void safeCopyTo(final Folder destination,
                           final CopyMode mode,
                           final Matcher<File> matcher,
                           final ProgressReporter reporter)
    {
        if (mode == CopyMode.DO_NOT_OVERWRITE && destination.exists() && !destination.isEmpty())
        {
            fail("Can't overwrite non-empty destination folder: $ ", destination);
        }

        destination.delete();
        if (destination.exists())
        {
            fail("Can't remove empty destination folder " + destination);
        }

        final var start = Time.now();
        LOGGER.information("Safely copying $ to $", this, destination);
        final var temporary = destination.parent().temporaryFolder(FileName.parse("temporary-copy"));
        for (final var file : nestedFiles(matcher))
        {
            file.copyTo(temporary.file(file.relativeTo(this)), mode, reporter);
        }
        temporary.renameTo(destination);
        LOGGER.information("Safe copy completed in $", start.elapsedSince());
    }

    @Override
    public void safeCopyTo(final Folder destination, final CopyMode mode, final ProgressReporter reporter)
    {
        safeCopyTo(destination, mode, new All<>(), reporter);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Folder scheduleCleanUpOnExit()
    {
        // Add a shutdown hook to make sure the folder is deleted on VM exit
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            try
            {
                if (type() == CLEAN_UP_ON_EXIT && System.getProperty("KIVAKIT_KEEP_TEMPORARY_FILES") == null)
                {
                    if (exists())
                    {
                        clearAllAndDelete();
                    }
                }
            }
            catch (final Exception ignored)
            {
            }
        }));

        return this;
    }

    public Bytes size()
    {
        return folder().bytes();
    }

    public File temporaryFile(final FileName baseName)
    {
        return temporaryFile(baseName, Extension.TMP);
    }

    public File temporaryFile(final FileName baseName, final Extension extension)
    {
        synchronized (temporaryLock)
        {
            var sequenceNumber = 0;
            File file;
            do
            {
                file = file(baseName + "-" + sequenceNumber + extension);
                sequenceNumber++;
            }
            while (file.exists());
            file.writer().save("");
            return file;
        }
    }

    public Folder temporaryFolder(final FileName baseName)
    {
        synchronized (temporaryLock)
        {
            var sequenceNumber = 0;
            Folder folder;
            do
            {
                folder = folder(baseName + "-" + sequenceNumber);
                sequenceNumber++;
            }
            while (folder.exists());
            folder.mkdirs();
            return folder;
        }
    }

    @Override
    public String toString()
    {
        return folder().toString();
    }

    @UmlExcludeMember
    public Type type()
    {
        return type;
    }

    FolderService service()
    {
        return service;
    }

    private FolderService folder()
    {
        if (service == null)
        {
            service = FileSystemServiceLoader.fileSystem(path).folderService(path);
        }
        return service;
    }
}
