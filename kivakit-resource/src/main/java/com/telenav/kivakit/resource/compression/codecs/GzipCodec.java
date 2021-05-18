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

package com.telenav.kivakit.resource.compression.codecs;

import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceCompression;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Codec for gzip compression.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceCompression.class)
@LexakaiJavadoc(complete = true)
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
