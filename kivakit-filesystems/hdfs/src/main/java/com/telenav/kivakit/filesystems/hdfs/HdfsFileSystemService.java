////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
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
