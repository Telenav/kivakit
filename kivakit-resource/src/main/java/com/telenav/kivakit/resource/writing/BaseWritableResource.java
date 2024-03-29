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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.reading.BaseReadableResource;
import com.telenav.kivakit.resource.reading.ReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.io.IO.close;
import static com.telenav.kivakit.core.io.IO.copyAndClose;

/**
 * Extends {@link BaseReadableResource} and provides a base implementation of the {@link WritableResource} interface.
 *
 * <p><b>Writing</b></p>
 *
 * <ul>
 *     <li>{@link #copyFrom(Resource, WriteMode, ProgressReporter)} - Copies to this resource from the given resource</li>
 *     <li>{@link #save(Listener, InputStream, ProgressReporter)} - Copies the given input to this resource</li>
 *     <li>{@link #saveText(String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Resource
 * @see WritableResource
 * @see ReadableResource
 * @see BaseReadableResource
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             documentation = DOCUMENTED,
             testing = UNTESTED)
public abstract class BaseWritableResource extends BaseReadableResource implements WritableResource
{
    protected BaseWritableResource()
    {
    }

    protected BaseWritableResource(@NotNull BaseWritableResource that)
    {
        super(that);
    }

    protected BaseWritableResource(@NotNull ResourcePath path)
    {
        super(path);
    }

    /**
     * Copies the given source resource to this resource.
     *
     * @param source The resource to copy from
     */
    public void copyFrom(@NotNull Resource source,
                         @NotNull WriteMode mode,
                         @NotNull ProgressReporter reporter)
    {
        source.copyTo(this, mode, reporter);
    }

    /**
     * Returns an {@link OutputStream} for this resource, or an exception is thrown
     */
    @Override
    public OutputStream openForWriting(WriteMode mode)
    {
        return codec().compressed(new BufferedOutputStream(onOpenForWriting(mode)));
    }

    /**
     * Saves the given input stream into this file
     */
    public void save(@NotNull Listener listener,
                     @NotNull InputStream in,
                     @NotNull ProgressReporter reporter)
    {
        var out = openForWriting(reporter);
        copyAndClose(listener, in, out);
        close(listener, out);
    }

    /**
     * Prints the given text to this resource
     *
     * @param text The text to print
     * @throws IllegalStateException Thrown if the text cannot be saved
     */
    public void saveText(@NotNull String text)
    {
        writer().saveText(text);
    }
}
