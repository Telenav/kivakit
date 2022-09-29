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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversion;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Base class for implementing two-way converters.
 *
 * <p>
 * Adds to {@link BaseConverter} an implementation of {@link #unconvert(Object)} that checks for null, catches
 * exceptions and called {@link #onUnconvert(Object)}.
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
 * @see Glitch
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramConversion.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public abstract class BaseTwoWayConverter<From, To> extends BaseConverter<From, To> implements TwoWayConverter<From, To>
{
    /**
     * @param listener Listener to report problems to
     */
    protected BaseTwoWayConverter(Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public From unconvert(To value)
    {
        // If the value is null
        if (value == null)
        {
            // and we allow null values
            if (allowsNull())
            {
                // then let the subclass convert to a null string representation
                return nullValue();
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
            return onUnconvert(value);
        }
        catch (Exception e)
        {
            // and broadcast any exception thrown as a problem
            problem(e, "${class}: Cannot unconvert ${debug}", getClass(), value);
            return null;
        }
    }

    /**
     * Returns logical value to use for null
     */
    protected From nullValue()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    protected abstract From onUnconvert(To to);
}
