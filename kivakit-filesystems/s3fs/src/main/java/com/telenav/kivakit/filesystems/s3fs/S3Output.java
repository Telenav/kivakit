////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.File;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.Folder.Type;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.io.IOException;
import java.io.OutputStream;

import static com.telenav.kivakit.core.resource.CopyMode.OVERWRITE;

/**
 * OutputStream that appends to an existing S3 object
 *
 * @author songg
 */
@LexakaiJavadoc(complete = true)
public class S3Output extends OutputStream
{
    // The temporary cache folder for storing materialized files
    private static final Folder cacheFolder = Folder.temporaryForProcess(Type.CLEAN_UP_ON_EXIT).ensureExists();

    /** The {@link S3FileSystemObject} this stream writes to */
    private final S3FileSystemObject object;

    // The underline output stream
    private final OutputStream outputStream;

    // The cache file
    private final File cacheFile;

    /**
     * Create an {@link OutputStream} writing to an {@link S3FileSystemObject}
     *
     * @param object The {@link S3FileSystemObject} to which this stream is writing
     */
    protected S3Output(final S3FileSystemObject object)
    {
        this.object = object;
        cacheFile = cacheFile(object.path());
        outputStream = cacheFile.openForWriting();
    }

    /** Close this stream and release the lease */
    @Override
    public void close() throws IOException
    {
        outputStream.close();
        if (object.exists())
        {
            object.delete();
        }
        object.copyFrom(cacheFile, OVERWRITE, ProgressReporter.NULL);
    }

    @Override
    public void flush()
    {
        try
        {
            outputStream.flush();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void write(final byte[] bytes, final int offset, final int length) throws IOException
    {
        outputStream.write(bytes, offset, length);
    }

    @Override
    public void write(final int b) throws IOException
    {
        outputStream.write(b);
    }

    private File cacheFile(final FilePath filePath)
    {
        // Flatten path being cached into a long filename by turning all file
        // system meta characters into underscores.
        // For example, "a/b/c.txt" becomes "a_b_c.txt"
        return File.parse(cacheFolder + "/" + filePath.toString().replaceAll("[/:]", "_"));
    }
}
