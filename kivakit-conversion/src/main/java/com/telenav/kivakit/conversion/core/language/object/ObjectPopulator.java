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

package com.telenav.kivakit.conversion.core.language.object;

import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionLanguage;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.language.reflection.property.PropertyValue;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Populates object properties given a property filter and a source of property values.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramConversionLanguage.class)
public class ObjectPopulator extends BaseRepeater
{
    private final Source<PropertyValue> source;

    private final PropertyFilter filter;

    public ObjectPopulator(PropertyFilter filter, Source<PropertyValue> source)
    {
        this.filter = filter;
        this.source = source;
    }

    /**
     * Populates the given object using the values obtained from the property value source
     *
     * @param object The object to populate
     * @return The populated object (for method chaining)
     */
    public <T> T populate(T object)
    {
        // Go through each property on the object,
        for (var property : Type.type(object).properties(filter))
        {
            // get any value for the given property,
            var value = source.get().propertyValue(property);

            // and if the value is non-null,
            if (value != null)
            {
                // set the property value,
                var error = property.set(object, () -> value);

                // and if something went wrong,
                if (error != null)
                {
                    // notify any listeners.
                    warning(error.toString());
                }
            }
            else
            {
                if (!property.isOptional())
                {
                    warning("No value found for property: " + property.name());
                }
            }
        }

        return object;
    }
}
