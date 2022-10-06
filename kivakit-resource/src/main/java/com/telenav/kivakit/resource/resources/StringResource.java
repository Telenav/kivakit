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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.io.StringInputStream;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.kivakit.resource.reading.ReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.resource.ResourcePath.parseUnixResourcePath;

/**
 * A {@link ReadableResource} formed from a string passed to the constructor {@link #StringResource(String)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = TESTING_NONE)
public class StringResource extends BaseReadableResource
{
    /** The text to read */
    private final String text;

    /** The time of creation of this object */
    private final Time created = Time.now();

    /**
     * @param text The text to read
     */
    public StringResource(@NotNull String text)
    {
        this(parseUnixResourcePath(nullListener(),
                "/objects/String@" + Integer.toHexString(text.hashCode())), text);
    }

    /**
     * @param path The made-up path to use
     * @param text The text to read
     */
    public StringResource(@NotNull ResourcePath path,
                          @NotNull String text)
    {
        super(path);
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Time createdAt()
    {
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        return new StringInputStream(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return Bytes.bytes(text.length());
    }

    /**
     * Returns a new string resource with the given transformation applied
     *
     * @param transformation The transformation to apply to this string resource
     * @return The resource to read
     */
    public StringResource transform(@NotNull Function<String, String> transformation)
    {
        return new StringResource(transformation.apply(text));
    }
}
