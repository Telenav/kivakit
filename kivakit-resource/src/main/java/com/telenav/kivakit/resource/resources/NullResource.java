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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceType;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.nullListener;
import static com.telenav.kivakit.resource.ResourcePath.parseUnixResourcePath;

/**
 * A writable resource that accepts anything you write and destroys it, so when you read there is nothing to be seen. It
 * also has no contents when read.
 *
 * @author matthieun
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = UNTESTED)
public class NullResource extends BaseWritableResource
{
    public NullResource()
    {
        super(parseUnixResourcePath(nullListener(), "/objects/NullResource"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isWritable()
    {
        // A null resource accepts it all.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream onOpenForReading()
    {
        return new InputStream()
        {
            @Override
            public int read()
            {
                return -1;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream onOpenForWriting(WriteMode mode)
    {
        return new OutputStream()
        {
            @Override
            public void write(int b)
            {
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes sizeInBytes()
    {
        return null;
    }
}
