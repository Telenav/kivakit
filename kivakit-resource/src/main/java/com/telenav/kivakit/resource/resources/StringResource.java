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

import com.telenav.kivakit.core.io.StringInputStream;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.ReadableResource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.project.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.util.function.Function;

/**
 * A {@link ReadableResource} formed from a string passed to the constructor {@link #StringResource(String)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class StringResource extends BaseReadableResource
{
    private final String value;

    public StringResource(String value)
    {
        this(ResourcePath.parseUnixResourcePath(Listener.console(), "/objects/String@" + Integer.toHexString(value.hashCode())), value);
    }

    public StringResource(ResourcePath path, String value)
    {
        super(path);
        this.value = value;
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new StringInputStream(value);
    }

    @Override
    public Bytes sizeInBytes()
    {
        return Bytes.bytes(value.length());
    }

    public StringResource transform(Function<String, String> transformation)
    {
        return new StringResource(transformation.apply(value));
    }
}
