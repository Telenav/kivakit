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

import com.telenav.kivakit.core.project.lexakai.DiagramProgress;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe subclass of {@link BroadcastingProgressReporter}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramProgress.class)
public class ConcurrentBroadcastingProgressReporter extends BroadcastingProgressReporter
{
    private final AtomicLong at;

    public ConcurrentBroadcastingProgressReporter(BroadcastingProgressReporter that)
    {
        super(that);
        at = new AtomicLong(that.at());
    }

    protected ConcurrentBroadcastingProgressReporter()
    {
        at = new AtomicLong();
    }

    @Override
    public void at(long count)
    {
        at.set(count);
    }

    @Override
    public long at()
    {
        return at.get();
    }

    @Override
    protected long increase(long increase)
    {
        return at.addAndGet(increase);
    }

    @Override
    protected long increment()
    {
        return at.incrementAndGet();
    }

    @Override
    protected @NotNull
    BroadcastingProgressReporter newInstance()
    {
        return new ConcurrentBroadcastingProgressReporter(this);
    }
}
