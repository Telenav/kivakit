////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.test.UnitTest;
import com.telenav.kivakit.core.test.annotations.SlowTests;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category({ SlowTests.class })
public class S3FileTest extends UnitTest
{
    final S3File file = new S3File("s3://telenav-tdk/" + apidocs());

    @Test
    public void testBucketName()
    {
        ensure(file.fileName().equals(new FileName("index.html")));
    }

    @Test
    public void testGetName()
    {
        ensure("index.html".equals(file.name()));
    }

    @Test
    public void testInSameBucket()
    {
        ensure(file.inSameBucket(new S3File("s3://telenav-tdk/" + KivaKit.get().version() + "/another")));
    }

    @Test
    public void testIsFolder()
    {
        ensure(file.isFile());
        ensureFalse(file.isFolder());
    }

    @Test
    public void testKeyName()
    {
        ensure(apidocs().equals(file.key()));
    }

    @Test
    public void testParent()
    {
        final FolderService folder = file.parent();
        ensure("apidocs".equals(folder.baseName().toString()));
        ensure(("s3://telenav-tdk/" + KivaKit.get().version() + "/apidocs").equals(folder.path().toString()));
    }

    @Test
    public void testPath()
    {
        final var s3File = new S3File("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/s3-test.gz");
        ensure(s3File.path().equals(FilePath.parseFilePath("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/s3-test.gz")));
    }

    @Test
    public void testReadFile()
    {
        // Message.println("$", file.string());
    }

    @Test
    public void testWithIdenticalKey()
    {
        ensure(file.withIdenticalKey(new S3File("s3://anotherBucket/" + apidocs())));
    }

    @NotNull
    private String apidocs()
    {
        return KivaKit.get().version() + "/apidocs/index.html";
    }
}
