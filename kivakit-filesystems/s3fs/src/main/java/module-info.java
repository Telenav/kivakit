import com.telenav.kivakit.core.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystems.s3fs.S3FileSystemService;

open module kivakit.filesystems.s3fs
{
    requires transitive kivakit.core.configuration;

    requires transitive software.amazon.awssdk.services.s3;
    requires transitive software.amazon.awssdk.core;
    requires transitive software.amazon.awssdk.regions;
    requires transitive software.amazon.awssdk.auth;

    provides FileSystemService with S3FileSystemService;

    exports com.telenav.kivakit.filesystems.s3fs;
}
