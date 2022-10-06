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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceCompression;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Codec for gzip compression.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceCompression.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class GzipCodec implements Codec
{
    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream compressed(@NotNull OutputStream out)
    {
        try
        {
            return new GZIPOutputStream(out);
        }
        catch (IOException e)
        {
            throw new Problem(e, "Cannot open GZIP output stream").asException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream decompressed(@NotNull InputStream in)
    {
        try
        {
            return new GZIPInputStream(in);
        }
        catch (IOException e)
        {
            throw new Problem(e, "Cannot open GZIP input stream").asException();
        }
    }
}
