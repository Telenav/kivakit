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

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.spi.DiskService;
import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams.DiagramS3;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of the {@link FileSystemService} SPI to provide S3 access through the KIVAKIT resource API. This
 * service accepts {@link FilePath}s that begin with "s3:" and it provides implementations of {@link FileService} and
 * {@link FolderService} which are used by {@link File} and {@link Folder} to provide transparent access to file system
 * services, including HDFS.
 *
 * @author songg
 * @author jonathanl (shibo)
 * @see S3File
 * @see S3Folder
 * @see Resource
 * @see File
 * @see Folder
 */
@UmlClassDiagram(diagram = DiagramS3.class)
@LexakaiJavadoc(complete = true)
public class S3FileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(final FilePath path)
    {
        return S3FileSystemObject.accepts(path.toString());
    }

    @Override
    public DiskService diskService(final FilePath path)
    {
        return unsupported();
    }

    @Override
    @UmlRelation(label = "provides")
    public S3File fileService(final FilePath path)
    {
        return new S3File(path);
    }

    @Override
    @UmlRelation(label = "provides")
    public S3Folder folderService(final FilePath path)
    {
        return new S3Folder(path);
    }
}
