////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.spi.DiskService;
import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams.DiagramS3;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

@UmlClassDiagram(diagram = DiagramS3.class)
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
