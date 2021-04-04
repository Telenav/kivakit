////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.commandline.ArgumentParser;
import com.telenav.kivakit.core.commandline.SwitchParser;
import com.telenav.kivakit.core.filesystem.loader.FileSystemServiceLoader;
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.language.threading.locks.Monitor;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.filters.operators.All;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFolder;
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
 * Folder abstraction that adds type safety and various helpful methods. Support for various file systems is implemented
 * in kivakit-filesystems.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
public class Folder implements FileSystemObject, Comparable<Folder>
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

    public static Folder folder(final Path path)
    {
        return parse(path.toString());
    }

    public static Folder folder(final java.io.File file)
    {
        return folder(file.toPath());
    }

    public static Folder folder(final URI uri)
    {
        return folder(new java.io.File(uri));
    }

    public static Folder folder(final URL url)
    {
        try
        {
            return folder(url.toURI());
        }
        catch (final URISyntaxException e)
        {
            LOGGER.problem(e, "Unable to parse URL: $", url);
            return null;
        }
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

    public static Folder fromSystemProperty(final String key)
    {
        final var path = JavaVirtualMachine.property(key);
        return path == null ? null : parse(path);
    }

    public static boolean isFolder(final FilePath path)
    {
        return new java.io.File(path.join()).isDirectory();
    }

    public static Folder of(final FileName name)
    {
        return parse(name.asPath().asString());
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

    public static Folder kivakitCacheFolder()
    {
        return Folder.folder(KivaKit.get().cacheFolderPath()).mkdirs();
    }

    public static Folder kivakitConfigurationFolder()
    {
        return kivakitCacheFolder().folder("configuration");
    }

    public static Folder kivakitHome()
    {
        final var home = KivaKit.get().homeFolderPath();
        if (home != null)
        {
            return Folder.folder(home);
        }
        return fail("Cannot find KivaKit home folder");
    }

    public static Folder kivakitTemporaryFolder()
    {
        return kivakitCacheFolder().folder("temporary").mkdirs();
    }

    /**
     * @return unique temporary folder per process
     */
    public static Folder temporaryForProcess(final Type type)
    {
        synchronized (temporaryLock)
        {
            final var name = "kivakit-process-" + OperatingSystem.get().processIdentifier();
            final var temporary = kivakitTemporaryFolder().folder("processes").folder(name).mkdirs();
            temporary.type = type;
            if (type == CLEAN_UP_ON_EXIT)
            {
                temporary.scheduleCleanUpOnExit();
            }
            if (!temporaryForProcessInitialized)
            {
                temporaryForProcessInitialized = true;

                // Clear existing folder if it exists
                temporary.clear();
                temporary.mkdirs();
            }
            return temporary;
        }
    }

    public static Folder unitTestOutput(final Class<?> type)
    {
        return kivakitTemporaryFolder().folder("test").folder(type.getSimpleName()).mkdirs();
    }

    public static Folder userHome()
    {
        return Folder.parse(System.getProperty("user.home"));
    }

    public enum Traversal
    {
        RECURSE,
        FLAT
    }

    public enum Type
    {
        NORMAL,
        CLEAN_UP_ON_EXIT
    }

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
    private transient FolderService serviceFolder;

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
    public Folder(final FolderService serviceFolder)
    {
        ensureNotNull(serviceFolder);

        this.serviceFolder = serviceFolder;
        path = serviceFolder.path();

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

    @SuppressWarnings("UnusedReturnValue")
    public synchronized Folder clear()
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
                    folder.clear();
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
     * Clears this folder of files, then deletes it
     */
    @SuppressWarnings("UnusedReturnValue")
    public Folder clearAndDelete()
    {
        DEBUG.trace("Clearing and deleting $", this);
        try
        {
            if (exists())
            {
                clear();
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
        if (child.name().name().equals("."))
        {
            return this;
        }
        return new Folder(folder().folder(child));
    }

    public Folder folder(final String child)
    {
        if (child.equals("."))
        {
            return this;
        }
        final var childFolder = Folder.parse(child);
        return childFolder == null ? null : folder(childFolder);
    }

    public List<Folder> folders(final Matcher<Folder> matcher)
    {
        return folders().stream().filter(matcher::matches).collect(Collectors.toList());
    }

    public List<Folder> folders()
    {
        final List<Folder> folders = new ArrayList<>();
        if (serviceFolder != null)
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

    public boolean isRemote()
    {
        final var scheme = path().scheme();
        return "hdfs".equalsIgnoreCase(scheme) || "s3".equalsIgnoreCase(scheme);
    }

    public boolean isWritable()
    {
        return folder().isWritable();
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

    @UmlExcludeMember
    public boolean renameTo(final FolderService that)
    {
        DEBUG.trace("Renaming $ to $", this, that);
        return folder().renameTo(that);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean renameTo(final Folder that)
    {
        DEBUG.trace("Renaming $ to $", this, that);
        return folder().renameTo(that.folder());
    }

    @UmlExcludeMember
    public FileSystemObjectService resolve()
    {
        return serviceFolder;
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
        final var temporary = destination.parent().temporaryFolder(new FileName("temporary-copy"));
        for (final var file : nestedFiles(matcher))
        {
            file.copyTo(temporary.file(file.relativeTo(this)), mode, reporter);
        }
        temporary.renameTo(destination);
        LOGGER.information("Safe copy completed in $", start.elapsedSince());
    }

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
                        clearAndDelete();
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

    public FolderChangeWatcher watch(final Frequency frequency)
    {
        return LOGGER.listenTo(new FolderChangeWatcher(this, frequency));
    }

    FolderService virtualFolder()
    {
        return serviceFolder;
    }

    private FolderService folder()
    {
        if (serviceFolder == null)
        {
            serviceFolder = FileSystemServiceLoader.fileSystem(path).folderService(path);
        }
        return serviceFolder;
    }
}
