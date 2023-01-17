package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.filesystem.FilePath.parseFilePath;

/**
 * Converts to and from {@link Folder}s
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FolderConverter extends BaseStringConverter<Folder>
{
    private boolean ensureExists;

    public FolderConverter(@NotNull Listener listener)
    {
        super(listener, Folder.class);
    }

    public FolderConverter(@NotNull Listener listener, boolean ensureExists)
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
