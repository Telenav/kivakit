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

import com.telenav.kivakit.core.value.count.ByteSized;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.project.lexakai.DiagramIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.OutputStream;

@UmlClassDiagram(diagram = DiagramIo.class)
public class ByteSizedOutputStream extends OutputStream implements ByteSized
{
    private final OutputStream out;

    private long size;

    public ByteSizedOutputStream(OutputStream out)
    {
        this.out = out;
    }

    @Override
    public void close() throws IOException
    {
        out.close();
    }

    @Override
    public void flush() throws IOException
    {
        out.flush();
    }

    @Override
    public Bytes sizeInBytes()
    {
        return Bytes.bytes(size);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        out.write(b);
        size += b.length;
    }

    @Override
    public void write(byte[] b, int off, int length) throws IOException
    {
        out.write(b, off, length);
        size += length;
    }

    @Override
    public void write(int b) throws IOException
    {
        out.write(b);
        size++;
    }
}
