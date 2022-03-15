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

import com.telenav.kivakit.resource.ReadableResource;
import com.telenav.kivakit.resource.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@link ReadableResource} formed from a string passed to the constructor {@link #StringOutputResource()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class StringOutputResource extends BaseWritableResource
{
    private final ByteArrayOutputStream array = new ByteArrayOutputStream();

    @Override
    public Boolean isWritable()
    {
        return true;
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new ByteArrayInputStream(array.toByteArray());
    }

    @Override
    public OutputStream onOpenForWriting()
    {
        return array;
    }

    public String string()
    {
        return array.toString();
    }
}
