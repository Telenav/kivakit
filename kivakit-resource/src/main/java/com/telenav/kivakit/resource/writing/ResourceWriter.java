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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * This class provides methods that write to a {@link WritableResource}. An instance of this class can be created with
 * {@link WritableResource#writer()} or {@link WritableResource#writer(Charset)}. This functionality is provided
 * separately from {@link WritableResource} so it doesn't clutter up that class and so it can be extended in the
 * future.
 *
 * <p><b>Writing</b></p>
 *
 * <ul>
 *     <li>{@link #charset()}</li>
 *     <li>{@link #printWriter()}</li>
 *     <li>{@link #saveText(String)}</li>
 *     <li>{@link #textWriter()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTATION_COMPLETE,
             testing = UNTESTED)
public class ResourceWriter
{
    /** The resource to write to */
    private final WritableResource resource;

    /** The character set to use */
    private final Charset charset;

    public ResourceWriter(@NotNull WritableResource resource)
    {
        this.resource = resource;
        this.charset = UTF_8;
    }

    public ResourceWriter(@NotNull WritableResource resource,
                          @NotNull Charset charset)
    {
        this.resource = resource;
        this.charset = charset;
    }

    /**
     * Returns the character set being used by this text writer
     */
    public Charset charset()
    {
        return charset;
    }

    /**
     * Returns a {@link PrintWriter} that writes to this resource. The writer must be closed by the caller.
     *
     * @return The writer
     */
    public PrintWriter printWriter()
    {
        return new PrintWriter(textWriter());
    }

    /**
     * Saves the given text to this resource
     *
     * @param text The text
     */
    public void saveText(@NotNull String text)
    {
        try (var out = printWriter())
        {
            out.print(text);
        }
    }

    /**
     * Returns a text {@link Writer} to this resource. The writer must be closed by the caller.
     *
     * @return The writer
     */
    public Writer textWriter()
    {
        var out = resource.openForWriting();
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
