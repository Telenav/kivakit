////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.*;

public class FolderTest extends UnitTest
{
    @Test
    public void testAge()
    {
        final var file = Folder.unitTestOutput(getClass()).file("age-test.txt");
        file.print("test");
        ensure(file.age().isLessThan(Duration.seconds(30)));
    }

    @Test
    public void testAsAbsolute()
    {
        ensureEqual(Folder.userHome().folder("foo"), Folder.parse("~/foo").absolute());
    }

    @Test
    public void testClear()
    {
        final var folder = Folder.unitTestOutput(getClass()).folder("clear-test");
        folder.mkdirs();
        folder.file("a.txt").print("A");
        folder.file("b.txt").print("B");
        ensureEqual(2, folder.files().size());
        folder.clear();
        ensure(folder.isEmpty());
    }

    @Test
    public void testDelete()
    {
        final var file = Folder.unitTestOutput(getClass()).file("delete-test.txt");
        file.print("test");
        ensure(file.exists());
        file.delete();
        ensureFalse(file.exists());
    }

    // This test works but it's disabled to make the build run faster
    @Ignore
    public void testOldest()
    {
        final var folder = Folder.unitTestOutput(getClass()).folder("clear-test");
        folder.mkdirs();
        folder.clear();
        folder.file("a.txt").print("A");
        Duration.untilNextSecond().sleep();
        folder.file("b.txt").print("B");
        ensureEqual(new FileName("a.txt"), folder.oldest().fileName());
    }

    @Test
    public void testTemporary()
    {
        final var folder = Folder.unitTestOutput(getClass());
        ensure(folder.exists());
    }
}
