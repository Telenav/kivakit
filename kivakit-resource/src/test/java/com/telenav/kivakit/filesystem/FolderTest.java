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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Ignore;
import org.junit.Test;

public class FolderTest extends UnitTest
{
    @Test
    public void testAge()
    {
        final var file = Folder.kivakitTest(getClass()).file("age-test.txt");
        file.delete();
        file.print("test");
        ensure(file.created().elapsedSince().isLessThan(Duration.seconds(30)));
    }

    @Test
    public void testAsAbsolute()
    {
        final Folder foo = Folder.parse("~/foo");
        assert foo != null;
        ensureEqual(Folder.userHome().folder("foo"), foo.absolute());
    }

    @Test
    public void testClear()
    {
        final var folder = Folder.kivakitTest(getClass()).folder("clear-test");
        folder.mkdirs();
        folder.file("a.txt").print("A");
        folder.file("b.txt").print("B");
        ensureEqual(2, folder.files().size());
        folder.clearAll();
        ensure(folder.isEmpty());
    }

    @Test
    public void testDelete()
    {
        final var file = Folder.kivakitTest(getClass()).file("delete-test.txt");
        file.print("test");
        ensure(file.exists());
        file.delete();
        ensureFalse(file.exists());
    }

    // This test works but it's disabled to make the build run faster
    @Ignore
    public void testOldest()
    {
        final var folder = Folder.kivakitTest(getClass()).folder("clear-test");
        folder.mkdirs();
        folder.clearAll();
        folder.file("a.txt").print("A");
        Duration.untilNextSecond().sleep();
        folder.file("b.txt").print("B");
        ensureEqual(FileName.parse("a.txt"), folder.oldest().fileName());
    }

    @Test
    public void testTemporary()
    {
        final var folder = Folder.kivakitTest(getClass());
        ensure(folder.exists());
    }
}
