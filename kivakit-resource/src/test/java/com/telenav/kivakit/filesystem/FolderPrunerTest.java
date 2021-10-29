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

import com.telenav.kivakit.kernel.language.threading.latches.CompletionLatch;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class FolderPrunerTest extends UnitTest
{
    @Test
    public void testCapacity()
    {
        final var folder = folder("folder-capacity-test");

        // Save file abc
        final var file1 = folder.file("abc");
        file1.writer().save("abc");
        ensure(file1.exists());

        // Start folder pruner
        final var removed = new CompletionLatch();
        final FolderPruner pruner = new FolderPruner(folder, Duration.milliseconds(1).asFrequency())
        {
            @Override
            protected void onFileRemoved( File file)
            {
                removed.completed();
            }
        };
        pruner.capacity(Bytes.bytes(4));
        pruner.minimumAge(Duration.NONE);
        pruner.minimumUsableDiskSpace(Percent._0);
        pruner.start();

        ensure(file1.exists());

        // Save file def
        final var file2 = folder.file("def");
        file2.writer().save("def");

        // Wait for a file to get removed
        removed.waitForCompletion();

        // Because of file system time granularity either file could get removed, but not both
        ensure(file1.exists() || file2.exists());
        ensureFalse(!file1.exists() && !file2.exists());
        pruner.stop(Duration.ONE_SECOND);
    }

    @Test
    public void testDiskSpace()
    {
        if (!isQuickTest())
        {
            final var folder = folder("disk-space-test");
            final var file = folder.file("temp1");
            file.writer().save("test");
            final FolderPruner pruner = new FolderPruner(folder, Duration.milliseconds(25).asFrequency())
            {
                @Override
                protected void onFileRemoved( File file)
                {
                }
            };
            pruner.minimumUsableDiskSpace(Percent.of(100));
            pruner.minimumAge(Duration.NONE);
            pruner.start();
            Duration.seconds(0.25).sleep();
            ensureFalse(file.exists());
            pruner.stop(Duration.ONE_SECOND);
        }
    }

    private Folder folder( String name)
    {
        final var folder = Folder.kivakitTest(getClass()).folder(name);
        folder.mkdirs();
        folder.clearAll();
        return folder;
    }
}
