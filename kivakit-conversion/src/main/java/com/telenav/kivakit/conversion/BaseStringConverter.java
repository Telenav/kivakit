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

package com.telenav.kivakit.conversion;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionPrimitive;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * Base class for conversions to and from String objects.
 *
 * <p><b>Converting to and from Strings</b>
 * <p>
 * Subclasses implement conversion by overriding {@link #onToValue(String)}. If no implementation is given for
 * {@link #onToString(Object)}, a default implementation is provided which simply converts the object to a String by
 * calling {@link #toString()}.
 * </p>
 *
 * <p><b>Empty Strings</b></p>
 *
 * <p>
 * Just as {@link BaseConverter} has an option to allow or disallow null values, {@link BaseStringConverter} has an
 * option to allow or disallow empty strings. An empty string null, zero length or contains nothing but whitespace. The
 * method {@link #allowEmpty(boolean)} can be used to allow empty values (which are not allowed by default), and
 * {@link #allowsEmpty()} will return true if the converter allows empty strings.
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
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramConversion.class)
@UmlClassDiagram(diagram = DiagramConversionPrimitive.class, includeMembers = false)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseStringConverter<Value> extends BaseConverter<String, Value> implements StringConverter<Value>
{
    /** True if empty strings are allowed */
    private boolean allowEmpty;

    /** Function to convert from a string to a value */
    private Function<String, Value> lambda;

    /** A bi-function to convert from string to value, allowing access to this listener in the function */
    private BiFunction<Listener, String, Value> biLambda;

    /**
     * @param listener The conversion listener
     */
    protected BaseStringConverter(Listener listener)
    {
        super(listener);
    }

    /**
     * @param listener The conversion listener
     */
    protected BaseStringConverter(Listener listener, Function<String, Value> lambda)
    {
        super(listener);
        this.lambda = lambda;
    }

    /**
     * @param listener The conversion listener
     */
    protected BaseStringConverter(Listener listener, BiFunction<Listener, String, Value> biLambda)
    {
        super(listener);
        this.biLambda = biLambda;
    }

    /**
     * Specifies whether empty (null or "") strings should be allowed (they will convert to null)
     */
    public BaseStringConverter<Value> allowEmpty(boolean allowEmpty)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final Value onConvert(String string)
    {
        // If we allow null values and our string is null,
        if (allowsNull() && string == null)
        {
            // then return null.
            return null;
        }

        // If we allow empty strings and our string is empty,
        if (allowEmpty && Strings.isEmpty(string))
        {
            // then return null.
            return null;
        }

        // Return the value of our string converted by the subclass.
        return onToValue(string);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String unconvert(Value value)
    {
        // If the value is null
        if (value == null)
        {
            // and we allow null values
            if (allowsNull())
            {
                // then let the subclass convert to a null string representation
                return nullString();
            }
            else
            {
                // otherwise, we can't convert null values
                problem("${class}: Cannot unconvert null value", getClass());
                return null;
            }
        }

        try
        {
            // Call the subclass to convert the value to a string,
            return onToString(value);
        }
        catch (Exception e)
        {
            // and broadcast any exception thrown as a problem
            problem(e, "${class}: Cannot unconvert ${debug}", getClass(), value);
            return null;
        }
    }

    /**
     * @return The string representation of a null value. By default, this value is null, not "null".
     */
    protected String nullString()
    {
        return null;
    }

    /**
     * Convert the given value to a string
     *
     * @param value The (guaranteed non-null, non-empty) value
     * @return A string which is by default value.toString() if this method is not overridden
     */
    protected String onToString(Value value)
    {
        return value.toString();
    }

    /**
     * Implemented by subclass to convert the given string to a value. The subclass implementation will never be called
     * in cases where value is null or empty, so it need not check for either case.
     *
     * @param value The (guaranteed non-null, non-empty) value to convert
     * @return The converted object
     */
    protected Value onToValue(String value)
    {
        ensure(lambda != null ^ biLambda != null, "Must override onToValue() or provide a lambda to constructor");
        return lambda != null
                ? lambda.apply(value)
                : biLambda.apply(this, value);
    }
}
