package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.kernel.language.reflection.populator.KivaKitPropertyConverter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

/**
 * @author jonathanl (shibo)
 */
public class S3Settings
{
    private String accessKey;

    private String secretAccessKey;

    @KivaKitPropertyConverter
    public void accessKey(final String accessKey)
    {
        this.accessKey = accessKey;
    }

    public AwsBasicCredentials credentials()
    {
        return AwsBasicCredentials.create(accessKey, secretAccessKey);
    }

    @KivaKitPropertyConverter
    public void secretAccessKey(final String secretAccessKey)
    {
        this.secretAccessKey = secretAccessKey;
    }
}
