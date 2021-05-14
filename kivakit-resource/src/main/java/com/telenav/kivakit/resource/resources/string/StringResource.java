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

package com.telenav.kivakit.resource.resources.string;

import com.telenav.kivakit.kernel.language.io.StringInput;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.ReadableResource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
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

    public StringResource(final String value)
    {
        this(ResourcePath.parseUnixResourcePath("/objects/String@" + Integer.toHexString(value.hashCode())), value);
    }

    public StringResource(final ResourcePath path, final String value)
    {
        super(path);
        this.value = value;
    }

    @Override
    public Bytes bytes()
    {
        return Bytes.bytes(value.length());
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new StringInput(value);
    }

    public StringResource transform(final Function<String, String> transformation)
    {
        return new StringResource(transformation.apply(value));
    }
}
