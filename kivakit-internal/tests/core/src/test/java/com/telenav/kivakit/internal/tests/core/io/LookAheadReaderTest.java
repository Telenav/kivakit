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

package com.telenav.kivakit.internal.tests.core.io;
import com.telenav.kivakit.core.io.LookAheadReader;
import com.telenav.kivakit.internal.testing.CoreUnitTest;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class LookAheadReaderTest extends CoreUnitTest
{
    @Test
    public void test() throws IOException
    {
        var reader = new LookAheadReader(new StringReader("xyz<XML"));
        while (reader.current() != '<')
        {
            reader.next();
        }
        ensureEqual('<', (char) reader.current());
        ensureEqual('X', (char) reader.lookAhead());
        ensureEqual('<', (char) reader.read());
        ensureEqual('X', (char) reader.read());
        ensureEqual('M', (char) reader.read());
        ensureEqual('L', (char) reader.read());
        ensureFalse(reader.hasNext());
        reader.close();
    }
}
