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

package com.telenav.kivakit.core.resource.writing;

import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.WritableResource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.core.resource.reading.BaseReadableResource;
import com.telenav.kivakit.core.kernel.language.io.IO;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;

@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
public abstract class BaseWritableResource extends BaseReadableResource implements WritableResource
{
    protected BaseWritableResource()
    {
    }

    protected BaseWritableResource(final BaseWritableResource that)
    {
        super(that);
    }

    protected BaseWritableResource(final ResourcePath path)
    {
        super(path);
    }

    /**
     * Copies the given source resource to this resource.
     *
     * @param source The resource to copy from
     */
    public void copyFrom(final Resource source, final CopyMode mode, final ProgressReporter reporter)
    {
        source.copyTo(this, mode, reporter);
    }

    public boolean delete()
    {
        return false;
    }

    /**
     *
     */
    public ObjectOutputStream objectOutputStream()
    {
        return writer().objectOutputStream();
    }

    @Override
    public OutputStream openForWriting()
    {
        return codec().compressed(new BufferedOutputStream(onOpenForWriting()));
    }

    public void print(final String output)
    {
        try (final var out = printWriter())
        {
            out.print(output);
        }
    }

    /**
     * Saves the given input stream into this file
     */
    public void save(final InputStream in, final ProgressReporter reporter)
    {
        final var out = openForWriting(reporter);
        IO.copyAndClose(in, out);
        IO.close(out);
    }

    public Writer textWriter()
    {
        return writer().textWriter();
    }
}
