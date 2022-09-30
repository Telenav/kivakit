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
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.interfaces.string.StringFormattable.Format;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.MORE_TESTING_NEEDED;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.string.ObjectFormatter.ObjectFormat.SINGLE_LINE;

/**
 * A convenient class for formatting particular fields and methods of an object as a debugging string. Fields to be
 * included in the formatted string must be marked with the {@link KivaKitFormat} annotation, which describes what
 * format to use for each annotated member. Child objects will be formatted recursively.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = MORE_TESTING_NEEDED,
            documentation = FULLY_DOCUMENTED)
public class ObjectFormatter
{
    /**
     * The format to use for properties of an object. This is independent of the format for each individual property,
     * which is supplied by {@link KivaKitFormat}.
     */
    public enum ObjectFormat
    {
        /** Object should be formatted as a single line */
        SINGLE_LINE,

        /** Object should be formatted in multiple lines */
        MULTILINE
    }

    /** The object to format */
    private final Object object;

    /**
     * @param object The object to format
     */
    public ObjectFormatter(Object object)
    {
        this.object = object;
    }

    /**
     * Formats the properties of the object with the given format
     *
     * @param objectFormat The format
     * @return The formatted properties string
     */
    public String asString(ObjectFormat objectFormat)
    {
        if (object == null)
        {
            return "null";
        }

        Type<?> type = Type.type(object);
        var strings = new StringList();
        for (var property : type.properties(PropertyFilter.allProperties()))
        {
            if (!"class".equals(property.name()))
            {
                var getter = property.getter();
                if (getter != null)
                {
                    var annotation = getter.annotation(KivaKitFormat.class);
                    if (annotation != null)
                    {
                        var value = getter.get(object);
                        if (value == null)
                        {
                            strings.add(property.name() + " = null");
                        }
                        else
                        {
                            value = asString(objectFormat, annotation.format(), 0, value);
                            strings.add(property.name() + " = " + value);
                        }
                    }
                }
            }
        }

        // If there were no properties added
        if (strings.isEmpty())
        {
            // that is a problem
            return fail("Unable to find any properties to format in object of type $", type);
        }
        else
        {
            var name = type.simpleName();
            if (objectFormat == SINGLE_LINE)
            {
                return "[" + name + (!strings.isEmpty() ? " " : "") + strings + "]";
            }
            else
            {
                return strings.bracketed(4);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return asString(SINGLE_LINE);
    }

    /**
     * Converts the given object to a string in the given format
     */
    private String asString(ObjectFormat objectFormat, Format propertyFormat, int recursionLevel, Object propertyValue)
    {
        ensureNotNull(propertyFormat, "@KivaKitFormat(\"$\") is not a known format", propertyFormat);

        switch (propertyFormat)
        {
            case TO_STRING:
                return propertyValue.toString();

            case TEXT:
            default:
            {
                return format(objectFormat, propertyFormat, recursionLevel, propertyValue);
            }
        }
    }

    /**
     * Returns the given object in the given format
     */
    private String format(ObjectFormat objectFormat, Format propertyFormat, int recursionLevel, Object propertyValue)
    {
        // If the property value supports string formatting,
        if (propertyValue instanceof StringFormattable)
        {
            // return the value for the property
            return ((StringFormattable) propertyValue).asString(propertyFormat);
        }
        // If the property value is an enum,
        else if (propertyValue instanceof Enum<?>)
        {
            // return its name
            return ((Enum<?>) propertyValue).name();
        }
        // If the property value is a collection,
        else if (propertyValue instanceof Collection)
        {
            // go through each value in the collection,
            var formatted = new StringList();
            for (var at : (Collection<?>) propertyValue)
            {
                // and add the formatted string for the value
                formatted.add(asString(objectFormat, propertyFormat, recursionLevel + 1, at));
            }

            // then, return the properties as a single line or a bracketed multi-line string.
            return objectFormat == SINGLE_LINE
                    ? formatted.join(", ")
                    : formatted.bracketed(recursionLevel * 4);
        }
        else
        {
            // Get the type object for this value
            Type<?> valueType = Type.type(propertyValue);

            // If the value doesn't directly implement toString, and it's not a
            // system type (something implemented by Java)
            if (!valueType.declaresToString() && !valueType.isSystem())
            {
                // it's safe to recursively format because it's our code
                return new ObjectFormatter(propertyValue).toString();
            }

            return propertyValue.toString();
        }
    }
}