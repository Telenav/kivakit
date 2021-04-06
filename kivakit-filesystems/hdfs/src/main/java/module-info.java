import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.hdfs.HdfsFileSystemService;

open module kivakit.filesystems.hdfs
{
    requires transitive java.rmi;

    requires transitive kivakit.service.client;
    requires transitive kivakit.filesystems.hdfs.proxy.spi;

    provides FileSystemService with HdfsFileSystemService;

    exports com.telenav.kivakit.filesystems.hdfs;
}
