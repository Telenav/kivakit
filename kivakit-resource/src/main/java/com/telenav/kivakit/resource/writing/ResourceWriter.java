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

package com.telenav.kivakit.resource.writing;

import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * This class provides methods that write to a {@link WritableResource}. An instance of this class can be created with
 * {@link WritableResource#writer()} or {@link WritableResource#writer(Charset)}. This functionality is provided
 * separately from {@link WritableResource} so it doesn't clutter up that class and so it can be extended in the
 * future.
 *
 * <p><b>Writers</b></p>
 *
 * <p>
 * A {@link PrintWriter} can be obtained with {@link #printWriter()} and a {@link Writer} for performing text output
 * with {@link #textWriter()}.
 * </p>
 *
 * <p><b>Saving</b></p>
 *
 * <p>
 * A string can be saved with {@link #save(String)}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@LexakaiJavadoc(complete = true)
public class ResourceWriter
{
    private final WritableResource resource;

    private final Charset charset;

    public ResourceWriter(final WritableResource resource)
    {
        this.resource = resource;
        charset = null;
    }

    public ResourceWriter(final WritableResource resource, final Charset charset)
    {
        this.resource = resource;
        this.charset = charset;
    }

    public Charset charset()
    {
        return charset;
    }

    public PrintWriter printWriter()
    {
        return new PrintWriter(textWriter());
    }

    public void save(final String string)
    {
        try (final var out = printWriter())
        {
            out.print(string);
        }
    }

    public Writer textWriter()
    {
        final var out = resource.openForWriting();
        if (charset() == null)
        {
            return new OutputStreamWriter(out);
        }
        else
        {
            return new OutputStreamWriter(out, charset());
        }
    }

    @Override
    public String toString()
    {
        return resource.toString();
    }
}
