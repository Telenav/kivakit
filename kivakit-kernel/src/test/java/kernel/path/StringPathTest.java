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

package kernel.path;

import com.telenav.kivakit.kernel.language.paths.StringPath;
import org.junit.Test;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureEqual;

@SuppressWarnings("SameParameterValue")
public class StringPathTest
{
    @Test
    public void testParse()
    {
        // Relative paths
        ensureEqual("", slashPath("").toString());
        ensureEqual("a", slashPath("a").toString());
        ensureEqual("a/b", slashPath("a/b").toString());
        ensureEqual("a/b", dotPath("a.b").toString());
        ensureEqual("a.b", dotPath("a.b").withSeparator(".").toString());
        ensureEqual("a.b", slashPath("a/b").withSeparator(".").toString());

        // Absolute paths
        ensureEqual("/a/b", absoluteSlashPath("/a/b").toString());
        ensureEqual(":a:b", StringPath.parseStringPath(":a:b", ":", ":").withSeparator(":").toString());
    }

    private StringPath absoluteSlashPath(String path)
    {
        return StringPath.parseStringPath(path, "/", "/");
    }

    private StringPath dotPath(String path)
    {
        return StringPath.parseStringPath(path, "\\.");
    }

    private StringPath slashPath(String path)
    {
        return StringPath.parseStringPath(path, "/");
    }
}
