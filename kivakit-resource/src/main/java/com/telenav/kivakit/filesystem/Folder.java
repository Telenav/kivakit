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
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.code.UncheckedVoidCode;
import com.telenav.kivakit.core.language.trait.TryTrait;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.core.thread.Monitor;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourceFolderIdentifier;
import com.telenav.kivakit.resource.ResourceList;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemFolder;
import com.telenav.kivakit.resource.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.spi.ResourceFolderResolver;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.project.Project.resolveProject;
import static com.telenav.kivakit.filesystem.Folder.Traversal.RECURSE;
import static com.telenav.kivakit.filesystem.Folder.Type.CLEAN_UP_ON_EXIT;
import static com.telenav.kivakit.filesystem.loader.FileSystemServiceLoader.fileSystem;
import static com.telenav.kivakit.resource.Extension.TMP;
import static com.telenav.kivakit.resource.ResourceGlob.match;
import static com.telenav.kivakit.resource.ResourceList.resourceList;

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
 *     <li>{@link #parse(Listener, String, Object...)} - A folder for the given string</li>
 *     <li>{@link #folder(URI)} - A folder for the given URI</li>
 *     <li>{@link #folder(URL)} - A folder for the given URL</li>
 *     <li>{@link #folder(java.io.File)} - A folder for the given Java file</li>
 *     <li>{@link #folder(Path)} - A folder for the given NIO path</li>
 * </ul>
 *
 * <p><b>Locations</b></p>
 *
 * <ul>
 *     <li>{@link #current()} - The current working folder</li>
 *     <li>{@link #desktop()} - The desktop folder</li>
 *     <li>{@link #userHome()} - The user's home folder</li>
 *     <li>{@link #kivakitHome()} - The KivaKit home folder</li>
 *     <li>{@link #kivakitCache()} - The KivaKit cache folder, normally ~/.kivakit</li>
 *     <li>{@link #kivakitTemporary()} - Folder where temporary files can be created</li>
 *     <li>{@link #kivakitTest(Class)} - Folder for test files for the given class</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #name()} - The filename of this folder</li>
 *     <li>{@link #disk()} - The disk where this folder exists, if any</li>
 *     <li>{@link #modifiedAt()} - The last time this folder was modified</li>
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
 *     <li>{@link #temporary(FileName)} - A temporary file in this folder with the given name</li>
 *     <li>{@link #temporary(FileName, Extension)} - A temporary file in this folder with the given name and extension</li>
 *     <li>{@link #temporaryFolder(FileName)} - A temporary sub-folder with the given name</li>
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
 *     <li>{@link #file(String, Object...)} - The file with the given name in this folder</li>
 *     <li>{@link #file(FileName)} - The file with the given name in this folder</li>
 *     <li>{@link #file(ResourcePathed)} - The file with the given relative path to this folder</li>
 *     <li>{@link #folder(Folder)} - The folder relative to this folder</li>
 *     <li>{@link #folder(String)} - The folder with the given name in this folder</li>
 *     <li>{@link #folder(FileName)} - The folder in this folder with the given filename </li>
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
@LexakaiJavadoc(complete = true)
public class Folder extends BaseRepeater implements
        FileSystemObject,
        Comparable<Folder>,
        ResourceFolder<Folder>,
        TryTrait
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** A flag to make sure the temporary folder will be created only once per process */
    private static boolean temporaryForProcessInitialized;

    /** Monitor for serializing the creation of temporary files */
    private static final Monitor temporaryLock = new Monitor();

    private static final Monitor lock = new Monitor();

    public static Folder current()
    {
        try
        {
            return parse(Listener.throwingListener(), new java.io.File(".").getCanonicalPath());
        }
        catch (IOException e)
        {
            throw new Problem(e, "Can't get working folder").asException();
        }
    }

    public static Folder desktop()
    {
        return userHome().folder("Desktop");
    }

    public static Folder folder(Path path)
    {
        return parse(Listener.throwingListener(), path.toString());
    }

    public static Folder folder(StringPath path)
    {
        return parse(Listener.throwingListener(), path.toString());
    }

    public static Folder folder(java.io.File file)
    {
        return folder(file.toPath());
    }

    public static Folder folder(URI uri)
    {
        return folder(new java.io.File(uri));
    }

    public static Folder folder(URL url)
    {
        try
        {
            return folder(url.toURI());
        }
        catch (URISyntaxException e)
        {
            new Problem(e, "Unable to parse URL: $", url).throwAsIllegalStateException();
            return null;
        }
    }

    public static ArgumentParser.Builder<Folder> folderArgumentParser(Listener listener,
                                                                      String description)
    {
        return ArgumentParser.builder(Folder.class)
                .converter(new Folder.Converter(listener))
                .description(description);
    }

    public static ArgumentParser.Builder<FolderList> folderListArgumentParser(Listener listener,
                                                                              String description)
    {
        return ArgumentParser.builder(FolderList.class)
                .converter(new FolderList.Converter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<FolderList> folderListSwitchParser(Listener listener,
                                                                          String name, String description)
    {
        return SwitchParser.builder(FolderList.class)
                .name(name)
                .converter(new FolderList.Converter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Folder> folderSwitchParser(Listener listener,
                                                                  String name, String description)
    {
        return SwitchParser.builder(Folder.class)
                .name(name)
                .converter(new Folder.Converter(listener))
                .description(description);
    }

    public static SwitchParser.Builder<Folder> inputFolderSwitchParser(Listener listener)
    {
        return folderSwitchParser(listener, "input-folder", "Input folder to process");
    }

    public static boolean isFolder(FilePath path)
    {
        return new java.io.File(path.join()).isDirectory();
    }

    public static Folder kivakitCache()
    {
        return Folder.folder(resolveProject(KivaKit.class).cacheFolderPath()).mkdirs();
    }

    public static Folder kivakitExtensionsHome()
    {
        return kivakitHome().parent().folder("kivakit-extensions");
    }

    public static Folder kivakitHome()
    {
        var home = resolveProject(KivaKit.class).homeFolderPath();
        if (home != null)
        {
            return Folder.folder(home);
        }
        return fail("Cannot find KivaKit home folder");
    }

    public static Folder kivakitTemporary()
    {
        return kivakitCache().folder("temporary").mkdirs();
    }

    public static Folder kivakitTest(Class<?> type)
    {
        return kivakitTemporary().folder("test").folder(type.getSimpleName()).mkdirs();
    }

    public static Folder of(FilePath path)
    {
        return parse(Listener.throwingListener(), path.toString());
    }

    // Note that this switch parser ensures that the folder exists
    public static SwitchParser.Builder<Folder> outputFolderSwitchParser(Listener listener)
    {
        return SwitchParser.builder(Folder.class)
                .name("output-folder")
                .converter(new Folder.Converter(listener, true))
                .description("Output folder to write to");
    }

    public static Folder parse(String path, Object... arguments)
    {
        return parse(Listener.throwingListener(), path, arguments);
    }

    public static Folder parse(Listener listener, String path, Object... arguments)
    {
        if (Strings.isEmpty(path))
        {
            return null;
        }
        path = Strings.format(path, arguments);
        var filePath = FilePath.parseFilePath(listener, path);
        return filePath == null ? null : new Folder(filePath);
    }

    /**
     * @return unique temporary folder per process
     */
    public static Folder temporaryForProcess(Type type)
    {
        synchronized (temporaryLock)
        {
            var name = "kivakit-process-" + OperatingSystem.get().processIdentifier();
            var temporary = kivakitTemporary()
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
        return Folder.parse(Listener.throwingListener(), System.getProperty("user.home"));
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
     * Converts to and from {@link Folder}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Folder>
    {
        private boolean ensureExists;

        public Converter(Listener listener)
        {
            super(listener);
        }

        public Converter(Listener listener, boolean ensureExists)
        {
            super(listener);
            this.ensureExists = ensureExists;
        }

        @Override
        protected Folder onToValue(String value)
        {
            var path = FilePath.parseFilePath(this, value);
            var folder = new Folder(path);
            if (ensureExists)
            {
                folder.ensureExists();
            }
            return folder;
        }
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
        public boolean accepts(ResourceFolderIdentifier identifier)
        {
            return Folder.parse(this, identifier.identifier()) != null;
        }

        @Override
        public Folder resolve(ResourceFolderIdentifier identifier)
        {
            return Folder.parse(this, identifier.identifier());
        }
    }

    private FilePath path;

    @UmlAggregation(label = "delegates to")
    private transient FolderService service;

    private Type type = Type.NORMAL;

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
    public Folder(FolderService service)
    {
        ensureNotNull(service);

        this.service = service;
        path = service.path();

        assert path != null;
    }

    /**
     * <b>Not public API</b>
     */
    public Folder(FilePath path)
    {
        this(Objects.requireNonNull(fileSystem(Listener.throwingListener(), path)).folderService(path));
    }

    /**
     * This folder as an absolute path with a trailing slash on it
     */
    @Override
    public Folder absolute()
    {
        return new Folder(path().absolute()).withTrailingSlash();
    }

    public URI asUri()
    {
        try
        {
            var path = path();
            if (!path.hasScheme())
            {
                path = path.withScheme("file");
            }
            return new URI(Strings.ensureEndsWith(path.toString(), "/"));
        }
        catch (Exception e)
        {
            throw problem(e, "Cannot convert to URI: $", this).asException();
        }
    }

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

    @SuppressWarnings("UnusedReturnValue")
    public boolean chmod(PosixFilePermission... permissions)
    {
        return folder().chmod(permissions);
    }

    public void chmodNested(PosixFilePermission... permissions)
    {
        nestedFolders(Filter.all()).forEach(folder -> folder().chmod(permissions));
        nestedFiles(Filter.all()).forEach(file -> file.chmod(permissions));
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

    @Override
    public int compareTo(@NotNull Folder that)
    {
        return name().compareTo(that.name());
    }

    /**
     * Copies all nested files matching the given matcher from this folder to the destination folder.
     */
    public void copyTo(Folder destination,
                       CopyMode mode,
                       Matcher<File> matcher,
                       ProgressReporter reporter)
    {
        var start = Time.now();

        // Ensure the destination folder exists,
        information("Copying $ to $", this, destination);
        destination.ensureExists();

        // then for each nested file,
        for (var file : nestedFiles(matcher))
        {
            // make relative target file,
            var target = destination.file(file.relativeTo(this));

            // and if we can copy to it,
            if (mode.canCopy(file, target))
            {
                // then copy the file and update its last modified timestamp to the source timestamp
                file.copyTo(target.ensureWritable(), mode, reporter);
                target.lastModified(file.modifiedAt());
            }
        }
        information("Copy completed in $", start.elapsedSince());
    }

    /**
     * Copies all nested files from this folder to the destination folder
     */
    public void copyTo(Folder destination, CopyMode mode, ProgressReporter reporter)
    {
        copyTo(destination, mode, Filter.all(), reporter);
    }

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

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Folder)
        {
            var that = (Folder) object;
            return path().equals(that.path());
        }
        return false;
    }

    /**
     * @return True if this folder exists
     */
    @Override
    public boolean exists()
    {
        return folder().exists();
    }

    public File file(File file)
    {
        return file(file.path());
    }

    public File file(FileName name)
    {
        return new File(folder().file(name));
    }

    public File file(ResourcePathed pathed)
    {
        var child = pathed.path().withoutRoot();

        // Get the filename from the path
        var fileName = child.fileName();

        // and if the parent is empty
        var parent = child.parent();
        if (parent.isEmpty())
        {
            // then it's a simple filename.
            return new File(folder().file(fileName));
        }
        else
        {
            // Otherwise, append the parent path and filename to this folder
            return new File(folder().folder(Folder.folder(parent)).file(fileName));
        }
    }

    public File file(String path, Object... arguments)
    {
        return file(FilePath.parseFilePath(this, path, arguments));
    }

    public FileList files()
    {
        return files(Filter.all());
    }

    public FileList files(Matcher<File> matcher, Traversal recurse)
    {
        return recurse == RECURSE ? nestedFiles(matcher) : files(matcher);
    }

    public FileList files(String globPattern)
    {
        return files(file -> match(globPattern).matches(file));
    }

    public FileList files(Extension extension)
    {
        return files(extension.fileMatcher());
    }

    public FileList files(Matcher<File> matcher)
    {
        var files = new FileList();
        if (exists())
        {
            for (FileService service : folder().files())
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

    public FileList files(Pattern pattern)
    {
        return files(value -> pattern.matcher(value.fileName().name()).matches());
    }

    @Override
    public Folder folder(String child)
    {
        if (child.equals("."))
        {
            return this;
        }
        var childFolder = Folder.parse(this, child);
        return childFolder == null ? null : folder(childFolder);
    }

    public Folder folder(FileName child)
    {
        if (child.name().equals("."))
        {
            return this;
        }
        return new Folder(folder().folder(child));
    }

    public Folder folder(Folder child)
    {
        if (child.path().isEmpty() || child.path().asString().equals("."))
        {
            return this;
        }
        return new Folder(folder().folder(child));
    }

    public List<Folder> folders(Matcher<Folder> matcher)
    {
        return folders().stream().filter(matcher::matches).collect(Collectors.toList());
    }

    @Override
    public List<Folder> folders()
    {
        var folders = new ArrayList<Folder>();
        if (service != null)
        {
            for (FolderService folder : folder().folders())
            {
                folders.add(new Folder(folder));
            }
        }
        Collections.sort(folders);
        trace("Folders in $: $", this, folders);
        return folders;
    }

    /**
     * @return True if the given folder has changed since the last time this method was called
     */
    public boolean hasChanged()
    {
        // Get the Java 'Preferences' node for this folder and from that, the previous folder digest,
        var node = Preferences.userNodeForPackage(getClass()).node("kivakit-folder:" + path().toString());
        var previousDigest = node.getByteArray("digest", null);

        // then create and save a new digest,
        var digest = nestedFiles().digest();
        node.putByteArray("digest", digest);
        if (tryCatch(UncheckedVoidCode.of(node::flush), "Failed to flush preferences"))
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

    @Override
    public boolean hasTrailingSlash()
    {
        return path().hasTrailingSlash();
    }

    @Override
    public int hashCode()
    {
        return path().hashCode();
    }

    @Override
    public ResourceFolderIdentifier identifier()
    {
        return ResourceFolder.identifier(path().join());
    }

    @Override
    public boolean isEmpty()
    {
        return exists()
                && files().isEmpty()
                && folders().isEmpty();
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
        return folder().isRemote();
    }

    @Override
    public boolean isWritable()
    {
        return folder().isWritable();
    }

    public Folder last()
    {
        return Folder.parse(withoutTrailingSlash(), path().last());
    }

    @Override
    public Folder mkdirs()
    {
        if (!exists())
        {
            trace("Creating folder $", this);
            folder().mkdirs();
        }
        return this;
    }

    @Override
    public Time modifiedAt()
    {
        return folder().modifiedAt();
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
    public FileList nestedFiles(Matcher<File> matcher)
    {
        var files = FileList.forServices(folder().nestedFiles(path -> matcher.matches((path.asFile()))));
        trace("Nested files in $: $", this, files);
        return files;
    }

    /**
     * @return Any matching folders that are recursively contained in this folder
     */
    @Override
    public FolderList nestedFolders(Matcher<Folder> matcher)
    {
        var folders = FolderList.forVirtual(folder().nestedFolders(path -> matcher.matches(new Folder(path))));
        trace("Nested folders in $: $", this, folders);
        return folders;
    }

    @Override
    public ResourceFolder<?> newFolder(ResourcePath relativePath)
    {
        return new Folder(FilePath.filePath(relativePath));
    }

    public File oldest()
    {
        return oldest(Filter.all());
    }

    public File oldest(Matcher<File> matcher)
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

    @Override
    public Folder parent()
    {
        return new Folder(path().absolute().parent());
    }

    @Override
    public FilePath path()
    {
        return folder().path();
    }

    public FilePath relativePath(Folder folder)
    {
        return absolute().folder().relativePath(folder.absolute().folder());
    }

    public Folder relativeTo(Folder folder)
    {
        return new Folder(relativePath(folder));
    }

    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(ResourceFolder<?> that)
    {
        trace("Renaming $ to $", this, that);
        return folder().renameTo(((Folder) that).folder());
    }

    @Override
    public Resource resource(ResourcePathed name)
    {
        return file(name.path());
    }

    @Override
    public ResourceList resources(Matcher<? super Resource> matcher)
    {
        return resourceList(files()
                .stream()
                .filter(matcher.asPredicate())
                .collect(Collectors.toList()));
    }

    public Folder root()
    {
        return new Folder(path().root());
    }

    public void safeCopyTo(ResourceFolder<?> destination,
                           CopyMode mode,
                           Matcher<File> matcher,
                           ProgressReporter reporter)
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

    @Override
    public void safeCopyTo(ResourceFolder<?> destination, CopyMode mode, ProgressReporter reporter)
    {
        safeCopyTo(destination, mode, Filter.all(), reporter);
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
            catch (Exception ignored)
            {
            }
        }));

        return this;
    }

    public Bytes size()
    {
        return folder().sizeInBytes();
    }

    @Override
    public File temporary(FileName baseName)
    {
        return temporary(baseName, TMP);
    }

    @Override
    public File temporary(FileName baseName, Extension extension)
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

    @Override
    public Folder temporaryFolder(FileName baseName)
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

    @Override
    public URI uri()
    {
        return path().uri();
    }

    @Override
    public Folder withTrailingSlash()
    {
        if (hasTrailingSlash())
        {
            return this;
        }
        return new Folder(path().withChild(""));
    }

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

    private @NotNull FileSystemService fileSystemService(FilePath path)
    {
        return ensureNotNull(fileSystem(Listener.throwingListener(), path));
    }

    private FolderService folder()
    {
        if (service == null)
        {
            service = fileSystemService(path).folderService(path);
        }
        return service;
    }
}
