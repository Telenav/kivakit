////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.conversion;

import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.messaging.Broadcaster;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.Transceiver;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataConversion;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Base class for implementing converters. The inherited {@link Converter#convert(Object)} method converts from the From
 * type to the To type. Whether the conversion allows null values or not can be specified with {@link
 * #allowNull(boolean)}. Converters are used extensively in KivaKit, including for switch parsing and populating
 * objects from properties files.
 *
 * <p>
 * Since this class extends {@link BaseRepeater} and implements the {@link Repeater} interface, it can both hear
 * messages broadcast by sub-converters and broadcast messages about the conversion itself.
 * </p>
 *
 * <p>
 * For example, if there is a problem with a conversion, {@link Transceiver#problem(String, Object...)} (which {@link
 * Repeater} indirectly extends) can broadcast a {@link Problem} message to any clients of the converter. Similarly
 * {@link Warning} and {@link Quibble} messages can be broadcast with {@link #warning(String, Object...)} and {@link
 * #quibble(String, Object...)}.
 * </p>
 *
 * @param <From> The type to convert from
 * @param <To> The type to convert to
 * @author jonathanl (shibo)
 * @see Repeater
 * @see BaseRepeater
 * @see Broadcaster
 * @see Problem
 * @see Warning
 * @see Quibble
 */
@UmlClassDiagram(diagram = DiagramDataConversion.class)
public abstract class BaseConverter<From, To> extends BaseRepeater implements Converter<From, To>
{
    /** True if this converter allows null values */
    private boolean allowNull;

    protected BaseConverter(final Listener listener)
    {
        listener.listenTo(this);
    }

    /**
     * @param allowNull True if null values should be converted to null
     */
    public BaseConverter<From, To> allowNull(final boolean allowNull)
    {
        this.allowNull = allowNull;
        return this;
    }

    /**
     * @return True if this converter allows null values, false if a problem will be broadcast when a null value is
     * encountered.
     */
    public boolean allowsNull()
    {
        return allowNull;
    }

    /**
     * Converts from the &lt;From&gt; type to the &lt;To&gt; type. If the from value is null and the converter allows
     * null values, null will be returned. If the value is null and the converter does not allow null values a problem
     * will be broadcast. Any exceptions that occur during conversion are caught and broadcast as problems.
     */
    @Override
    @UmlExcludeMember
    public final To convert(final From from)
    {
        // If the value is null,
        if (from == null)
        {
            // and we don't allow conversion of null values,
            if (!allowsNull())
            {
                // then broadcast a problem
                problem(problemBroadcastFrequency(), "${class}: Cannot convert null value", subclass());
            }

            return null;
        }

        try
        {
            // Convert any non-null value, returning the result
            return onConvert(from);
        }
        catch (final Exception e)
        {
            // and if an exception occurs, broadcast a problem
            problem(problemBroadcastFrequency(), e, "${class}: Problem converting ${debug}", subclass(), from);

            // and return null.
            return null;
        }
    }

    /**
     * The method to override to provide the conversion
     */
    protected abstract To onConvert(From value);

    /**
     * @return The maximum {@link Frequency} to broadcast problems at, for example, every minute or once an hour
     */
    @UmlExcludeMember
    protected Frequency problemBroadcastFrequency()
    {
        return null;
    }

    @UmlExcludeMember
    protected Class<?> subclass()
    {
        return getClass();
    }
}
