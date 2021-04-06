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

package com.telenav.kivakit.core.kernel.data.conversion.string;

import com.telenav.kivakit.core.kernel.data.conversion.Converter;
import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversion;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversionPrimitive;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.kivakit.core.kernel.data.conversion.BaseConverter;

/**
 * Base class for conversions to and from String objects.
 *
 * <p><b>Converting to and from Strings</b>
 *
 * <p>
 * It would be more elegant for this class to implement the {@link Converter} interface twice, but due to type erasure
 * in Java, this class cannot implement both Converter&lt;String, Value&gt; and Converter&lt;Value, String&gt;, so
 * instead it implements Converter&lt;String, Value&gt;, which is the most common case and then it provides a separate
 * method, {@link #toString(Object)} which converts from Value to String. In the event that a Converter&lt;Value,
 * String&gt; is required, the {@link #toStringConverter()} method can be called to retrieve that converter.
 * </p>
 *
 * <p>
 * Subclasses implement conversion by overriding {@link #onConvertToObject(String)}. If no implementation is given for
 * {@link #onConvertToString(Object)}, a default implementation is provided which simply converts the object to a String
 * by calling {@link #toString()}.
 * </p>
 *
 * <p><b>Empty Strings</b></p>
 *
 * <p>
 * Just as {@link BaseConverter} has an option to allow or disallow null values, {@link BaseStringConverter} has an
 * option to allow or disallow empty strings. An empty string null, zero length or contains nothing but whitespace. The
 * method {@link #allowEmpty(boolean)} can be used to allow empty values (which are not allowed by default), and {@link
 * #allowsEmpty()} will return true if the converter allows empty strings.
 * </p>
 *
 * <p><b>Thread Safety</b></p>
 *
 * <p>
 * Note: String converters in general are thread-safe as the base classes do not have mutable state, but some specific
 * string converters implementations may have mutable state, for example, Java formatter objects. Such converters will
 * not be thread-safe.
 * </p>
 *
 * @param <Value> The type to convert to and from
 * @author jonathanl (shibo)
 * @see BaseConverter
 * @see StringConverter
 */
@UmlClassDiagram(diagram = DiagramDataConversion.class)
@UmlClassDiagram(diagram = DiagramDataConversionPrimitive.class, includeMembers = false)
public abstract class BaseStringConverter<Value> extends BaseConverter<String, Value> implements StringConverter<Value>
{
    private BaseConverter<Value, String> toStringConverter;

    /** True if null or empty strings may be converted */
    private boolean allowEmpty;

    /**
     * @param listener The conversion listener
     */
    @UmlExcludeMember
    protected BaseStringConverter(final Listener listener)
    {
        super(listener);
    }

    /**
     * Specifies whether empty (null or "") strings should be allowed (they will convert to null)
     */
    public BaseStringConverter<Value> allowEmpty(final boolean allowEmpty)
    {
        this.allowEmpty = allowEmpty;
        return this;
    }

    /**
     * @return True if this string converter allows empty strings
     */
    public boolean allowsEmpty()
    {
        return allowEmpty;
    }

    @Override
    public final Value onConvert(final String value)
    {
        // If we allow empty values,
        if (allowEmpty)
        {
            // and the value is empty,
            if (Strings.isEmpty(value))
            {
                // return null.
                return null;
            }
        }

        // otherwise, call the subclass converter
        return onConvertToObject(value);
    }

    /**
     * @return A converter that will convert from a {@link String} to the value type
     */
    public final Converter<String, Value> toObjectConverter()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public final String toString(final Value value)
    {
        // If the value is null
        if (value == null)
        {
            // and we allow null values
            if (allowsNull())
            {
                // then let the subclass convert to a null string representation
                return onConvertNullToString();
            }
            else
            {
                // otherwise, we can't convert null values
                problem("${class}: Cannot convert null value", getClass());
                return null;
            }
        }

        try
        {
            // Call the subclass to convert the value to a string,
            return onConvertToString(value);
        }
        catch (final Exception e)
        {
            // and broadcast any exception thrown as a problem
            problem(e, "${class}: Problem converting ${debug}", getClass(), value);
            return null;
        }
    }

    /**
     * @return A Converter interface to this object which converts from Value to String. This is only necessary because
     * Java performs type erasure on generic types.
     */
    @UmlExcludeMember
    public final Converter<Value, String> toStringConverter()
    {
        if (toStringConverter == null)
        {
            toStringConverter = new BaseConverter<>(this)
            {
                @Override
                public String onConvert(final Value value)
                {
                    return onConvertToString(value);
                }
            };
        }
        return toStringConverter;
    }

    /**
     * @return The null string value for an object. By default this value is null, not "null".
     */
    @UmlExcludeMember
    protected String onConvertNullToString()
    {
        return null;
    }

    /**
     * Implemented by subclass to convert a given string value to an object. The subclass will never be called in cases
     * where value is null, so it need not check for that case.
     *
     * @param value The (guaranteed non-null) value to convert
     * @return The converted object
     */
    protected abstract Value onConvertToObject(String value);

    /**
     * Convert the given value to a string
     *
     * @param value The (guaranteed non-null) value
     * @return A string which is by default value.toString() if this method is not overridden
     */
    protected String onConvertToString(final Value value)
    {
        return value.toString();
    }
}
