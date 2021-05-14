////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.test.UnitTest;
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
