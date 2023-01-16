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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.resource.WriteMode;
import com.telenav.kivakit.resource.FileName;
import com.telenav.kivakit.resource.Resource;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.progress.ProgressReporter.nullProgressReporter;
import static com.telenav.kivakit.core.time.Frequency.EVERY_30_SECONDS;
import static com.telenav.kivakit.core.value.level.Percent.percent;

/**
 * A file cache with the given root. Resources can be copied into the cache with
 * {@link #add(Resource, WriteMode, ProgressReporter)}. Files can be retrieved with {@link #file(FileName)}. The cache
 * can be pruned of old files by calling {@link #startPruner()}.
 *
 * <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #add(Resource, WriteMode)}</li>
 *     <li>{@link #add(Resource, WriteMode, ProgressReporter)}</li>
 *     <li>{@link #addAs(Resource, FileName, WriteMode)}</li>
 *     <li>{@link #addAs(Resource, FileName, WriteMode, ProgressReporter)}</li>
 * </ul>
 *
 * <p><b>Retrieving</b></p>
 *
 * <ul>
 *     <li>{@link #file(FileName)}</li>
 *     <li>{@link #folder(String)}</li>
 * </ul>
 *
 * <p><b>Pruning</b></p>
 *
 * <ul>
 *     <li>{@link #maximumAge(Duration)}</li>
 *     <li>{@link #minimumUsableDiskSpace(Percent)}</li>
 *     <li>{@link #startPruner()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FileCache extends BaseRepeater
{
    /** Storage for cached files */
    private final Folder cacheFolder;

    /** Maximum age beyond which files are pruned */
    private Duration maximumAge;

    /** Minimum usable disk space below which files are pruned */
    private Percent minimumUsableDiskSpace = percent(10);

    /**
     * @param cacheFolder The cache folder where files should be stored
     */
    public FileCache(@NotNull Folder cacheFolder)
    {
        this.cacheFolder = cacheFolder.mkdirs();

        ensure(cacheFolder.exists(), "Unable to create root folder for cache: " + cacheFolder);
    }

    /**
     * Adds the given resource to this cache
     *
     * @param resource The resource to add to the cache
     * @param mode How the resource should be copied
     */
    public File add(@NotNull Resource resource, @NotNull WriteMode mode)
    {
        return add(resource, mode, nullProgressReporter());
    }

    /**
     * Adds the given resource to this cache
     *
     * @param resource The resource to add to the cache
     * @param mode How the resource should be copied
     * @param reporter The progress reporter to call as the file is being copied into the cache
     */
    public File add(@NotNull Resource resource,
                    @NotNull WriteMode mode,
                    @NotNull ProgressReporter reporter)
    {
        return addAs(resource, resource.fileName(), mode, reporter);
    }

    /**
     * Adds the given resource as a file in this cache with the given filename
     *
     * @param resource The resource to add to the cache
     * @param filename The name of the file to write to in this cache
     * @param mode How the resource should be copied
     */
    public File addAs(@NotNull Resource resource,
                      @NotNull FileName filename,
                      @NotNull WriteMode mode)
    {
        return addAs(resource, filename, mode, nullProgressReporter());
    }

    /**
     * Adds the given resource as a file in this cache with the given filename
     *
     * @param resource The resource to add to the cache
     * @param filename The name of the file to write to in this cache
     * @param mode How the resource should be copied
     * @param reporter The progress reporter to call as the file is being copied into the cache
     */
    public synchronized File addAs(@NotNull Resource resource,
                                   @NotNull FileName filename,
                                   @NotNull WriteMode mode,
                                   @NotNull ProgressReporter reporter)
    {
        var file = file(filename);
        if (!file.exists())
        {
            resource.safeCopyTo(file, mode, reporter);
        }
        return file;
    }

    /**
     * Returns the given file in this cache
     */
    public File file(@NotNull FileName name)
    {
        return cacheFolder.file(name);
    }

    /**
     * Returns a sub-folder in the cache folder with the given name
     */
    public Folder folder(@NotNull String name)
    {
        return cacheFolder.folder(name);
    }

    /**
     * Sets the maximum age of files in the cache before they can be pruned
     */
    public void maximumAge(@NotNull Duration maximumAge)
    {
        this.maximumAge = maximumAge;
    }

    /**
     * Sets the minimum disk space below which files are pruned from the cache
     */
    public void minimumUsableDiskSpace(@NotNull Percent minimum)
    {
        this.minimumUsableDiskSpace = minimum;
    }

    /**
     * Starts the thread that prunes old files
     */
    public void startPruner()
    {
        // Start folder pruner
        var pruner = new FolderPruner(cacheFolder, EVERY_30_SECONDS);
        pruner.minimumUsableDiskSpace(minimumUsableDiskSpace);
        pruner.maximumAge(maximumAge);
        pruner.start();
    }
}
