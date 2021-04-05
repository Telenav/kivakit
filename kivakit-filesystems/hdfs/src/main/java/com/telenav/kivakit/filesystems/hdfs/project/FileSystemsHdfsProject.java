package com.telenav.kivakit.filesystems.hdfs.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class FileSystemsHdfsProject extends Project
{
    private static final Lazy<FileSystemsHdfsProject> singleton = Lazy.of(FileSystemsHdfsProject::new);

    public static FileSystemsHdfsProject get()
    {
        return singleton.get();
    }

    protected FileSystemsHdfsProject()
    {
    }
}
