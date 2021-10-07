package com.telenav.kivakit.kernel.language.paths;

import com.telenav.kivakit.kernel.language.strings.Paths;
import com.telenav.kivakit.kernel.messaging.Listener;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Nio
{
    private static final Map<String, FileSystem> filesystemForUri = new HashMap<>();

    public static void close(final FileSystem filesystem)
    {
        for (var key : new HashSet<>(filesystemForUri.keySet()))
        {
            var existing = filesystemForUri.get(key);
            if (existing != null)
            {
                filesystemForUri.remove(key);
            }
        }
    }

    public static List<Path> filesAndFolders(Listener listener, Path path)
    {
        final var files = new ArrayList<Path>();
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
        catch (final Exception e)
        {
            listener.problem(e, "Unable to get list of files in: $", path);
        }

        return files;
    }

    public static FileSystem filesystem(Listener listener, URI uri)
    {
        return filesystem(listener, uri, new HashMap<>());
    }

    public static FileSystem filesystem(Listener listener, URI uri, Map<String, String> variables)
    {
        var key = Paths.head(uri.toString(), "!/");
        if (key == null)
        {
            key = uri.toString();
        }
        var filesystem = filesystemForUri.get(key);
        if (filesystem == null)
        {
            try
            {
                filesystem = FileSystems.newFileSystem(uri, variables);
                filesystemForUri.put(key, filesystem);
            }
            catch (Exception e)
            {
                // listener.problem(e, "Unable to create filesystem for: $", uri);
                return null;
            }
        }
        return filesystem;
    }
}
