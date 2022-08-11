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

package com.telenav.kivakit.core.language.object;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.language.module.PackageReference;
import com.telenav.kivakit.core.language.reflection.Getter;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.core.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.core.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.core.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.language.reflection.property.PropertyMembers;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

/**
 * A convenient class for formatting the fields and also particular methods of an object. By default, all fields will be
 * included except fields annotated with {@link KivaKitExcludeProperty}. In addition, all methods annotated with {@link
 * KivaKitIncludeProperty} will be included. Child objects will be formatted recursively. Recursion is limited to
 * objects that are in the list of {@link PackageReference}s passed to the constructor.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SwitchStatementWithTooFewBranches")
@UmlClassDiagram(diagram = DiagramReflection.class)
public class ObjectFormatter
{
    public enum Format
    {
        SINGLE_LINE,
        MULTILINE
    }

    private PropertyFilter filter = PropertyFilter.kivakitProperties(PropertyMembers.INCLUDED_FIELDS_AND_METHODS, PropertyMembers.INCLUDED_FIELDS);

    /** The package paths to include while formatting recursively */
    private final PackageReference[] include;

    /** The object to format */
    private final Object object;

    public ObjectFormatter(Object object)
    {
        this(object, PackageReference.TELENAV);
    }

    public ObjectFormatter(Object object, PackageReference... include)
    {
        this.object = object;
        this.include = include;
    }

    public ObjectFormatter filter(PropertyFilter filter)
    {
        this.filter = filter;
        return this;
    }

    @Override
    public String toString()
    {
        return toString(ObjectFormatter.Format.SINGLE_LINE);
    }

    public String toString(Format format)
    {
        if (object == null)
        {
            return "null";
        }
        Type<?> type = Type.of(object);
        var strings = new StringList();
        for (var property : type.properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                var getter = property.getter();
                if (getter != null)
                {
                    var value = getter.get(object);
                    if (value == null)
                    {
                        strings.add(property.name() + " = null");
                    }
                    else
                    {
                        value = toString(getter, value);
                        strings.add(property.name() + " = " + value);
                    }
                }
            }
        }

        // If there were no properties added
        if (strings.isEmpty())
        {
            // that is a problem
            Ensure.fail("Unable to find any properties to format in object of type $", type);
            return null;
        }
        else
        {
            var name = type.simpleName();
            if (format == ObjectFormatter.Format.SINGLE_LINE)
            {
                return "[" + name + (!strings.isEmpty() ? " " : "") + strings + "]";
            }
            else
            {
                return "\n" + strings.indented(4).join("");
            }
        }
    }

    private Object toString(Getter getter, Object value)
    {
        var annotation = getter.annotation(KivaKitFormatProperty.class);
        if (annotation != null)
        {
            var format = annotation.format();
            switch (format)
            {
                case TO_STRING:
                    return value.toString();

                default:
                {
                    Ensure.ensureNotNull(format, "@KivaKitFormatProperty(\"" + format + "\") is not a known format");
                    return ((Stringable) value).asString(format);
                }
            }
        }

        // If the value supports debug string generation
        if (value instanceof Stringable)
        {
            // get the debug string
            value = ((Stringable) value).asString();
        }
        else if (value instanceof Enum<?>)
        {
            value = ((Enum<?>) value).name();
        }
        else if (value instanceof Collection)
        {
            value = value.toString();
        }
        else
        {
            // Get the type object for this value
            Type<?> valueType = Type.of(value);

            // If the value doesn't directly implement toString and it's not a
            // system type (something implemented by Java)
            if (!valueType.declaresToString() && !valueType.isSystem())
            {
                // Go through each package path in our path list
                for (var packagePath : include)
                {
                    // If the value's class is declared inside this package
                    if (valueType.isInside(packagePath))
                    {
                        // it's safe to recursively format because it's our code
                        value = new ObjectFormatter(value).toString();
                    }
                }
            }
        }
        return value;
    }
}
