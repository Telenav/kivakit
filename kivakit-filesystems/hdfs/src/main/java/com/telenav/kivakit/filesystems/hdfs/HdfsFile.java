////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.hdfs;

import com.telenav.kivakit.core.filesystem.spi.FileService;
import com.telenav.kivakit.core.kernel.interfaces.code.Code;
import com.telenav.kivakit.core.kernel.language.threading.Retry;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
import com.telenav.kivakit.filesystems.hdfs.project.lexakai.diagrams.DiagramHdfs;
import com.telenav.kivakit.filesystems.hdfs.proxy.spi.HdfsProxy;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.PosixFilePermission;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <b>Not public API</b>
 * <p>
 * Implementation of {@link FileService} used to provide {@link HdfsFileSystemService}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramHdfs.class)
@UmlRelation(label = "gets proxy from", referent = HdfsProxyClient.class)
@UmlNotPublicApi
@LexakaiJavadoc(complete = true)
public class HdfsFile extends BaseWritableResource implements FileService
{
    private final FilePath path;

    private Bytes size;

    @UmlAggregation
    private HdfsProxy proxy;

    public HdfsFile(final FilePath path)
    {
        this.path = path;
    }

    @Override
    public Bytes bytes()
    {
        if (size == null)
        {
            size = retry(() ->
            {
                final var length = proxy().length(pathAsString());
                return length < 0 ? null : Bytes.bytes(length);
            }).or(Bytes._0, "Unable to get size of $", this);
        }
        return size;
    }

    @Override
    public boolean chmod(final PosixFilePermission... permissions)
    {
        return unsupported();
    }

    @Override
    public boolean delete()
    {
        return retry(() -> proxy().deleteFile(pathAsString()))
                .or(false, "Unable to delete $", this);
    }

    @Override
    public boolean exists()
    {
        return retry(() -> proxy().exists(pathAsString()))
                .or(false, "Unable to determine if $ exists", this);
    }

    @Override
    public boolean isFolder()
    {
        return retry(() -> proxy().isFolder(pathAsString()))
                .or(false, "Unable to determine if $ is a folder", this);
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public boolean isWritable()
    {
        return retry(() -> proxy().isWritable(pathAsString())).or(false, "Unable to determine if $ is writable", this);
    }

    @Override
    public Time lastModified()
    {
        return retry(() -> Time.milliseconds(proxy().lastModified(pathAsString())))
                .or(null, "Unable to get last modified time of $", this);
    }

    @Override
    public boolean lastModified(final Time modified)
    {
        return retry(() -> proxy().lastModified(pathAsString(), modified.asMilliseconds()))
                .or(false, "Unable to set last modified time of $ to $", this, modified);
    }

    @Override
    public InputStream onOpenForReading()
    {
        return retry(() -> HdfsProxyIO.in(proxy().openForReading(pathAsString())))
                .or(null, "Unable to open $ for reading", this);
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return retry(() -> HdfsProxyIO.out(proxy().openForWriting(pathAsString())))
                .or(null, "Unable to open $ for writing", this);
    }

    @Override
    public HdfsFolder parent()
    {
        final var parent = path().parent();
        if (parent != null)
        {
            return new HdfsFolder(parent);
        }
        return null;
    }

    @Override
    public FilePath path()
    {
        return path;
    }

    @Override
    public boolean renameTo(final FileService that)
    {
        if (isOnSameFileSystem(that))
        {
            return renameTo((HdfsFile) that.resolveService());
        }
        fail("Cannot rename $ to $ across filesystems", this, that);
        return false;
    }

    public boolean renameTo(final HdfsFile to)
    {
        return retry(() -> proxy().rename(pathAsString(), to.path().toString()))
                .or(false, "Unable to rename $ to $", this, to);
    }

    @Override
    public HdfsFolder root()
    {
        return new HdfsFolder(path.root());
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    private String pathAsString()
    {
        return path().toString();
    }

    private HdfsProxy proxy()
    {
        if (proxy == null)
        {
            proxy = HdfsProxyClient.get().proxy();
        }
        return proxy;
    }

    private <T> Code<T> retry(final Code<T> code)
    {
        return Retry.retry(code, 16, Duration.seconds(15), () -> proxy = null);
    }
}
