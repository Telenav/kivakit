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

package com.telenav.kivakit.kernel.language.strings.conversion;

import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.property.Property;
import com.telenav.kivakit.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An {@link AsString} sub-interface that traverses a tree of objects, adding information to an {@link AsStringIndenter}
 * object as it goes. An {@link AsStringIndenter} handles string indenting, directs recursion and performs reflection on
 * fields and methods that are tagged with the annotation {@literal @}{@link KivaKitIncludeProperty}.
 * <p>
 * The method {@link #asString(StringFormat, AsStringIndenter)} uses the given {@link AsStringIndenter} object to
 * determine if it should recurse or not as well as to perform labeling and indentation of text lines. The {@link
 * #asString()} implementation simply formats this object with a {@link AsStringIndenter} specifying a maximum of 8
 * levels.
 * <p>
 * When the traversal is complete, the {@link AsStringIndenter} object yields an indented debug string.
 *
 * @author jonathanl (shibo)
 * @see AsString
 * @see AsStringIndenter
 * @see KivaKitIncludeProperty
 * @see Property
 * @see Type
 */
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public interface AsIndentedString extends AsString
{
    /**
     * {@inheritDoc}
     */
    @Override
    default String asString(final StringFormat format)
    {
        final var indenter = new AsStringIndenter(format);
        indenter.levels(Maximum._8);
        asString(format, indenter);
        return indenter.toString();
    }

    /**
     * Adds structured information about this object to the given {@link AsStringIndenter} object
     *
     * @param indenter Information about the traversal in progress
     */
    default AsStringIndenter asString(final StringFormat format, final AsStringIndenter indenter)
    {
        indenter.asString(this);
        return indenter;
    }
}
