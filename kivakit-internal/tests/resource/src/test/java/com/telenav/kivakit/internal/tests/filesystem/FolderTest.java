////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.internal.tests.filesystem;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.testing.UnitTest;
import org.junit.Ignore;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.seconds;
import static com.telenav.kivakit.core.time.Duration.untilNextSecond;
import static com.telenav.kivakit.filesystem.Folder.parseFolder;
import static com.telenav.kivakit.filesystem.Folders.kivakitTestFolder;
import static com.telenav.kivakit.filesystem.Folders.userHome;

public class FolderTest extends UnitTest
{
    @Test
    public void testAge()
    {
        var file = kivakitTestFolder(getClass()).file("age-test.txt");
        file.delete();
        file.saveText("test");
        ensure(file.createdAt().elapsedSince().isLessThan(seconds(30)));
    }

    @Test
    public void testAsAbsolute()
    {
        Folder foo = parseFolder(this, "~/foo");
        assert foo != null;
        ensureEqual(userHome().folder("foo").withTrailingSlash(), foo.absolute());
    }

    @Test
    public void testClear()
    {
        var folder = kivakitTestFolder(getClass()).folder("clear-test");
        folder.mkdirs();
        folder.file("a.txt").saveText("A");
        folder.file("b.txt").saveText("B");
        ensureEqual(2, folder.files().size());
        folder.clearAll();
        ensure(folder.isEmpty());
    }

    @Test
    public void testDelete()
    {
        var file = kivakitTestFolder(getClass()).file("delete-test.txt");
        file.saveText("test");
        ensure(file.exists());
        file.delete();
        ensureFalse(file.exists());
    }

    // This test works but it's disabled to make the build run faster
    @SuppressWarnings("unused")
    @Ignore
    public void testOldest()
    {
        var folder = kivakitTestFolder(getClass()).folder("clear-test");
        folder.mkdirs();
        folder.clearAll();
        folder.file("a.txt").saveText("A");
        untilNextSecond().sleep();
        folder.file("b.txt").saveText("B");
        ensureEqual(FileName.parseFileName(this, "a.txt"), folder.oldest().fileName());
    }

    @Test
    public void testTemporary()
    {
        var folder = kivakitTestFolder(getClass());
        ensure(folder.exists());
    }
}
