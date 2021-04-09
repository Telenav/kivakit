////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.spi.DiskService;
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of the {@link FileSystemService} SPI to provide HDFS access through the KivaKit resource API. This
 * service accepts {@link FilePath}s that begin with "hdfs:" and it provides implementations of {@link FileService} and
 * {@link FolderService} which are used by {@link File} and {@link Folder} to provide transparent access to file system
 * services, including HDFS.
 *
 * @author jonathanl (shibo)
 * @see HdfsFile
 * @see HdfsFolder
 * @see Resource
 * @see File
 * @see Folder
 */
@UmlClassDiagram(diagram = DiagramHdfs.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class HdfsFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(final FilePath path)
    {
        assert path != null;
        return path.startsWith("hdfs:");
    }

    @Override
    @UmlRelation(label = "not supported")
    public DiskService diskService(final FilePath path)
    {
        return unsupported();
    }

    @Override
    @UmlRelation(label = "provides")
    public HdfsFile fileService(final FilePath path)
    {
        return new HdfsFile(path);
    }

    @Override
    @UmlRelation(label = "provides")
    public HdfsFolder folderService(final FilePath path)
    {
        return new HdfsFolder(path);
    }
}
