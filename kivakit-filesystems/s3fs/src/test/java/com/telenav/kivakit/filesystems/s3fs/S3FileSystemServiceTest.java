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

package com.telenav.kivakit.filesystems.s3fs;

import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.path.FileName;
import org.junit.Ignore;
import org.junit.Test;

// TODO shibo - S3 test doesn't work for now because of missing credentials
@Ignore
public class S3FileSystemServiceTest
{
    @Test
    public void testFile()
    {
        try
        {
            final var folder = new S3Folder("s3://com-telenav-nav-user-analytics-dev/test");
            folder.delete();

            folder.folder(FileName.parse("2nd")).mkdirs();

            final var file2 = new S3File("s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/s3-test.gz");
            final var printWriter = file2.printWriter();
            printWriter.println("the 3rd test case for s3 outputstream");
            printWriter.close();

            // File file = new File(
            // "s3://com-telenav-nav-user-analytics-dev/nav-user-analytics/map-matching/fcdtelenav.bay-sf/bay-sf.2017.01.24.pbf_2017-01-25.230133/part-r-00000.gz");

            for (final String line : file2.reader().lines(ProgressReporter.NULL))
            {
                System.out.println(line);
                break;
            }
        }
        catch (final Exception ex)
        {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }
}
