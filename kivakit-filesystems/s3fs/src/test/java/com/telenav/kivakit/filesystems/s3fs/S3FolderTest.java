////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.filesystem.spi.FolderService;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

// This test can be run in Eclipse where the module path is set, but there doesn't seem to be a way
// to run it from maven on the command line
@Ignore
public class S3FolderTest extends UnitTest
{
    @SuppressWarnings("EmptyMethod")
    @BeforeClass
    public static void testInitialize()
    {
    }

    @Test
    public void testBucketName()
    {
        final var s3Folder = new S3Folder("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder");
        ensure(s3Folder.bucket().equals("com-telenav-nav-user-analytics-dev"));
    }

    @Test
    public void testClear()
    {
        final FolderService folder = new S3Folder("s3://com-telenav-nav-user-analytics-dev/folder");
        final var child = folder.folder(new FileName("child"));
        child.mkdirs();
        ensure(child.exists());
        ensureFalse(folder.isEmpty());
        folder.clear();
        ensure(!child.exists());
        ensureFalse(!folder.isEmpty());
    }

    @Test
    public void testDelete()
    {
        final FolderService folder = new S3Folder("s3://com-telenav-nav-user-analytics-dev/folder");
        folder.mkdirs();
        ensure(folder.exists());
        folder.clear();
        folder.delete();
        ensure(!folder.exists());
    }

    @Test
    public void testFile()
    {
        final var folder = Folder.parse("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder");
        final var file = folder.file(new FileName("name1"));
        ensure(file.fileName().toString().equals("name1"));
    }

    @Test
    public void testFolder()
    {
        final var path = "s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder";
        final var folder = Folder.parse(path);
        ensure(folder.folder("child").name().equals(new FileName("child")));
    }

    @Test
    public void testGetName()
    {
        final var path = "s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder";
        final var folder = Folder.parse(path);
        ensure(folder.name().equals(new FileName("folder")));
    }

    @Test
    public void testKeyName()
    {
        final var s3Folder = new S3Folder("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder");
        ensure(s3Folder.key().equals("nav-user-analytics/folder/"));
    }

    @Test
    public void testParent()
    {
        final var path = "s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder";
        final var folder = Folder.parse(path);
        ensure(folder.parent().equals(Folder.parse("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics")));
        ensure(folder.parent().name().equals(new FileName("nav-user-analytics")));
    }

    @Test
    public void testPath()
    {
        final var path = "s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder";
        final var folder = Folder.parse(path);
        ensure(folder.path().equals(FilePath.parseFilePath(path)));
    }

    @Test
    public void testPathString()
    {
        final var path = "s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder";
        final var folder = Folder.parse(path);
        ensure(folder.path().toString().equals(path));
    }

    @Test
    public void testRename()
    {
        final var folder = Folder.parse("s3://com-telenav-nav-user-analytics-dev/old-" + FileName.dateTime());
        folder.mkdirs();
        final var file = folder.file(new FileName("tmp.txt"));
        final var printWriter = file.printWriter();
        printWriter.println("for test");
        printWriter.close();

        final var that = folder.parent().folder(FileName.dateTime());
        folder.renameTo(that);
        ensure(that.exists());
        ensure(!folder.exists());
        that.clearAll();
        ensure(that.isEmpty());
        that.delete();
        ensure(!that.exists());
        folder.delete();
        ensure(!folder.exists());
    }

    @Test
    public void testScheme()
    {
        final var s3Folder = new S3Folder("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/folder");
        ensure(s3Folder.scheme().equals("s3://"));
    }
}
