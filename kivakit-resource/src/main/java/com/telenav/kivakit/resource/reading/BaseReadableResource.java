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

package com.telenav.kivakit.resource.reading;

import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.lexakai.DiagramResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * A base implementation of the {@link Resource} interface. Adds the following methods:
 *
 * <ul>
 *     <li>{@link #codec(Codec)} - Applies a {@link Codec} to this resource</li>
 * </ul>
 * <p>
 * All other methods are documented in the {@link Resource} superinterface.
 */
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@LexakaiJavadoc(complete = true)
public abstract class BaseReadableResource extends BaseRepeater implements Resource
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /**
     * The temporary cache folder for storing materialized files
     */
    private static final Lazy<Folder> cacheFolder = Lazy.of(() ->
            Folder.temporaryForProcess(Folder.Type.CLEAN_UP_ON_EXIT).ensureExists());

    /**
     * Character mapping (default is UTF-8)
     */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * Compression (default is none, but may be derived from resource extension)
     */
    private Codec codec;

    /**
     * Local copy if the resource is cached from a remote location
     */
    private File materialized;

    /**
     * StringPath to resource
     */
    private final ResourcePath path;

    /**
     * For serialization
     */
    protected BaseReadableResource()
    {
        path = null;
    }

    protected BaseReadableResource(ResourcePath path)
    {
        this.path = path;
    }

    protected BaseReadableResource(BaseReadableResource that)
    {
        path = that.path;
        codec = that.codec;
        charset = that.charset;
        materialized = that.materialized;
    }

    @Override
    public final Charset charset()
    {
        return charset;
    }

    @Override
    public Codec codec()
    {
        if (codec == null)
        {
            if (extension() != null)
            {
                codec = extension().codec();
            }
            else
            {
                codec = new NullCodec();
            }
        }
        return codec;
    }

    @SuppressWarnings("UnusedReturnValue")
    public BaseReadableResource codec(Codec codec)
    {
        this.codec = codec;
        return this;
    }

    /**
     * Copies the data in this resource to the destination.
     */
    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter)
    {
        // If we can copy from this resource to the given resource in this mode,
        if (mode.canCopy(this, destination))
        {
            // copy the resource stream (which might involve compression or decompression or both).
            var input = openForReading(reporter);
            var output = destination.openForWriting();
            if (!IO.copyAndClose(input, output))
            {
                throw new IllegalStateException("Unable to copy " + this + " to " + destination);
            }
        }
    }

    @Override
    public boolean delete()
    {
        return unsupported();
    }

    /**
     * Remove any materialized local copy if this is a remote resource that's been cached
     */
    @Override
    public void dematerialize()
    {
        synchronized (uniqueIdentifier())
        {
            if (materialized != null && materialized.exists())
            {
                try
                {
                    materialized.delete();
                    trace("Dematerialized ${debug} from local cache", materialized);
                    materialized = null;
                }
                catch (Exception e)
                {
                    LOGGER.warning("Unable to dematerialize $", materialized);
                }
            }
        }
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof BaseReadableResource)
        {
            BaseReadableResource that = (BaseReadableResource) object;
            return this.path.equals(that.path);
        }
        return false;
    }

    @Override
    public boolean exists()
    {
        try
        {
            InputStream in = null;
            var exists = false;
            try
            {
                in = openForReading();
                exists = in != null;
            }
            finally
            {
                if (in != null)
                {
                    in.close();
                }
            }
            return exists;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(path);
    }

    // Ensure the remote resource is locally accessible
    @Override
    public Resource materialized(ProgressReporter reporter)
    {
        synchronized (uniqueIdentifier())
        {
            if (isRemote() || isPackaged())
            {
                if (materialized == null)
                {
                    var cached = cacheFile();
                    if (!cached.exists())
                    {
                        cached.parent().ensureExists();
                        var start = Time.now();
                        trace("Materializing $ to $", this, cached.path().absolute());
                        safeCopyTo(cached, CopyMode.OVERWRITE, reporter);
                        trace("Materialized ${debug} ($) from ${debug} in ${debug}", cached.path().absolute(),
                                cached.sizeInBytes(), this, start.elapsedSince());
                    }
                    materialized = cached;
                }
                return materialized;
            }
            return this;
        }
    }

    @Override
    public InputStream openForReading(ProgressReporter reporter)
    {
        // Open the input stream,
        var in = onOpenForReading();
        if (in == null)
        {
            return null;
        }

        // add a decompression layer if need be,
        var decompressed = codec().decompressed(IO.buffer(in));

        // and if there is a reporter,
        if (reporter != null)
        {
            // start it up
            reporter.start(fileName().name());
            reporter.steps(sizeInBytes());

            // and return a progressive input which will call the reporter.
            return reporter.progressiveInput(decompressed);
        }

        return decompressed;
    }

    @Override
    public ResourcePath path()
    {
        return path;
    }

    @Override
    public String toString()
    {
        return fileName().toString();
    }

    protected void charset(Charset charset)
    {
        this.charset = charset;
    }

    private File cacheFile()
    {
        // Flatten path being cached into a long filename by turning all file system meta characters
        // into underscores.
        // For example, "a/b/c.txt" becomes "a_b_c.txt"
        return File.parseFile(this, cacheFolder.get() + "/" + path().toString().replaceAll("[/:]", "_"));
    }

    private String uniqueIdentifier()
    {
        var path = path();
        if (path instanceof FilePath)
        {
            var filepath = (FilePath) path;
            return filepath.absolute().toString();
        }
        return path().toString();
    }
}
