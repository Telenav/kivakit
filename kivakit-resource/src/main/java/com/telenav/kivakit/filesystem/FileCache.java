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

import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.path.FileName;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.time.Frequency.EVERY_30_SECONDS;

/**
 * A file cache with the given root. Resources can be copied into the cache with {@link #add(Resource, CopyMode,
 * ProgressReporter)}. Files can be retrieved with {@link #file(FileName)}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class FileCache extends BaseRepeater
{
    private final Folder cacheFolder;

    /**
     * @param cacheFolder The cache folder where files should be stored
     */
    public FileCache(Folder cacheFolder)
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
    public File add(Resource resource, CopyMode mode)
    {
        return add(resource, mode, ProgressReporter.none());
    }

    /**
     * Adds the given resource to this cache
     *
     * @param resource The resource to add to the cache
     * @param mode How the resource should be copied
     * @param reporter The progress reporter to call as the file is being copied into the cache
     */
    public File add(Resource resource, CopyMode mode, ProgressReporter reporter)
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
    public File addAs(Resource resource, FileName filename, CopyMode mode)
    {
        return addAs(resource, filename, mode, ProgressReporter.none());
    }

    /**
     * Adds the given resource as a file in this cache with the given filename
     *
     * @param resource The resource to add to the cache
     * @param filename The name of the file to write to in this cache
     * @param mode How the resource should be copied
     * @param reporter The progress reporter to call as the file is being copied into the cache
     */
    public synchronized File addAs(Resource resource,
                                   FileName filename,
                                   CopyMode mode,
                                   ProgressReporter reporter)
    {
        var file = file(filename);
        if (!file.exists())
        {
            resource.safeCopyTo(file, mode, reporter);
        }
        return file;
    }

    /**
     * @return The given file in this cache
     */
    public File file(FileName name)
    {
        return cacheFolder.file(name);
    }

    /**
     * @return A sub-folder in the cache folder with the given name
     */
    public Folder folder(String name)
    {
        return cacheFolder.folder(name);
    }

    public void startPruner()
    {
        // Start folder pruner
        var pruner = new FolderPruner(cacheFolder, EVERY_30_SECONDS);
        pruner.minimumUsableDiskSpace(Percent.of(10));
        pruner.minimumAge(Duration.days(30));
        pruner.start();
    }
}
