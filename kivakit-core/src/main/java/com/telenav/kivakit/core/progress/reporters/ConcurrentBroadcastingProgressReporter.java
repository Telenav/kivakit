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

package com.telenav.kivakit.core.progress.reporters;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramProgress;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.value.count.Count.count;

/**
 * A thread-safe subclass of {@link BroadcastingProgressReporter}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramProgress.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ConcurrentBroadcastingProgressReporter extends BroadcastingProgressReporter
{
    /** The current step we are at. This storage is thread-safe */
    private final AtomicLong at;

    /** The number of problems encountered */
    private final AtomicLong problems;

    public ConcurrentBroadcastingProgressReporter(BroadcastingProgressReporter that)
    {
        super(that);
        at = new AtomicLong(that.at().asLong());
        problems = new AtomicLong(that.at().asLong());
    }

    protected ConcurrentBroadcastingProgressReporter()
    {
        at = new AtomicLong();
        problems = new AtomicLong();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void at(Count count)
    {
        at.set(count.longValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count at()
    {
        return count(at.get());
    }

    @Override
    public Count problems()
    {
        return count(problems.get());
    }

    @Override
    public void problems(long problems)
    {
        this.problems.addAndGet(problems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull
    BroadcastingProgressReporter copy()
    {
        return new ConcurrentBroadcastingProgressReporter(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long increase(long increase)
    {
        return at.addAndGet(increase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long increment()
    {
        return at.incrementAndGet();
    }
}
