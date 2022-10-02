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

import com.telenav.kivakit.core.thread.latches.CompletionLatch;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.FolderPruner;
import com.telenav.kivakit.testing.UnitTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import static com.telenav.kivakit.core.time.Duration.ZERO_DURATION;

@Ignore
public class FolderPrunerTest extends UnitTest
{
    @Test
    public void testCapacity()
    {
        var folder = folder("folder-capacity-test");

        // Save file abc
        var file1 = folder.file("abc");
        file1.writer().saveText("abc");
        ensure(file1.exists());

        // Start folder pruner
        var removed = new CompletionLatch();
        FolderPruner pruner = new FolderPruner(folder, Duration.milliseconds(1).asFrequency())
        {
            @Override
            protected void onFileRemoved(@NotNull File file)
            {
                removed.threadCompleted();
            }
        };
        pruner.capacity(Bytes.bytes(4));
        pruner.maximumAge(ZERO_DURATION);
        pruner.minimumUsableDiskSpace(Percent._0);
        pruner.start();

        ensure(file1.exists());

        // Save file def
        var file2 = folder.file("def");
        file2.writer().saveText("def");

        // Wait for a file to get removed
        removed.waitForAllThreadsToComplete();

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
            var folder = folder("disk-space-test");
            var file = folder.file("temp1");
            file.writer().saveText("test");
            FolderPruner pruner = new FolderPruner(folder, Duration.milliseconds(25).asFrequency())
            {
                @Override
                protected void onFileRemoved(@NotNull File file)
                {
                }
            };
            pruner.minimumUsableDiskSpace(Percent.percent(100));
            pruner.maximumAge(ZERO_DURATION);
            pruner.start();
            Duration.seconds(0.1).sleep();
            ensureFalse(file.exists());
            pruner.stop(Duration.ONE_SECOND);
        }
    }

    private Folder folder(String name)
    {
        var folder = Folder.kivakitTest(getClass()).folder(name);
        folder.mkdirs();
        folder.clearAll();
        return folder;
    }
}
