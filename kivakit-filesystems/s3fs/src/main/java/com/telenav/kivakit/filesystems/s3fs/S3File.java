////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams.DiagramS3;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FileService} used to provide {@link S3FileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramS3.class)
@LexakaiJavadoc(complete = true)
public class S3File extends S3FileSystemObject implements FileService
{
    public S3File(final FilePath path)
    {
        super(path, false);
    }

    public S3File(final String path)
    {
        super(FilePath.parseFilePath(path), false);
    }

    @Override
    public Bytes bytes()
    {
        return length();
    }

    @Override
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public boolean isWritable()
    {
        return true;
    }

    public boolean renameTo(final S3File that)
    {
        if (canRenameTo(that))
        {
            copyTo(that);
            delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean renameTo(final FileService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((S3File) that.resolveService());
        }
        fail("Cannot rename $ to $ across filesystems", this, that);
        return false;
    }

    public void write(final String line)
    {
        if (exists())
        {
            delete();
        }
        final var printWriter = printWriter();
        printWriter.println(line);
        printWriter.close();
    }
}
