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

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.CopyMode;
import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.core.resource.path.FileName;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * A file cache with the given root. Resources can be copied into the cache with {@link #add(Resource, CopyMode,
 * ProgressReporter)}. Files can be retrieved with {@link #file(FileName)}.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class FileCache
{
    private final Folder cacheFolder;

    /**
     * @param cacheFolder The cache folder where files should be stored
     */
    public FileCache(final Folder cacheFolder)
    {
        this.cacheFolder = cacheFolder;
        if (!this.cacheFolder.mkdirs().exists())
        {
            throw new IllegalStateException("Unable to create root folder for cache: " + this.cacheFolder);
        }
    }

    /**
     * @param resource The resource to add to the cache
     * @param mode How the resource should be copied
     * @param reporter The progress reporter to call as the file is being copied into the cache
     */
    public File add(final Resource resource, final CopyMode mode, final ProgressReporter reporter)
    {
        final var file = file(resource.fileName());
        if (!file.exists())
        {
            resource.safeCopyTo(file, mode, reporter);
        }
        return file;
    }

    /**
     * @return The given file in this cache
     */
    public File file(final FileName name)
    {
        return cacheFolder.file(name);
    }

    /**
     * @return A sub-folder in the cache folder with the given name
     */
    public Folder folder(final String name)
    {
        return cacheFolder.folder(name);
    }
}
