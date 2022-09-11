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

package com.telenav.kivakit.core.io;

import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.internal.lexakai.DiagramIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;

@UmlClassDiagram(diagram = DiagramIo.class)
public class LimitedInputStream extends InputStream
{
    private InputStream in;

    private final Bytes limit;

    private long read;

    public LimitedInputStream(InputStream in, Bytes limit)
    {
        this.in = in;
        this.limit = limit;
    }

    @Override
    public void close()
    {
        IO.close(in);
        in = null;
    }

    @Override
    public int read() throws IOException
    {
        if (read++ > limit.asBytes())
        {
            return -1;
        }
        return in.read();
    }
}
