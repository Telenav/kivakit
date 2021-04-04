////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.strings.formatting;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.access.Getter;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitExcludeProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitProperties;
import com.telenav.kivakit.core.kernel.language.strings.conversion.AsString;
import com.telenav.kivakit.core.kernel.language.strings.conversion.StringFormat;
import com.telenav.kivakit.core.kernel.project.CoreKernelLimits;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageString;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Collection;

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

    private PropertyFilter filter = KivaKitProperties.INCLUDED_PROPERTIES_AND_FIELDS;

    /** The package paths to include when recursing */
    private final PackagePath[] include;

    /** The object to format */
    private final Object object;

    public ObjectFormatter(final Object object)
    {
        this(object, PackagePath.TELENAV);
    }

    public ObjectFormatter(final Object object, final PackagePath... include)
    {
        this.object = object;
        this.include = include;
    }

    public ObjectFormatter filter(final PropertyFilter filter)
    {
        this.filter = filter;
        return this;
    }

    @Override
    public String toString()
    {
        return toString(ObjectFormatter.Format.SINGLE_LINE);
    }

    public String toString(final Format format)
    {
        if (object == null)
        {
            return "null";
        }
        final Type<?> type = Type.of(object);
        final var strings = new StringList(CoreKernelLimits.PROPERTIES_PER_OBJECT);
        for (final var property : type.properties(filter))
        {
            if (!"class".equals(property.name()))
            {
                final var getter = property.getter();
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
            final var name = type.simpleName();
            if (format == ObjectFormatter.Format.SINGLE_LINE)
            {
                return "[" + name + (!strings.isEmpty() ? " " : "") + strings.toString() + "]";
            }
            else
            {
                return "\n" + strings.indented(4).join("");
            }
        }
    }

    private Object toString(final Getter getter, Object value)
    {
        final var annotation = getter.annotation(KivaKitFormatProperty.class);
        if (annotation != null)
        {
            final var formatName = annotation.format();
            switch (formatName)
            {
                case "toString":
                    return value.toString();

                default:
                {
                    final var format = StringFormat.of(formatName.toUpperCase());
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
            final Type<?> valueType = Type.of(value);

            // If the value doesn't directly implement toString and it's not a
            // system type (something implemented by Java)
            if (!valueType.declaresToString() && !valueType.isSystem())
            {
                // Go through each package path in our path list
                for (final var packagePath : include)
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
