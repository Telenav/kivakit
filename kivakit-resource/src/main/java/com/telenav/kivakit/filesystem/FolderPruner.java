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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.kernel.language.threading.RepeatingKivaKitThread;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.kernel.language.values.level.Percent;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Removes nested files matching {@link #matcher(Matcher)} from the given folder when they meet expiration criteria.
 *
 * <p><b>Expiration Criteria</b></p>
 *
 * <p>
 * The minimum age of surviving files can be set with {@link #minimumAge(Duration)}. Files will also be pruned (from
 * oldest to newest) if disk space falls below {@link #minimumUsableDiskSpace(Percent)} or when the folder's total size
 * exceeds {@link #capacity(Bytes)}.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@UmlRelation(label = "prunes old files from", referent = Folder.class)
@LexakaiJavadoc(complete = true)
public class FolderPruner
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** Matcher to restrict files that can be pruned */
    private volatile Matcher<File> matcher = new AnythingMatcher<>();

    /** The minimum percentage of usable disk space that must be maintained on the folder's disk. */
    private volatile Percent minimumUsableDiskSpace = Percent.of(15);

    /** The minimum age for a file to be pruned */
    private volatile Duration minimumAge = Duration.weeks(2);

    /** The maximum folder capacity */
    private volatile Bytes capacity = Bytes.MAXIMUM;

    /** The pruner thread */
    private final RepeatingKivaKitThread thread;

    /** True if this pruner is running */
    private volatile boolean running;

    public FolderPruner(final Folder folder, final Frequency frequency)
    {
        thread = new RepeatingKivaKitThread(LOGGER, getClass().getSimpleName(), frequency)
        {
            @Override
            protected void onRun()
            {
                try
                {
                    // Get files that we could delete, sorted from oldest to newest
                    final var files = folder.nestedFiles(matcher());
                    files.sortedOldestToNewest();

                    // Determine size of all removable files
                    var size = files.totalSize();

                    // For each file we can remove
                    for (final var file : files)
                    {
                        // If we're low on disk or we're exceeding the folder capacity
                        if (folder.disk().percentUsable().isLessThan(minimumUsableDiskSpace())
                                || size.isGreaterThan(capacity()))
                        {
                            // and the file is old enough to remove and we can remove it
                            if (age(file).isGreaterThan(minimumAge()) && canRemove(file, files))
                            {
                                // then remove the file and adjust the folder size
                                onFileRemoved(file);
                                final var length = file.bytes();
                                file.delete();
                                size = size.minus(length);
                            }
                        }
                    }
                }
                catch (final Exception e)
                {
                    LOGGER.problem(e, "Folder pruner threw exception");
                }
            }
        };
        thread.daemon(true);
        thread.addListener(LOGGER);
    }

    public void capacity(final Bytes capacity)
    {
        this.capacity = capacity;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void matcher(final Matcher<File> matcher)
    {
        this.matcher = matcher;
    }

    public void minimumAge(final Duration minimumAge)
    {
        this.minimumAge = minimumAge;
    }

    public void minimumUsableDiskSpace(final Percent minimumUsableDiskSpace)
    {
        this.minimumUsableDiskSpace = minimumUsableDiskSpace;
    }

    public void start()
    {
        thread.start();
        running = true;
    }

    public void stop(final Duration maximumWaitTime)
    {
        thread.stop(maximumWaitTime);
        running = false;
    }

    /**
     * Allows subclass to determine the file age (because it may want to infer the date of the file from the filename or
     * some other source instead of using the OS time)
     */
    protected Duration age(final File file)
    {
        return file.lastModified().elapsedSince();
    }

    /**
     * @param candidate The candidate file
     * @param files All files in the repository
     * @return True if the candidate file can be removed
     */
    @SuppressWarnings({ "SameReturnValue" })
    protected boolean canRemove(final File candidate, final FileList files)
    {
        return true;
    }

    protected Bytes capacity()
    {
        return capacity;
    }

    protected Matcher<File> matcher()
    {
        return matcher;
    }

    protected Duration minimumAge()
    {
        return minimumAge;
    }

    protected Percent minimumUsableDiskSpace()
    {
        return minimumUsableDiskSpace;
    }

    protected void onFileRemoved(final File file)
    {
        LOGGER.warning("FolderPruner removing $", file);
    }
}
