package com.telenav.kivakit.core.io;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.string.Paths.pathHead;
import static java.nio.file.FileSystems.newFileSystem;

/**
 * Convenience functions for working with {@link FileSystem}s:
 *
 * <ul>
 *     <li>{@link #filesystem(Listener, URI)}</li>
 *     <li>{@link #filesystem(Listener, URI, Map)}</li>
 *     <li>{@link #close(Listener, FileSystem)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class Nio
{
    /** Map from URI string to filesystem */
    private static final Map<String, FileSystem> filesystemForUri = new HashMap<>();

    /** Closes the given filesystem */
    public static void close(Listener listener, @NotNull FileSystem filesystem)
    {
        for (var key : new HashSet<>(filesystemForUri.keySet()))
        {
            var existing = filesystemForUri.get(key);
            if (existing != null)
            {
                //noinspection resource
                filesystemForUri.remove(key);
                try
                {
                    filesystem.close();
                }
                catch (IOException e)
                {
                    listener.problem(e, "Unable to close filesystem");
                }
            }
        }
    }

    public static List<Path> filesAndFolders(Listener listener, Path path)
    {
        var files = new ArrayList<Path>();
        try
        {
            try (var stream = Files.newDirectoryStream(path))
            {
                for (var at : stream)
                {
                    files.add(at);
                }
            }
        }
        catch (Exception e)
        {
            listener.problem(e, "Unable to get list of files in: $", path);
        }

        return files;
    }

    /**
     * Gets the filesystem for the given URI
     *
     * @param listener The listener to report errors to
     * @param uri The URI
     * @return The filesystem
     */
    public static FileSystem filesystem(Listener listener, URI uri)
    {
        return filesystem(listener, uri, new HashMap<>());
    }

    /**
     * Gets the filesystem for the given URI
     *
     * @param listener The listener to report errors to
     * @param uri The URI
     * @param variables Variables passed to the filesystem
     * @return The filesystem
     */
    public static FileSystem filesystem(Listener listener, URI uri, Map<String, String> variables)
    {
        var key = pathHead(uri.toString(), "!/");
        if (key == null)
        {
            key = uri.toString();
        }
        var filesystem = filesystemForUri.get(key);
        if (filesystem == null)
        {
            try
            {
                filesystem = newFileSystem(uri, variables);
                filesystemForUri.put(key, filesystem);
            }
            catch (Exception e)
            {
                listener.problem(e, "Unable to create filesystem for: $", uri);
                return null;
            }
        }
        return filesystem;
    }
}
