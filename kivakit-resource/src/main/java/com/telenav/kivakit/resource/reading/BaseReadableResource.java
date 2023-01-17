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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.io.IO;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.object.Lazy;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.progress.reporters.ProgressiveInputStream;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.CloseMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.writing.WritableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.io.IO.copy;
import static com.telenav.kivakit.core.io.IO.copyAndClose;
import static com.telenav.kivakit.core.object.Lazy.lazy;
import static com.telenav.kivakit.core.time.Time.now;
import static com.telenav.kivakit.filesystem.File.parseFile;
import static com.telenav.kivakit.filesystem.Folder.FolderType.CLEAN_UP_ON_EXIT;
import static com.telenav.kivakit.filesystem.Folder.temporaryFolderForProcess;
import static com.telenav.kivakit.resource.CloseMode.CLOSE;
import static com.telenav.kivakit.resource.ResourcePath.resourcePath;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.hash;

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
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class BaseReadableResource extends BaseRepeater implements Resource
{
    /**
     * The temporary cache folder for storing materialized files
     */
    private static final Lazy<Folder> cacheFolder = lazy(() ->
        temporaryFolderForProcess(CLEAN_UP_ON_EXIT).ensureExists());

    /**
     * Character mapping (default is UTF-8)
     */
    private Charset charset = UTF_8;

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

    protected BaseReadableResource(@NotNull ResourcePath path)
    {
        this.path = path;
    }

    protected BaseReadableResource(@NotNull BaseReadableResource that)
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
    public BaseReadableResource codec(@NotNull Codec codec)
    {
        this.codec = codec;
        return this;
    }

    /**
     * Copies the data in this resource to the destination.
     *
     * @param target The destination resource
     * @param writeMode The copying mode
     * @param closeMode True if the streams should be closed when copying is complete
     * @param reporter The progress reporter to call as copying proceeds
     * @throws IllegalStateException Thrown if the copy operation fails
     */
    @Override
    public void copyTo(@NotNull WritableResource target,
                       @NotNull WriteMode writeMode,
                       @NotNull CloseMode closeMode,
                       @NotNull ProgressReporter reporter)
    {
        // If we can copy from this resource to the given resource in this mode,
        writeMode.ensureAllowed(this, target);

        // copy the resource stream (which might involve compression or decompression or both).
        var input = openForReading(reporter);
        var output = target.openForWriting(writeMode);
        if (closeMode == CLOSE)
        {
            ensure(copyAndClose(this, input, output), "Unable to copy ($) $ => $", writeMode, this, target);
        }
        else
        {
            ensure(copy(this, input, output), "Unable to copy ($) $ => $", writeMode, this, target);
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
                    warning("Unable to dematerialize $", materialized);
                }
            }
        }
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof BaseReadableResource that)
        {
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
        return hash(path);
    }

    // Ensure the remote resource is locally accessible
    @Override
    public Resource materialized(@NotNull ProgressReporter reporter)
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
                        var start = now();
                        trace("Materializing $ to $", this, cached.path().asAbsolute());
                        safeCopyTo(cached, OVERWRITE, reporter);
                        trace("Materialized ${debug} ($) from ${debug} in ${debug}", cached.path().asAbsolute(),
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
    public InputStream openForReading(@NotNull ProgressReporter reporter)
    {
        // Open the input stream,
        var in = onOpenForReading();
        if (in == null)
        {
            problem("Unable to open: $", this);
            return null;
        }

        // add a decompression layer if need be,
        var decompressed = codec().decompressed(IO.buffer(in));

        // start the reporter
        reporter.start(fileName().name());
        reporter.steps(sizeInBytes());

        // and return a progressive input which will call the reporter.
        return new ProgressiveInputStream(decompressed, reporter);
    }

    @Override
    public ResourcePath path()
    {
        return path == null
            ? resourcePath("/unknown/" + getClass().getSimpleName() + "/" + hashCode() + ".hash")
            : path;
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
        return parseFile(this, cacheFolder.get() + "/" + path().toString().replaceAll("[/:]", "_"));
    }

    private String uniqueIdentifier()
    {
        var path = path();
        if (path instanceof FilePath filepath)
        {
            return filepath.asAbsolute().toString();
        }
        return path().toString();
    }
}
