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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.thread.Monitor;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceGlob;
import com.telenav.kivakit.resource.ResourceList;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Collections;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.core.string.Strings.ensureEndsWith;
import static com.telenav.kivakit.core.string.Strings.isNullOrBlank;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;
import static com.telenav.kivakit.filesystem.Folder.FolderType.CLEAN_UP_ON_EXIT;
import static com.telenav.kivakit.filesystem.Folder.Traversal.RECURSE;
import static com.telenav.kivakit.filesystem.Folders.kivakitTemporaryFolder;
import static com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader.fileSystem;
import static com.telenav.kivakit.interfaces.comparison.Filter.acceptAll;
import static com.telenav.kivakit.resource.Extension.TEMPORARY;
import static com.telenav.kivakit.resource.ResourceGlob.glob;
import static com.telenav.kivakit.resource.ResourceList.resourceList;
import static java.util.Objects.requireNonNull;

/**
 * Folder abstraction that extends {@link FileSystemObject} and implements the {@link ResourceFolder} interface for
 * filesystems. Consumes a {@link FolderService} supplied by a filesystem service provider. Filesystem service providers
 * can be found in
 * <i>com.telenav.kivakit.filesystem.local</i> (the local filesystem provider) and in the
 * <i>kivakit-filesystems</i> module.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #folder(FilePath)}</li>
 *     <li>{@link #folder(Path)} - A folder for the given NIO path</li>
 *     <li>{@link #folder(StringPath)}</li>
 *     <li>{@link #folder(URI)} - A folder for the given URI</li>
 *     <li>{@link #folder(URL)} - A folder for the given URL</li>
 *     <li>{@link #folder(java.io.File)} - A folder for the given Java file</li>
 *     <li>{@link Folders#kivakitTestFolder(Class)} - Folder for test files for the given class</li>
 *     <li>{@link #parseFolder(Listener, String, Object...)} - A folder for the given string</li>
 *     <li>{@link #parseFolder(String, Object...)}</li>
 * </ul>
 *
 * <p><b>Command Line</b></p>
 *
 * <ul>
 *     <li>{@link Folders#folderArgumentParser(Listener, String)}</li>
 *     <li>{@link Folders#folderListArgumentParser(Listener, String)}</li>
 *     <li>{@link Folders#folderSwitchParser(Listener, String, String)}</li>
 *     <li>{@link Folders#folderListSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #disk()} - The disk where this folder exists, if any</li>
 *     <li>{@link #lastModified()} - The last time this folder was modified</li>
 *     <li>{@link #name()} - The filename of this folder</li>
 *     <li>{@link #path()}</li>
 *     <li>{@link #root()}</li>
 *     <li>{@link #size()} - The total size of this folder</li>
 *     <li>{@link #type()}</li>
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
 *     <li>{@link #temporaryFile(FileName, Extension)} - A temporary file in this folder with the given name and extension</li>
 *     <li>{@link #temporaryFolder(FileName)} - A temporary sub-folder with the given name</li>
 * </ul>
 *
 * <p><b>Hierarchy</b></p>
 *
 * <ul>
 *     <li>{@link #Folder(FilePath)} - The folder with the given relative path to this folder</li>
 *     <li>{@link #absolute()} - This folder with an absolute path</li>
 *     <li>{@link #chmodNested(PosixFilePermission...)}</li>
 *     <li>{@link #file(File)} - The given file relative to this folder</li>
 *     <li>{@link #file(FileName)} - The file with the given name in this folder</li>
 *     <li>{@link #file(ResourcePathed)} - The file with the given relative path to this folder</li>
 *     <li>{@link #file(String, Object...)} - The file with the given name in this folder</li>
 *     <li>{@link #files(Pattern)}</li>
 *     <li>{@link #files(String)}</li>
 *     <li>{@link #folder(FileName)} - The folder in this folder with the given filename </li>
 *     <li>{@link #folder(Folder)} - The folder relative to this folder</li>
 *     <li>{@link #folder(String)} - The folder with the given name in this folder</li>
 *     <li>{@link #nestedFiles()}</li>
 *     <li>{@link #nestedFiles(Matcher)}</li>
 *     <li>{@link #nestedFolders(Matcher)}</li>
 *     <li>{@link #parent()} - The parent folder, or null if there is none</li>
 *     <li>{@link #path()} - The path to this folder</li>
 *     <li>{@link #relativePath(Folder)} - The relative of this path with respect to the given folder</li>
 *     <li>{@link #relativeTo(Folder)} - This folder with a path relative to the given folder</li>
 *     <li>{@link #root()} - The root folder of this folder</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #chmod(PosixFilePermission...)} - Changes the access permissions of this folder</li>
 *     <li>{@link #chmodNested(PosixFilePermission...)} - Recursively changes the access permissions of this folder</li>
 *     <li>{@link #clearAll()} - Removes everything in this folder</li>
 *     <li>{@link #clearAllAndDelete()} - Removes everything in this folder and then deletes it</li>
 *     <li>{@link #copyTo(Folder, WriteMode, ProgressReporter)} - Copies this folder to the given folder</li>
 *     <li>{@link #copyTo(Folder, WriteMode, Matcher, ProgressReporter)} - Copies the matching files in this folder to the given folder</li>
 *     <li>{@link #delete()} - Deletes this folder if it is empty</li>
 *     <li>{@link #mkdirs()} - Creates this folder and any required parent folders</li>
 *     <li>{@link #renameTo(ResourceFolder)} - Renames this folder to the given folder</li>
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
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class Folder extends BaseRepeater implements
        FileSystemObject,
        Comparable<Folder>,
        ResourceFolder<Folder>,
        TryTrait
{
    /** A flag to make sure the temporary folder will be created only once per process */
    private static boolean temporaryForProcessInitialized;

    /** Monitor for serializing the creation of temporary files */
    private static final Monitor temporaryLock = new Monitor();

    /** Lock for atomic access to folders */
    private static final Monitor lock = new Monitor();

    /**
     * Returns the folder at the given path
     *
     * @param path The path
     */
    public static Folder folder(@NotNull Path path)
    {
        return parseFolder(throwingListener(), path.toString());
    }

    /**
     * Returns the folder for the given path
     */
    public static Folder folder(@NotNull StringPath path)
    {
        return parseFolder(throwingListener(), path.toString());
    }

    /**
     * Returns the folder specified by the given Java file object
     */
    public static Folder folder(@NotNull java.io.File file)
    {
        return folder(file.toPath());
    }

    /**
     * Returns the folder for the given {@link URI}
     */
    public static Folder folder(@NotNull URI uri)
    {
        return folder(new java.io.File(uri));
    }

    /**
     * Returns the folder for the given {@link URL}
     */
    public static Folder folder(@NotNull URL url)
    {
        try
        {
            return folder(url.toURI());
        }
        catch (URISyntaxException e)
        {
            new Problem(e, "Unable to parse URL: $", url).throwMessage();
            return null;
        }
    }

    /**
     * Returns a folder for the given path
     *
     * @param path The path
     * @return The folder
     */
    public static Folder folder(@NotNull FilePath path)
    {
        return parseFolder(throwingListener(), path.toString());
    }

    /**
     * Parses the given path into a folder
     *
     * @param path The path to parse
     * @param arguments Any arguments to format the path with
     * @return The folder
     */
    public static Folder parseFolder(@NotNull String path, Object... arguments)
    {
        return parseFolder(throwingListener(), path, arguments);
    }

    /**
     * Parses the given path into a folder
     *
     * @param listener The listener to call with problems
     * @param path The path to parse
     * @param arguments Any arguments to format the path with
     * @return The folder
     */
    public static Folder parseFolder(@NotNull Listener listener, String path, Object... arguments)
    {
        if (isNullOrBlank(path))
        {
            return null;
        }
        path = format(path, arguments);
        var filePath = parseFilePath(listener, path);
        return filePath == null ? null : new Folder(filePath);
    }

    /**
     * Returns unique temporary folder per process
     */
    public static Folder temporaryFolderForProcess(@NotNull FolderType type)
    {
        synchronized (temporaryLock)
        {
            var name = "kivakit-process-" + operatingSystem().processIdentifier();
            var temporary = kivakitTemporaryFolder()
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

    /**
     * Type of folder
     */
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    public enum FolderType
    {
        NORMAL,
        CLEAN_UP_ON_EXIT
    }

    /**
     * Type of traversal to perform
     */
    @CodeQuality(stability = STABLE_EXTENSIBLE,
                 testing = TESTING_NOT_NEEDED,
                 documentation = DOCUMENTATION_COMPLETE)
    public enum Traversal
    {
        RECURSE,
        FLAT
    }

    /**
     * Converts to and from {@link Folder}s
     *
     * @author jonathanl (shibo)
     */
    @CodeQuality(stability = STABLE,
                 testing = UNTESTED,
                 documentation = DOCUMENTATION_COMPLETE)
    public static class Converter extends BaseStringConverter<Folder>
    {
        private boolean ensureExists;

        public Converter(@NotNull Listener listener)
        {
            super(listener, Folder.class);
        }

        public Converter(@NotNull Listener listener, boolean ensureExists)
        {
            super(listener, Folder.class);
            this.ensureExists = ensureExists;
        }

        @Override
        protected Folder onToValue(String value)
        {
            var path = parseFilePath(this, value);
            var folder = new Folder(path);
            if (ensureExists)
            {
                folder.ensureExists();
            }
            return folder;
        }
    }

    /** The underlying path to the folder */
    private FilePath path;

    @UmlAggregation(label = "delegates to")
    private transient FolderService service;

    /** The type of folder */
    private FolderType type = FolderType.NORMAL;

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
    public Folder(@NotNull FolderService service)
    {
        ensureNotNull(service);

        this.service = service;
        path = service.path();

        assert path != null;
    }

    /**
     * <b>Not public API</b>
     */
    public Folder(@NotNull FilePath path)
    {
        this(requireNonNull(fileSystem(throwingListener(), path)).folderService(path));
    }

    /**
     * This folder as an absolute path with a trailing slash on it
     */
    @Override
    public Folder absolute()
    {
        return new Folder(path().asAbsolute()).withTrailingSlash();
    }

    /**
     * Returns this folder as a {@link URI}
     */
    public URI asUri()
    {
        try
        {
            var path = path();
            if (!path.hasScheme())
            {
                path = path.withScheme("file");
            }
            return new URI(ensureEndsWith(path.toString(), "/"));
        }
        catch (Exception e)
        {
            throw problem(e, "Cannot convert to URI: $", this).asException();
        }
    }

    /**
     * Returns this folder as a {@link URL}
     */
    public URL asUrl()
    {
        try
        {
            return withTrailingSlash().asUri().toURL();
        }
        catch (Exception e)
        {
            throw problem(e, "Folder could not be converted to a URL: $", this).asException();
        }
    }

    /**
     * Changes the access permissions for this folder
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(@NotNull PosixFilePermission... permissions)
    {
        return folderService().chmod(permissions);
    }

    /**
     * Changes the access permissions for this folder and all folders below it
     */
    public void chmodNested(@NotNull PosixFilePermission... permissions)
    {
        nestedFolders(acceptAll()).forEach(folder -> folderService().chmod(permissions));
        nestedFiles(acceptAll()).forEach(file -> file.chmod(permissions));
    }

    /**
     * Clears all files and folders in this folder
     */
    @SuppressWarnings("UnusedReturnValue")
    public synchronized Folder clearAll()
    {
        trace("Clearing $", this);
        try
        {
            if (exists())
            {
                for (var file : files())
                {
                    file.delete();
                    if (file.exists())
                    {
                        warning("Unable to remove $", file);
                    }
                }
                for (var folder : folders())
                {
                    folder.clearAll();
                    folder.delete();
                }
            }
        }
        catch (Exception e)
        {
            warning("Unable to clear $", this);
        }
        return this;
    }

    /**
     * Clears this folder of files and folders, then deletes it
     */
    @SuppressWarnings("UnusedReturnValue")
    public Folder clearAllAndDelete()
    {
        trace("Clearing and deleting $", this);
        try
        {
            if (exists())
            {
                clearAll();
                delete();
            }
        }
        catch (Exception e)
        {
            warning("Unable to deleteAll on $", this);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull Folder that)
    {
        return name().compareTo(that.name());
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
     * Delete a folder if it is empty
     *
     * @return True if the folder was removed
     */
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean delete()
    {
        trace("Deleting $", this);
        return folderService().delete();
    }

    /**
     * Returns the {@link Disk} where this folder exists
     */
    @UmlRelation(label = "exists on")
    public Disk disk()
    {
        return new Disk(folderService().diskService());
    }

    /**
     * Creates this folder if it does not exist. If the folder cannot be created, an {@link IllegalStateException} is
     * thrown.
     */
    public Folder ensureExists()
    {
        trace("Ensuring that $ exists", this);
        synchronized (lock)
        {
            if (!exists())
            {
                mkdirs();
                if (!exists())
                {
                    fatal("Unable to create folder " + this);
                }
            }
            return this;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Folder that)
        {
            return path().equals(that.path());
        }
        return false;
    }

    /**
     * Returns true if this folder exists
     */
    @Override
    public boolean exists()
    {
        return folderService().exists();
    }

    /**
     * Returns the given file in this folder
     */
    public File file(@NotNull File file)
    {
        return file(file.path());
    }

    /**
     * Returns the named file in this folder
     *
     * @param name The filename
     * @return The file
     */
    public File file(@NotNull FileName name)
    {
        return new File(folderService().file(name));
    }

    /**
     * Returns the file with the given path relative to this folder
     *
     * @param pathed The path source
     * @return The file
     */
    @SuppressWarnings("SpellCheckingInspection")
    public File file(@NotNull ResourcePathed pathed)
    {
        var child = pathed.path().withoutRoot();

        // Get the filename from the path
        var fileName = child.fileName();

        // and if the parent is empty
        var parent = child.parent();
        if (parent.isEmpty())
        {
            // then it's a simple filename.
            return new File(folderService().file(fileName));
        }
        else
        {
            // Otherwise, append the parent path and filename to this folder
            return new File(folderService().folder(folder(parent)).file(fileName));
        }
    }

    /**
     * Returns the file at the given relative path
     *
     * @param path The path
     * @param arguments Any arguments to format the path
     * @return The file
     */
    public File file(@NotNull String path, Object... arguments)
    {
        return file(parseFilePath(this, path, arguments));
    }

    /**
     * Returns all files in this folder
     */
    public FileList files()
    {
        return files(acceptAll());
    }

    /**
     * Returns all files in this folder or in this folder recursively, depending on the value of the given traversal
     * parameter
     *
     * @param traversal The kind of traversal to make
     */
    public FileList files(@NotNull Matcher<ResourcePathed> matcher, @NotNull Traversal traversal)
    {
        return traversal == RECURSE
                ? nestedFiles(matcher)
                : files(matcher);
    }

    /**
     * Returns the files matching the given resource glob pattern
     *
     * @param globPattern The resource glob pattern
     * @return The matching files
     * @see ResourceGlob
     */
    public FileList files(@NotNull String globPattern)
    {
        return files(file -> glob(globPattern).matches(file));
    }

    /**
     * Returns a list of all files matching the given extension
     *
     * @param extension The extension
     * @return The list of files
     */
    public FileList files(@NotNull Extension extension)
    {
        return files(extension::matches);
    }

    /**
     * Returns a list of all files matching the given matcher
     *
     * @param matcher The path matcher
     * @return The list of files
     */
    public FileList files(@NotNull Matcher<ResourcePathed> matcher)
    {
        var files = new FileList();
        if (exists())
        {
            for (FileService service : folderService().files())
            {
                var file = new File(service);
                if (matcher.matches(file))
                {
                    files.add(file);
                }
            }
        }
        trace("Files in $: $", this, files);
        return files;
    }

    /**
     * Returns a list of all files matching the regular expression pattern
     *
     * @param pattern The regular expression pattern
     * @return The list of matching files
     */
    public FileList files(@NotNull Pattern pattern)
    {
        return files(value -> pattern.matcher(value.fileName().name()).matches());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder folder(@NotNull String child)
    {
        if (".".equals(child))
        {
            return this;
        }
        return folder(ensureNotNull(parseFolder(this, child)));
    }

    /**
     * Returns the folder in this folder with the given name
     *
     * @param child The filename of the child folder
     * @return The child folder
     */
    public Folder folder(@NotNull FileName child)
    {
        if (".".equals(child.name()))
        {
            return this;
        }
        return new Folder(ensureNotNull(folderService().folder(child)));
    }

    /**
     * Returns the given folder relative to this folder
     *
     * @param child The relative child folder
     * @return The child folder
     */
    public Folder folder(@NotNull Folder child)
    {
        if (child.path().isEmpty() || ".".equals(child.path().asString()))
        {
            return this;
        }
        return new Folder(folderService().folder(child));
    }

    /**
     * Returns a list of folders matching the given matcher
     *
     * @param matcher The matcher
     * @return The list of folders
     */
    public ObjectList<Folder> folders(@NotNull Matcher<Folder> matcher)
    {
        return ObjectList.list(folders().stream().filter(matcher::matches).collect(Collectors.toList()));
    }

    /**
     * Returns a list of all folders in this folder
     */
    @Override
    public ObjectList<Folder> folders()
    {
        var folders = new ObjectList<Folder>();
        if (service != null)
        {
            for (FolderService folder : folderService().folders())
            {
                folders.add(new Folder(folder));
            }
        }
        Collections.sort(folders);
        trace("Folders in $: $", this, folders);
        return folders;
    }

    /**
     * Returns true if the given folder has changed since the last time this method was called
     */
    public boolean hasChanged()
    {
        // Get the Java 'Preferences' node for this folder and from that, the previous folder digest,
        var node = Preferences.userNodeForPackage(getClass()).node("kivakit-folder:" + path().toString());
        var previousDigest = node.getByteArray("digest", null);

        // then create and save a new digest,
        var digest = nestedFiles().digest();
        node.putByteArray("digest", digest);
        if (tryCatch(UncheckedVoidCode.unchecked(node::flush), "Failed to flush preferences"))
        {
            // and if there is a previous digest,
            if (previousDigest != null)
            {
                // and return true if the digests are not equal.
                return !Arrays.equals(digest, previousDigest);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasTrailingSlash()
    {
        return path().hasTrailingSlash();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return path().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    {
        return exists()
                && files().isEmpty()
                && folders().isEmpty();
    }

    /**
     * Returns true if this is a folder
     */
    public boolean isFolder()
    {
        return folderService().isFolder();
    }

    /**
     * Returns true if this folder is on the local host
     */
    public boolean isLocal()
    {
        return !isRemote();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMaterialized()
    {
        return true;
    }

    /**
     * Returns true if this folder is on a remote host
     */
    public boolean isRemote()
    {
        return folderService().isRemote();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWritable()
    {
        return folderService().isWritable();
    }

    /**
     * Returns the last path component as a (relative) folder
     */
    public Folder last()
    {
        return parseFolder(this, path().last());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time lastModified()
    {
        return folderService().lastModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder mkdirs()
    {
        if (!exists())
        {
            trace("Creating folder $", this);
            folderService().mkdirs();
        }
        return this;
    }

    /**
     * Returns the filename of this folder
     */
    public FileName name()
    {
        return folderService().fileName();
    }

    /**
     * Returns a list of all files in this folder, recursively
     */
    public FileList nestedFiles()
    {
        return nestedFiles(value -> true);
    }

    /**
     * Returns any matching files that are recursively contained in this folder
     */
    public FileList nestedFiles(@NotNull Matcher<ResourcePathed> matcher)
    {
        var files = FileList.fileListForServices(folderService().nestedFiles(path -> matcher.matches((path.asFile()))));
        trace("Nested files in $: $", this, files);
        return files;
    }

    /**
     * Returns any matching folders that are recursively contained in this folder
     */
    @Override
    public FolderList nestedFolders(@NotNull Matcher<Folder> matcher)
    {
        var folders = FolderList.folderList(folderService().nestedFolders(path -> matcher.matches(new Folder(path))));
        trace("Nested folders in $: $", this, folders);
        return folders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolder<?> newFolder(@NotNull ResourcePath relativePath)
    {
        return new Folder(FilePath.filePath(relativePath));
    }

    /**
     * Returns the oldest file in this folder
     */
    public File oldest()
    {
        return oldest(acceptAll());
    }

    /**
     * Returns the oldest file in this folder matching the given matcher
     *
     * @param matcher The matcher
     */
    public File oldest(@NotNull Matcher<File> matcher)
    {
        File oldestFile = null;
        for (var file : files())
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder parent()
    {
        return new Folder(path().withoutLast());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilePath path()
    {
        return folderService().path();
    }

    /**
     * The relative path from this folder to the given folder
     *
     * @param folder The other folder
     * @return The relative path
     */
    public FilePath relativePath(@NotNull Folder folder)
    {
        return absolute().folderService().relativePath(folder.absolute().folderService());
    }

    /**
     * Returns the folder with the relative path from this folder to the given folder
     *
     * @param folder The given folder
     * @return The relative folder
     */
    public Folder relativeTo(@NotNull Folder folder)
    {
        return new Folder(relativePath(folder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(@NotNull ResourceFolder<?> that)
    {
        trace("Renaming $ to $", this, that);
        return folderService().renameTo(((Folder) that).folderService());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource resource(@NotNull ResourcePathed name)
    {
        return file(name.path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceFolderIdentifier resourceFolderIdentifier()
    {
        return ResourceFolder.resourceFolderIdentifier(path().join());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceList resources(@NotNull Matcher<ResourcePathed> matcher)
    {
        return resourceList(files()
                .stream()
                .filter(matcher)
                .collect(Collectors.toList()));
    }

    /**
     * The root folder for this folder
     */
    public Folder root()
    {
        return new Folder(path().root());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void safeCopyTo(@NotNull ResourceFolder<?> destination,
                           @NotNull WriteMode mode,
                           @NotNull Matcher<ResourcePathed> matcher,
                           @NotNull ProgressReporter reporter)
    {
        if (mode == WriteMode.DO_NOT_OVERWRITE && destination.exists() && !destination.isEmpty())
        {
            fail("Can't overwrite non-empty destination folder: $ ", destination);
        }

        destination.delete();
        if (destination.exists())
        {
            fail("Can't remove empty destination folder " + destination);
        }

        var start = Time.now();
        information("Safely copying $ to $", this, destination);
        var temporary = destination.parent().temporaryFolder(FileName.parseFileName(this, "temporary-copy"));
        for (var file : nestedFiles(matcher))
        {
            file.copyTo(temporary.resource(file.relativeTo(this)).asWritable(), mode, reporter);
        }
        temporary.renameTo(destination);
        information("Safe copy completed in $", start.elapsedSince());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void safeCopyTo(@NotNull ResourceFolder<?> destination,
                           @NotNull WriteMode mode,
                           @NotNull ProgressReporter reporter)
    {
        safeCopyTo(destination, mode, acceptAll(), reporter);
    }

    /**
     * Schedules this folder for removal on VM exit
     */
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
            catch (Exception ignored)
            {
            }
        }));

        return this;
    }

    /**
     * Returns the total number of bytes in this folder
     */
    public Bytes size()
    {
        return folderService().sizeInBytes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File temporaryFile(@NotNull FileName baseName)
    {
        return temporaryFile(baseName, TEMPORARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File temporaryFile(@NotNull FileName baseName, @NotNull Extension extension)
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
            file.writer().saveText("");
            return file;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder temporaryFolder(@NotNull FileName baseName)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return folderService().toString();
    }

    /**
     * Returns the type of this folder
     */
    @UmlExcludeMember
    public FolderType type()
    {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI uri()
    {
        return path().uri();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Folder withTrailingSlash()
    {
        if (hasTrailingSlash())
        {
            return this;
        }
        return new Folder(path().withChild(""));
    }

    /**
     * Returns this folder without any trailing slash (empty element at the end)
     */
    public Folder withoutTrailingSlash()
    {
        if (hasTrailingSlash())
        {
            return new Folder(path().withoutTrailingSlash());
        }
        return this;
    }

    FolderService service()
    {
        return service;
    }

    private @NotNull FileSystemService fileSystemService(@NotNull FilePath path)
    {
        return ensureNotNull(fileSystem(throwingListener(), path));
    }

    private FolderService folderService()
    {
        if (service == null)
        {
            service = fileSystemService(path).folderService(path);
        }
        return service;
    }
}
