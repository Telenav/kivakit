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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramString;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.language.reflection.property.Property;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.TEXT;

/**
 * An {@link StringFormattable} sub-interface that traverses a tree of objects, adding information to an
 * {@link ObjectIndenter} object as it goes. An {@link ObjectIndenter} handles string indenting, directs recursion and
 * performs reflection on fields and methods that are tagged with the annotation
 * {@literal @}{@link KivaKitIncludeProperty}.
 * <p>
 * The method {@link #asString(Format, ObjectIndenter)} uses the given {@link ObjectIndenter} object to determine if it
 * should recurse or not as well as to perform labeling and indentation of text lines. The {@link #asString()}
 * implementation simply formats this object with a {@link ObjectIndenter} specifying a maximum of 8 levels.
 * <p>
 * When the traversal is complete, the {@link ObjectIndenter} object yields an indented debug string.
 *
 * @author jonathanl (shibo)
 * @see StringFormattable
 * @see ObjectIndenter
 * @see KivaKitIncludeProperty
 * @see Property
 * @see Type
 */
@UmlClassDiagram(diagram = DiagramString.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface AsIndentedString extends StringFormattable
{
    /**
     * Adds structured information about this object to the given {@link ObjectIndenter} object
     *
     * @param indenter Information about the traversal in progress
     */
    default ObjectIndenter asString(Format format, ObjectIndenter indenter)
    {
        indenter.asString(this);
        return indenter;
    }

    @Override
    default String asString()
    {
        return asString(TEXT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default String asString(Format format)
    {
        var indenter = new ObjectIndenter(format);
        indenter.levels(Maximum._8);
        asString(format, indenter);
        return indenter.toString();
    }
}
