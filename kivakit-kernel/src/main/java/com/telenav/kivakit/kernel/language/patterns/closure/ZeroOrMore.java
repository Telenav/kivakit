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

package com.telenav.kivakit.kernel.language.patterns.closure;

import com.telenav.kivakit.kernel.language.patterns.Pattern;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class ZeroOrMore extends Closure
{
    private final Pattern pattern;

    public ZeroOrMore(final Pattern pattern)
    {
        this(pattern, false);
    }

    public ZeroOrMore(final Pattern pattern, final boolean greedy)
    {
        super(greedy);
        this.pattern = pattern;
    }

    @Override
    public int bind(final int group)
    {
        return pattern.bind(group);
    }

    public ZeroOrMore greedy()
    {
        return new ZeroOrMore(pattern, true);
    }

    public ZeroOrMore nonGreedy()
    {
        return new ZeroOrMore(pattern, false);
    }

    @Override
    public String toExpression()
    {
        return pattern + "*" + greed();
    }
}
