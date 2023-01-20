package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.filesystem.Folder.parseFolder;

/**
 * Converts to and from folder lists separated by commas
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FolderListConverter extends BaseStringConverter<FolderList>
{
    public FolderListConverter(@NotNull Listener listener)
    {
        super(listener, FolderList.class);
    }

    @Override
    protected FolderList onToValue(String value)
    {
        var folders = new FolderList();
        for (var path : value.split(","))
        {
            folders.add(parseFolder(this, path));
        }
        return folders;
    }
}
