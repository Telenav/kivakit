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

package com.telenav.kivakit.core.resource.compression.codecs;

import com.telenav.kivakit.core.resource.compression.Codec;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceCompression;

import java.io.*;
import java.util.zip.*;

@UmlClassDiagram(diagram = DiagramResourceCompression.class)
public class GzipCodec implements Codec
{
    @Override
    public OutputStream compressed(final OutputStream out)
    {
        try
        {
            return new GZIPOutputStream(out);
        }
        catch (final IOException e)
        {
            throw new Problem(e, "Cannot open GZIP output stream").asException();
        }
    }

    @Override
    public InputStream decompressed(final InputStream in)
    {
        try
        {
            return new GZIPInputStream(in);
        }
        catch (final IOException e)
        {
            throw new Problem(e, "Cannot open GZIP input stream").asException();
        }
    }
}
