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

package com.telenav.kivakit.kernel.language.patterns.logical;

import com.telenav.kivakit.kernel.language.patterns.Pattern;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public final class Or extends Pattern
{
    private final Pattern a;

    private final Pattern b;

    public Or(final Pattern a, final Pattern b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public int bind(final int group)
    {
        return b.bind(a.bind(group));
    }

    @Override
    public String toExpression()
    {
        return a + "|" + b;
    }
}
