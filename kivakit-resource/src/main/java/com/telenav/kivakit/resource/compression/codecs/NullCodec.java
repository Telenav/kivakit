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

import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.lexakai.DiagramResourceCompression;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Codec that does no compression or decompression.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceCompression.class)
@LexakaiJavadoc(complete = true)
public class NullCodec implements Codec
{
    @Override
    public OutputStream compressed(OutputStream out)
    {
        return out;
    }

    @Override
    public InputStream decompressed(InputStream in)
    {
        return in;
    }
}
