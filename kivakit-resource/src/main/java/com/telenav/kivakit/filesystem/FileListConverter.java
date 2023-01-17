package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.Extension;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.filesystem.File.parseFile;

/**
 * Converts to and from {@link FileList}s
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FileListConverter extends BaseStringConverter<FileList>
{
    private final Extension extension;

    public FileListConverter(@NotNull Listener listener, @NotNull Extension extension)
    {
        super(listener, FileList.class);
        this.extension = extension;
    }

    @Override
    protected FileList onToValue(String value)
    {
        var files = new FileList();
        for (var path : value.split(","))
        {
            var file = parseFile(this, path.trim());
            if (file.isFolder())
            {
                files.addAll(file.asFolder().nestedFiles(extension::matches));
            }
            else
            {
                if (file.fileName().endsWith(extension))
                {
                    files.add(file);
                }
                else
                {
                    warning("$ is not a $ file", file, extension);
                }
            }
        }
        return files;
    }
}
