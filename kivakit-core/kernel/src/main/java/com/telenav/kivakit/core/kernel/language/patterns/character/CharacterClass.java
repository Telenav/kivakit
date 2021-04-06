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

package com.telenav.kivakit.core.kernel.language.patterns.character;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.patterns.Expression;
import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguagePattern;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

@UmlClassDiagram(diagram = DiagramLanguagePattern.class)
public class CharacterClass extends Pattern
{
    private final String characters;

    public CharacterClass(final Object... characters)
    {
        final var builder = new StringBuilder();
        for (final var object : characters)
        {
            if (object instanceof String)
            {
                builder.append(object.toString());
            }
            else if (object instanceof CharacterClass)
            {
                builder.append(((CharacterClass) object).characters);
            }
            else if (object instanceof LiteralCharacter)
            {
                builder.append(((LiteralCharacter) object).toExpression());
            }
            else if (object instanceof Expression)
            {
                builder.append(((Expression) object).toExpression());
            }
            else
            {
                Ensure.fail();
            }
        }
        this.characters = builder.toString();
    }

    public CharacterClass(final String characters)
    {
        this.characters = characters;
    }

    @Override
    public int bind(final int group)
    {
        return group;
    }

    public CharacterClass inverted()
    {
        return new CharacterClass("^" + characters);
    }

    @Override
    public String toExpression()
    {
        return "[" + characters + "]";
    }

    public CharacterClass with(final Character character)
    {
        return new CharacterClass(characters + character);
    }

    public CharacterClass with(final LiteralCharacter character)
    {
        return new CharacterClass(characters + character);
    }

    public CharacterClass withAlphabetic()
    {
        return withUpperCaseAlphabetic().withLowerCaseAlphabetic();
    }

    public CharacterClass withAlphanumeric()
    {
        return withAlphabetic().withNumeric();
    }

    public CharacterClass withCharacter(final char c)
    {
        return new CharacterClass(characters + c);
    }

    public CharacterClass withLowerCaseAlphabetic()
    {
        return withRange('a', 'z');
    }

    public CharacterClass withNumeric()
    {
        return withRange('0', '9');
    }

    public CharacterClass withRange(final char first, final char last)
    {
        if (last < first)
        {
            return Ensure.fail("Invalid range");
        }
        return new CharacterClass(characters + first + "-" + last);
    }

    public CharacterClass withUpperCaseAlphabetic()
    {
        return withRange('A', 'Z');
    }
}
