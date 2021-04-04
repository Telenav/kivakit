////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.resource.path.FilePath;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class FileSystemTest extends UnitTest
{
    @Test
    public void testFilePath()
    {
        ensureEqual(FilePath.parseFilePath("TestFile1.txt").absolute(), FilePath.parseFilePath("TestFile1.txt").absolute());

        final var filePath1 = FilePath.parseFilePath("TestFile1.txt");
        final var filePath1a = FilePath.parseFilePath("TestFile1.txt");
        final var filePath2 = FilePath.parseFilePath("TestFile2.txt");
        final var directoryName = "newdirectory";
        final var fileName = "TestFile3.txt";

        ensureEqual(filePath1, filePath1a);
        ensure(filePath1.equals(filePath1a));
        ensureFalse(filePath1.equals(filePath2));

        final var filePath3 = FilePath.parseFilePath(directoryName).withChild(fileName);
        ensureEqual(filePath3.toString(), directoryName + filePath1.separator() + fileName);

        ensure(FilePath.parseFilePath(fileName).absolute().toString().endsWith(FilePath.parseFilePath(fileName).separator() + fileName));
    }
}
