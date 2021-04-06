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

package com.telenav.kivakit.data.formats.library.text;

import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.populator.ObjectPopulator;
import com.telenav.kivakit.core.kernel.language.reflection.property.Property;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitProperties;
import com.telenav.kivakit.data.formats.library.text.project.lexakai.diagrams.DiagramText;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramText.class)
public class TextLine extends StringList implements PropertyValueSource
{
    private final transient TextReader stream;

    TextLine(final TextReader stream)
    {
        this.stream = stream;
    }

    public <T> T as(final TextColumn column, final Converter<String, T> converter)
    {
        return converter.convert(get(column));
    }

    public <T> T asObject(final Class<T> type)
    {
        try
        {
            return new ObjectPopulator(stream, KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS, this).populate(Type.forClass(type).newInstance());
        }
        catch (final Exception e)
        {
            stream.problem(e, "Unable to populate ${debug}", type);
            return null;
        }
    }

    public String get(final TextColumn column)
    {
        return get(column.getIndex());
    }

    @Override
    public Object valueFor(final Property property)
    {
        final var column = stream.columnForName(property.name());
        if (column != null)
        {
            return get(column);
        }
        return null;
    }
}
