package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * Locations of well-known folders, and argument and switch parser builders for folders.
 *
 * <p><b>Well-Known Folders</b></p>
 *
 * <ul>
 *     <li>{@link #currentFolder()}</li>
 *     <li>{@link #desktopFolder()}</li>
 *     <li>{@link #kivakitCacheFolder()}</li>
 *     <li>{@link #kivakitExtensionsHome()}</li>
 *     <li>{@link #kivakitHome()}</li>
 *     <li>{@link #kivakitTemporaryFolder()}</li>
 *     <li>{@link #kivakitTestFolder(Class)}</li>
 * </ul>
 *
 * <p><b>Command Line Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #folderArgumentParser(Listener, String)}</li>
 *     <li>{@link #folderListArgumentParser(Listener, String)}</li>
 *     <li>{@link #folderSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #folderListSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public class Folders
{
    /**
     * Returns the current folder for this process
     */
    public static Folder currentFolder()
    {
        try
        {
            return Folder.parseFolder(throwingListener(), new java.io.File(".").getCanonicalPath());
        }
        catch (IOException e)
        {
            throw new Problem(e, "Can't get working folder").asException();
        }
    }

    /**
     * Returns the desktop folder
     */
    public static Folder desktopFolder()
    {
        return userHome().folder("Desktop");
    }

    /**
     * Returns a {@link Folder} argument parser builder with the given description
     *
     * @param listener The listener to notify of any problems
     * @param description The description of the argument
     * @return The parser builder
     */
    public static ArgumentParser.Builder<Folder> folderArgumentParser(@NotNull Listener listener,
                                                                      @NotNull String description)
    {
        return argumentParser(Folder.class)
                .converter(new Folder.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link FolderList} argument parser builder with the given description
     *
     * @param listener The listener to notify of any problems
     * @param description The description of the argument
     * @return The parser builder
     */
    public static ArgumentParser.Builder<FolderList> folderListArgumentParser(@NotNull Listener listener,
                                                                              @NotNull String description)
    {
        return argumentParser(FolderList.class)
                .converter(new FolderList.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link FolderList} switch parser builder with the given name and description
     *
     * @param listener The listener to notify of any problems
     * @param name The name of the switch
     * @param description The description of the switch
     * @return The parser builder
     */
    public static SwitchParser.Builder<FolderList> folderListSwitchParser(@NotNull Listener listener,
                                                                          @NotNull String name,
                                                                          @NotNull String description)
    {
        return SwitchParser.switchParser(FolderList.class)
                .name(name)
                .converter(new FolderList.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link Folder} switch parser builder with the given name and description
     *
     * @param listener The listener to notify of any problems
     * @param description The description of the switch
     * @return The parser builder
     */
    public static SwitchParser.Builder<Folder> folderSwitchParser(@NotNull Listener listener,
                                                                  @NotNull String name,
                                                                  @NotNull String description)
    {
        return SwitchParser.switchParser(Folder.class)
                .name(name)
                .converter(new Folder.Converter(listener))
                .description(description);
    }

    /**
     * Returns the KivaKit cache folder
     */
    public static Folder kivakitCacheFolder()
    {
        return Folder.folder(resolveProject(KivaKit.class).kivakitCacheFolderPath()).mkdirs();
    }

    /**
     * Returns the kivakit-extensions home folder
     */
    public static Folder kivakitExtensionsHome()
    {
        return kivakitHome().parent().folder("kivakit-extensions");
    }

    /**
     * Returns the KivaKit home folder
     */
    public static Folder kivakitHome()
    {
        var home = resolveProject(KivaKit.class).kivakitHomeFolderPath();
        if (home != null)
        {
            return Folder.folder(home);
        }
        return fail("Cannot find KivaKit home folder");
    }

    /**
     * Returns the KivaKit temporary folder
     */
    public static Folder kivakitTemporaryFolder()
    {
        return kivakitCacheFolder().folder("temporary").mkdirs();
    }

    /**
     * Returns the test folder in the KivaKit temporary folder
     *
     * @param type The type to use as a sub-folder name
     * @return The folder
     */
    public static Folder kivakitTestFolder(@NotNull Class<?> type)
    {
        return kivakitTemporaryFolder().folder("test").folder(type.getSimpleName()).mkdirs();
    }

    /**
     * Returns the user's home folder
     */
    public static Folder userHome()
    {
        return Folder.parseFolder(throwingListener(), System.getProperty("user.home"));
    }
}
