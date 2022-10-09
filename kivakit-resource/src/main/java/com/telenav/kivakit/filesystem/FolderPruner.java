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

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.thread.RepeatingThread;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.lifecycle.Startable;
import com.telenav.kivakit.interfaces.lifecycle.Stoppable;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.time.Duration.seconds;

/**
 * Removes nested files matching {@link #matcher(Matcher)} from the given folder when they meet expiration criteria.
 *
 * <p><b>Expiration Criteria</b></p>
 *
 * <p>
 * The maximum age of files can be set with {@link #maximumAge(Duration)}. Files will also be pruned (from oldest to
 * newest) if disk space falls below {@link #minimumUsableDiskSpace(Percent)} or when the folder's total size exceeds
 * {@link #capacity(Bytes)}.
 * </p>
 *
 * <p><b>Lifecycle</b></p>
 *
 * <ul>
 *     <li>{@link #isRunning()}</li>
 *     <li>{@link #start()}</li>
 *     <li>{@link #stop(Duration)}</li>
 *     <li>{@link #maximumStopTime()}</li>
 * </ul>
 *
 * <p><b>Pruning Criteria</b></p>
 *
 * <ul>
 *     <li>{@link #capacity()}</li>
 *     <li>{@link #capacity(Bytes)}</li>
 *     <li>{@link #matcher()}</li>
 *     <li>{@link #matcher(Matcher)}</li>
 *     <li>{@link #maximumAge(Duration)}</li>
 *     <li>{@link #minimumUsableDiskSpace(Percent)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@UmlRelation(label = "prunes old files from", referent = Folder.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public class FolderPruner extends BaseRepeater implements
        Startable,
        Stoppable<Duration>
{
    /** The maximum folder capacity */
    private volatile Bytes capacity = Bytes.MAXIMUM;

    /** Matcher to restrict files that can be pruned */
    private volatile Matcher<ResourcePathed> matcher = Matcher.matchAll();

    /** The maximum age at which a file will be pruned */
    private volatile Duration maximumAge = Duration.weeks(2);

    /** The minimum percentage of usable disk space that must be maintained on the folder's disk. */
    private volatile Percent minimumUsableDiskSpace = Percent.percent(15);

    /** True if this pruner is running */
    private volatile boolean running;

    /** The pruner thread */
    private final RepeatingThread thread;

    public FolderPruner(@NotNull Folder folder, @NotNull Frequency frequency)
    {
        thread = new RepeatingThread(this, getClass().getSimpleName(), frequency)
        {
            @Override
            protected void onRun()
            {
                try
                {
                    // Get files that we could delete, sorted from oldest to newest
                    var files = folder.nestedFiles(matcher());
                    files.sortedOldestToNewest();

                    // Determine size of all removable files
                    var size = files.totalSize();

                    // For each file we can remove
                    for (var file : files)
                    {
                        // If we're low on disk or we're exceeding the folder capacity
                        if (folder.disk().percentUsable().isLessThan(minimumUsableDiskSpace())
                                || size.isGreaterThan(capacity()))
                        {
                            // and the file is old enough to remove and we can remove it
                            if (age(file).isGreaterThanOrEqualTo(maximumAge()) && canRemove(file, files))
                            {
                                // then remove the file and adjust the folder size
                                var length = file.sizeInBytes();
                                file.delete();
                                size = size.minus(length);
                                onFileRemoved(file);
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    problem(e, "Folder pruner threw exception");
                }
            }
        };
        thread.daemon(true);
        thread.addListener(this);
    }

    /**
     * Sets the capacity of the folder before pruning occurs
     */
    public void capacity(@NotNull Bytes capacity)
    {
        this.capacity = capacity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Sets the matcher to use when selecting resources to prune
     */
    public void matcher(@NotNull Matcher<ResourcePathed> matcher)
    {
        this.matcher = matcher;
    }

    /**
     * Sets the maximum age at which resources can be expired
     */
    public void maximumAge(@NotNull Duration maximumAge)
    {
        this.maximumAge = maximumAge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration maximumStopTime()
    {
        return seconds(30);
    }

    /**
     * Sets the minimum usable disk space below which pruning occurs
     */
    public void minimumUsableDiskSpace(@NotNull Percent minimumUsableDiskSpace)
    {
        this.minimumUsableDiskSpace = minimumUsableDiskSpace;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean start()
    {
        thread.start();
        running = true;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(@NotNull Duration maximumWaitTime)
    {
        thread.stop(maximumWaitTime);
        running = false;
    }

    /**
     * Allows subclass to determine the file age (because it may want to infer the date of the file from the filename or
     * some other source instead of using the OS time)
     */
    protected Duration age(@NotNull File file)
    {
        return file.lastModified().elapsedSince();
    }

    /**
     * @param candidate The candidate file
     * @param files All files in the repository
     * @return True if the candidate file can be removed
     */
    @SuppressWarnings({ "SameReturnValue", "unused" })
    protected boolean canRemove(@NotNull File candidate,
                                @NotNull FileList files)
    {
        return true;
    }

    protected Bytes capacity()
    {
        return capacity;
    }

    protected Matcher<ResourcePathed> matcher()
    {
        return matcher;
    }

    protected Duration maximumAge()
    {
        return maximumAge;
    }

    protected Percent minimumUsableDiskSpace()
    {
        return minimumUsableDiskSpace;
    }

    /**
     * Called when files are removed
     */
    protected void onFileRemoved(@NotNull File file)
    {
        warning("FolderPruner removing $", file);
    }
}
