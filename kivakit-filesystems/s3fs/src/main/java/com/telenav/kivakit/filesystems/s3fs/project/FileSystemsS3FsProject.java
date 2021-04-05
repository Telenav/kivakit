package com.telenav.kivakit.filesystems.s3fs.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class FileSystemsS3FsProject extends Project
{
    private static final Lazy<FileSystemsS3FsProject> singleton = Lazy.of(FileSystemsS3FsProject::new);

    public static FileSystemsS3FsProject get()
    {
        return singleton.get();
    }

    protected FileSystemsS3FsProject()
    {
    }
}
