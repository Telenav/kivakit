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

package com.telenav.kivakit.resource.resources;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A {@link WritableResource} that writes to a byte array, and can be converted to a string with {@link #string()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            documentation = DOCUMENTATION_COMPLETE,
            testing = TESTING_NONE)
public class StringOutputResource extends BaseWritableResource
{
    /** The output stream */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isWritable()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream onOpenForWriting()
    {
        return out;
    }

    public String string()
    {
        return out.toString();
    }
}
