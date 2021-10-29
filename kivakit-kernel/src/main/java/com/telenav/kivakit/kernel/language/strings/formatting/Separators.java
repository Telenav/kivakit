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

package com.telenav.kivakit.kernel.language.strings.formatting;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Maintains a list of separators that can be nested when used by list converters and converters with multiple separated
 * fields.
 * <p>
 * An explicit list of separators can be given, or separators can be automatically generated, depending on which
 * constructor is used. During conversion, the current() method yields the separator for the current level of nesting,
 * while child(N) yields the separator for the current level + N, where N is greater than or equal to 0. When
 * sub-converters are called recursively, the child() method creates a copy of the separators with the current level
 * increased by one.
 * <p>
 * For example:
 * <p>
 * Given the DEFAULT separators of "/", ":" and ",", coding a list of people and their locations might look like:
 * <p>
 * jon:45.123,-121.1231/matthieu:45.817,-123.1872
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class Separators
{
    public static final Separators DEFAULT = new Separators("/", ":", ",");

    private int nestingLevel;

    private final String[] separators;

    public Separators()
    {
        this("[[", "]]", 16);
    }

    public Separators(String... separators)
    {
        this.separators = separators;
    }

    public Separators(String before, String after, int levels)
    {
        separators = new String[levels];
        for (var i = 0; i < levels; i++)
        {
            separators[i] = before + i + after;
        }
    }

    private Separators(int nestingLevel, String[] separators)
    {
        this.nestingLevel = nestingLevel;
        this.separators = separators;
    }

    public Separators child()
    {
        return new Separators(nestingLevel + 1, separators);
    }

    public String child(int relativeLevel)
    {
        return separators[nestingLevel + relativeLevel];
    }

    public String current()
    {
        return child(0);
    }
}
