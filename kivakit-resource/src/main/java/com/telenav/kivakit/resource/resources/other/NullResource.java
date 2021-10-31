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

package com.telenav.kivakit.resource.resources.other;

import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.resource.writing.BaseWritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A writable resource that accepts anything you write and destroys it, so when you read there is nothing to be seen.
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramResourceType.class)
@LexakaiJavadoc(complete = true)
public class NullResource extends BaseWritableResource
{
    public NullResource()
    {
        super(ResourcePath.parseUnixResourcePath(Listener.none(), "/objects/NullResource"));
    }

    @Override
    public Boolean isWritable()
    {
        // A null resource accepts it all.
        return true;
    }

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

    @Override
    public OutputStream onOpenForWriting()
    {
        return new OutputStream()
        {
            @Override
            public void write(int b)
            {
            }
        };
    }

    @Override
    public Bytes sizeInBytes()
    {
        return null;
    }
}
