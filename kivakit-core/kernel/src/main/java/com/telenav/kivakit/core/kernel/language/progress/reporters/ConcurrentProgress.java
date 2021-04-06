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

package com.telenav.kivakit.core.kernel.language.progress.reporters;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageProgress;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe subclass of {@link Progress}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageProgress.class)
public class ConcurrentProgress extends Progress
{
    private final AtomicLong at;

    public ConcurrentProgress(final Progress that)
    {
        super(that);
        at = new AtomicLong(that.at());
    }

    protected ConcurrentProgress()
    {
        at = new AtomicLong();
    }

    @Override
    public void at(final long count)
    {
        at.set(count);
    }

    @Override
    public long at()
    {
        return at.get();
    }

    @Override
    protected long increase(final long increase)
    {
        return at.addAndGet(increase);
    }

    @Override
    protected long increment()
    {
        return at.incrementAndGet();
    }

    @Override
    protected @NotNull Progress newInstance()
    {
        return new ConcurrentProgress(this);
    }
}
