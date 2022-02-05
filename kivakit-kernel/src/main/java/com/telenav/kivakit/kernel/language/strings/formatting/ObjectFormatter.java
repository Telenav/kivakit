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

import com.telenav.kivakit.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.reflection.Type;
import com.telenav.kivakit.kernel.language.reflection.access.Getter;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.KivaKitIncludeProperty;
import com.telenav.kivakit.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.KernelLimits;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.INCLUDED_FIELDS;
import static com.telenav.kivakit.kernel.language.reflection.property.IncludeProperty.INCLUDED_FIELDS_AND_METHODS;

/**
 * A convenient class for formatting the fields and also particular methods of an object. By default, all fields will be
 * included except fields annotated with {@link KivaKitExcludeProperty}. In addition, all methods annotated with {@link
 * KivaKitIncludeProperty} will be included. Child objects will be formatted recursively. Recursion is limited to
 * objects that are in the list of {@link PackagePath}s passed to the constructor.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SwitchStatementWithTooFewBranches")
@UmlClassDiagram(diagram = DiagramLanguageString.class)
public class ObjectFormatter
{
    public enum Format
    {
        SINGLE_LINE,
        MULTILINE
    }

    private PropertyFilter filter = PropertyFilter.kivakitProperties(INCLUDED_FIELDS_AND_METHODS, INCLUDED_FIELDS);

    /** The package paths to include when recursing */
    private final PackagePath[] include;

    /** The object to format */
    private final Object object;

    public ObjectFormatter(Object object)
    {
        this(object, PackagePath.TELENAV);
    }

    public ObjectFormatter(Object object, PackagePath... include)
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
        var strings = new StringList(KernelLimits.PROPERTIES_PER_OBJECT);
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
            var formatName = annotation.format();
            switch (formatName)
            {
                case "toString":
                    return value.toString();

                default:
                {
                    var listener = (object instanceof Listener) ? (Listener) object : Listener.console();
                    var format = StringFormat.parse(listener, formatName.toUpperCase());
                    Ensure.ensureNotNull(format, "@KivaKitFormatProperty(\"" + format + "\") is not a known format");
                    return ((AsString) value).asString(format);
                }
            }
        }

        // If the value supports debug string generation
        if (value instanceof AsString)
        {
            // get the debug string
            value = ((AsString) value).asString();
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
