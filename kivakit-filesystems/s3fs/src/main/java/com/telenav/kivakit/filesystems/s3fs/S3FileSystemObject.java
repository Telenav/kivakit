////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.configuration.settings.Settings;
import com.telenav.kivakit.core.filesystem.spi.FileSystemObjectService;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import com.telenav.kivakit.core.kernel.language.patterns.group.Group;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.resource.writing.BaseWritableResource;
import com.telenav.kivakit.filesystems.s3fs.project.lexakai.diagrams.DiagramS3;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class represents an AWS S3 (Simple Storage Service) object
 *
 * @author songg
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramS3.class)
public abstract class S3FileSystemObject extends BaseWritableResource implements FileSystemObjectService
{
    protected static final Logger LOGGER = LoggerFactory.newLogger();

    // A bucket name in the path
    private static final Pattern BUCKET_NAME = Pattern.expression("[\\w-\\d\\.]").oneOrMore();

    // A key name in the path
    private static final Pattern KEY_NAME = Pattern.ANYTHING;

    private static final Group<String> bucketGroup = BUCKET_NAME.group(Listener.none());

    private static final Group<String> keyGroup = KEY_NAME.group(Listener.none());

    private static Pattern schemePattern;

    private static final Group<String> schemeGroup = schemePattern().group(Listener.none());

    // s3://${bucket}/${key}
    private static final Pattern pattern = schemeGroup
            .then(Pattern.constant("://"))
            .then(bucketGroup)
            .then(Pattern.SLASH.optional())
            .then(keyGroup.optional());

    public static boolean accepts(final String path)
    {
        return schemePattern().then(Pattern.ANYTHING)
                .matches(path);
    }

    protected static ListObjectsRequest listRequest(final String bucketName, final String keyName)
    {
        return ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(keyName + "/")
                .delimiter("/")
                .build();
    }

    protected static FilePath path(final String scheme, final String bucketName, final String keyName)
    {
        return FilePath.parseFilePath(scheme + bucketName + "/" + keyName);
    }

    // The scheme of path, such as "s3://"
    private final String scheme;

    // Name of s3 bucket
    private final String bucket;

    // Name of s3 object key
    private final String key;

    // Meta data attached to the object
    private Map<String, String> metadata;

    // S3 client
    private S3Client client;

    // True if it's a folder
    private final boolean isFolder;

    S3FileSystemObject(final FilePath path, final boolean isFolder)
    {
        super(normalize(path));

        this.isFolder = isFolder;

        final var matcher = pattern.matcher(path().toString());
        if (matcher.matches())
        {
            scheme = schemeGroup.get(matcher);
            bucket = bucketGroup.get(matcher);
            key = isFolder ? keyGroup.get(matcher, "") + "/" : keyGroup.get(matcher, "");
        }
        else
        {
            throw new IllegalArgumentException("Bad S3 path: " + path);
        }
    }

    @Override
    public void copyFrom(final Resource resource, final CopyMode mode, final ProgressReporter reporter)
    {
        final var in = resource.openForReading();

        final var request = PutObjectRequest.builder()
                .bucket(bucket())
                .key(key())
                .build();

        client().putObject(request, RequestBody.fromInputStream(in, resource.bytes().asBytes()));
    }

    @Override
    public boolean delete()
    {
        final var request = DeleteObjectRequest.builder()
                .bucket(bucket())
                .key(key())
                .build();

        client().deleteObject(request);
        return !exists();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (o instanceof S3FileSystemObject)
        {
            final var that = (S3FileSystemObject) o;
            return inSameBucket(that) && withIdenticalKey(that);
        }
        return false;
    }

    @Override
    public boolean exists()
    {
        return metadata() != null;
    }

    @Override
    public boolean isFolder()
    {
        return isFolder;
    }

    @Override
    public boolean isRemote()
    {
        return true;
    }

    @Override
    public abstract boolean isWritable();

    public String name()
    {
        return path().fileName().name();
    }

    @Override
    public InputStream onOpenForReading()
    {
        final var request = GetObjectRequest.builder()
                .bucket(bucket())
                .key(key())
                .build();

        return client().getObject(request);
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return new S3Output(this);
    }

    @Override
    public S3Folder parent()
    {
        final var parent = path().parent();
        if (parent != null)
        {
            return new S3Folder(parent);
        }
        return null;
    }

    @Override
    public FilePath path()
    {
        return FilePath.parseFilePath(super.path().toString());
    }

    @Override
    public FolderService rootFolderService()
    {
        return new S3Folder(path(scheme(), bucket(), ""));
    }

    @Override
    public String toString()
    {
        return path().toString();
    }

    private static FilePath normalize(final FilePath path)
    {
        return path;
    }

    // The S3 path schema
    private static Pattern schemePattern()
    {
        if (schemePattern == null)
        {
            schemePattern = Pattern.expression("s3")
                    .then(Pattern.expression("[na]").optional());
        }
        return schemePattern;
    }

    final String bucket()
    {
        return bucket;
    }

    boolean canRenameTo(final S3FileSystemObject to)
    {
        var canRename = true;
        if (to != null && to.exists())
        {
            LOGGER.warning("Can't rename " + this + " to " + to);
            canRename = false;
        }
        else if (equals(to))
        {
            LOGGER.warning("Can't rename to the same Aws S3 object as " + to);
            canRename = false;
        }
        else if (to != null && !inSameBucket(to))
        {
            LOGGER.warning("Can't move S3 object from ${debug} to another bucket ${debug} ", bucket(), to.bucket());
            canRename = false;
        }
        return canRename;
    }

    S3Client client()
    {
        if (client == null)
        {
            final var credentials = Settings.require(S3Settings.class).credentials();

            client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .region(Region.US_WEST_2)
                    .build();
        }
        return client;
    }

    void copyTo(final S3FileSystemObject that)
    {
        String sourceUrl = null;
        try
        {
            sourceUrl = URLEncoder.encode(bucket() + "/" + key(), StandardCharsets.UTF_8.toString());
        }
        catch (final UnsupportedEncodingException e)
        {
            LOGGER.warning(e, "URL could not be encoded");
        }

        final var request = CopyObjectRequest.builder().copySource(sourceUrl)
                .destinationBucket(that.bucket())
                .destinationKey(key())
                .build();

        client().copyObject(request);
    }

    boolean inSameBucket(final S3FileSystemObject that)
    {
        return bucket().equals(that.bucket());
    }

    final String key()
    {
        return key;
    }

    Bytes length()
    {
        return Bytes.bytes(object().contentLength());
    }

    Map<String, String> metadata()
    {
        if (metadata == null)
        {
            final var request = GetObjectRequest.builder()
                    .bucket(bucket())
                    .key(key())
                    .build();

            final var response = client()
                    .getObject(request)
                    .response();

            metadata = response.metadata();
        }
        return metadata;
    }

    GetObjectResponse object()
    {
        final var request = GetObjectRequest.builder().bucket(bucket()).key(key()).build();
        return client().getObject(request).response();
    }

    String scheme()
    {
        return scheme;
    }

    boolean withIdenticalKey(final S3FileSystemObject that)
    {
        return key().equals(that.key());
    }
}
