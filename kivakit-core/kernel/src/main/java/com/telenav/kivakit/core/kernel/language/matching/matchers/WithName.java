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

package com.telenav.kivakit.core.kernel.language.matching.matchers;

import com.telenav.kivakit.core.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageMatchers;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link Matcher} that matches objects with the given name
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageMatchers.class)
public class WithName<T extends Named> implements Matcher<T>
{
    private final String name;

    public WithName(final Named named)
    {
        name = named.name();
    }

    public WithName(final String name)
    {
        this.name = name;
    }

    @Override
    public boolean matches(final T value)
    {
        return value.name().equals(name);
    }
}
