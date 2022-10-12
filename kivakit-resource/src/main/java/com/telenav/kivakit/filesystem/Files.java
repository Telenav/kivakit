package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.Extension;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.commandline.SwitchParser.switchParser;

/**
 * <p><b>Command Line Parsing</b></p>
 *
 * <p>
 * Factory methods that produce switch and argument builders are available here.
 * </p>
 *
 * <ul>
 *     <li>{@link #fileArgumentParser(Listener, String)}</li>
 *     <li>{@link #fileListArgumentParser(Listener, String, Extension)}</li>
 *     <li>{@link #fileListSwitchParser(Listener, String, String, Extension)}</li>
 *     <li>{@link #filePathSwitchParser(Listener, String, String)}</li>
 *     <li>{@link #fileSwitchParser(Listener, String, String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see File
 * @see FileList
 * @see ArgumentParser
 * @see SwitchParser
 */
@SuppressWarnings("unused")
public class Files
{
    /**
     * Returns a file argument parser builder with the given description
     *
     * @param listener The listener to call with any problems
     * @param description The argument description
     * @return The argument parser builder
     */
    public static ArgumentParser.Builder<File> fileArgumentParser(@NotNull Listener listener,
                                                                  @NotNull String description)
    {
        return argumentParser(File.class)
                .converter(new File.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link FileList} argument parser builder with the given description
     *
     * @param listener The listener to call with any problems
     * @param description The argument description
     * @param extension The extension to match for files in the file list
     * @return The argument parser builder
     */
    public static ArgumentParser.Builder<FileList> fileListArgumentParser(@NotNull Listener listener,
                                                                          @NotNull String description,
                                                                          @NotNull Extension extension)
    {
        return argumentParser(FileList.class)
                .converter(new FileList.Converter(listener, extension))
                .description(description);
    }

    /**
     * Returns a {@link FileList} switch parser builder with the given name and description
     *
     * @param listener The listener to call with any problems
     * @param name The switch name
     * @param description The switch description
     * @param extension The extension to match for files in the file list
     * @return The switch parser builder
     */
    public static SwitchParser.Builder<FileList> fileListSwitchParser(@NotNull Listener listener,
                                                                      @NotNull String name,
                                                                      @NotNull String description,
                                                                      @NotNull Extension extension)
    {
        return switchParser(FileList.class)
                .name(name)
                .converter(new FileList.Converter(listener, extension))
                .description(description);
    }

    /**
     * Returns a {@link FilePath} switch parser builder with the given name and description
     *
     * @param listener The listener to call with any problems
     * @param name The switch name
     * @param description The switch description
     * @return The switch parser builder
     */
    public static SwitchParser.Builder<FilePath> filePathSwitchParser(@NotNull Listener listener,
                                                                      @NotNull String name,
                                                                      @NotNull String description)
    {
        return switchParser(FilePath.class)
                .name(name)
                .converter(new FilePath.Converter(listener))
                .description(description);
    }

    /**
     * Returns a {@link File} switch parser builder with the given name and description
     *
     * @param listener The listener to call with any problems
     * @param name The switch name
     * @param description The switch description
     * @return The switch parser builder
     */
    public static SwitchParser.Builder<File> fileSwitchParser(@NotNull Listener listener,
                                                              @NotNull String name,
                                                              @NotNull String description)
    {
        return switchParser(File.class)
                .name(name)
                .converter(new File.Converter(listener))
                .description(description);
    }
}
